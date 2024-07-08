package by.statistic.service;

import by.pm.model.*;
import by.statistic.api.client.PmClient;
import by.statistic.model.Balance;
import by.statistic.model.Change;
import by.statistic.repository.*;
import by.statistic.service.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

import static by.statistic.utils.DateTimeUtils.*;

@Service
@RequiredArgsConstructor
public class StatisticService {

    @Value("${pm.cs.name}")
    private String counterStrikeName;
    @Value("${pm.stake-type.total.id}")
    private String totalStakeTypeId;

    private final PmClient pmClient;

    private final GameMapper gameMapper;
    private final MatchMapper matchMapper;
    private final SportTypeMapper sportTypeMapper;
    private final StakeMapper stakeMapper;
    private final StakeTypeMapper stakeTypeMapper;
    private final TeamMapper teamMapper;
    private final TourneyMapper tourneyMapper;


    private final TourneyRepository tourneyRepository;
    private final BalanceRepository balanceRepository;
    private final ChangeRepository changeBalanceRepository;
    private final MatchRepository matchRepository;
    private final SportTypeRepository sportTypeRepository;
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final StakeTypeRepository stakeTypeRepository;
    private final StakeRepository stakeRepository;


    // check ending of matches in database
    public void method1() {
        /*
        get all changes with change is not empty
        get event by matchId and check status
        if (isWinner exists) -> get Stake in match and check winner by sideName(id)
                                                   change isWinner in Match(stake)
                                                   create balance change and save in db
                                                   modify total balance


        */
    }

    @Transactional
    public void method2() {
        /*
        get tourneys from DB
        get all new matches(futures)
        check new matches(tomorrow) of exists tourneys in DB
            if (matches(tourney) exist) -> check teams exists in DB (save if not exists in DB)
                                                   create stake(compare ratios of two teams) and if stakeType don't exist in DB save it
                                                   create DB match model -> save in DB
                                                   place bet, create change balance with empty change
            else (tourney not exists) ->  check game exists in DB (save)
                                                check SportType exists in DB (save)
                                                ---/\--- repeat if body ---/\---
        */
        Sport sport = pmClient.getFutureMatches(); // just 1 send method  to pm.service because can be lock
//        saveTourneys(sport); now save by hand
        Game game = getGameByName(sport.getGameList(), counterStrikeName);
        for (Tourney tourney : game.getTourneyList()) {
            if (tourneyRepository.existsById(tourney.getId())) {
                for (Match match : tourney.getMatchList()) {
                    if (isAvailableToBet(tourney, match)) {
                        placeBet(tourney, match);
                    }
                }
            }
        }
    }

    private void placeBet(Tourney tourney, Match match) {
        //save teams
        by.statistic.model.Team savedFirstTeam = saveTeam(match.getFirstTeam());
        by.statistic.model.Team savedSecondTeam = saveTeam(match.getSecondTeam());
        //save stake
        by.statistic.model.Stake savedStake = saveStake(match);
        //save match
        by.statistic.model.Match savedMatch = matchMapper.toDbModel(match);
        savedMatch.setFirstTeam(savedFirstTeam);
        savedMatch.setSecondTeam(savedSecondTeam);
        savedMatch.setStake(savedStake);
        savedMatch = matchRepository.save(savedMatch);
        //placeBet расчет суммы, поиск id для ставок, сделать ставку
        Stake stake = getBetStake(getTotalStakeType(match).getStakes());
        Double betAmount = calculateCashForBet(tourney, stake);
        //        pmClient.placeBet(match.getId(), stake.getId(), calculateCashForBet(tourney, stake));
        //for testing!!!
        System.out.println("Bet for event: " + match.getId() +
                "\nStake id: " + stake.getId() +
                "\nCash for bet: " + betAmount);
        //change totalBalance
        changeTotalBalance(betAmount); // изменение основного баланса
        //save BalanceChange
        saveDefultChange(savedMatch); // запись того, что ставка сделана и повлияет на баланс
    }

    /**
     * Save BalanceChange in DB
     */
    private Change saveDefultChange(by.statistic.model.Match match) {
        Change change = new Change();
        change.setBalance(totalBalance());
        change.setDate(LocalDateTime.now());
        change.setMatch(match);
        return changeBalanceRepository.save(change);
    }

    /**
     * Save Balance in DB with new changes
     */
    private void changeTotalBalance(Double change) {
        Balance balance = totalBalance();
        var newBalance = balance.getBalance().add(BigDecimal.valueOf(change));
        balance.setBalance(newBalance);
        balanceRepository.save(balance);
    }

    /**
     * Return cash for bet by catchUp strategy
     */
    private Double calculateCashForBet(Tourney tourney, Stake stake) {
        by.statistic.model.Tourney betTourney = tourneyRepository.getReferenceById(tourney.getId());
        if (betTourney.getLosses().compareTo(BigDecimal.ZERO) == 0) {
            return totalBalance().getDefaultBet().doubleValue();
        } else {
            double losses = betTourney.getLosses().doubleValue();
            double netProfit = 0.0;
            return (losses + netProfit) / (stake.getRatio() - 1);
        }
    }

    /**
     * Save Stake in Db
     */
    private by.statistic.model.Stake saveStake(Match match) {
        var stakeType = getTotalStakeType(match);
        by.statistic.model.StakeType savedStakeType = saveStakeType(stakeType);
        by.statistic.model.Stake savedStake =
                stakeMapper.toDbModel(getBetStake(stakeType.getStakes()));
        savedStake.setStakeType(savedStakeType);
        return stakeRepository.save(savedStake);
    }

    /**
     * Get Stake for Bet with min ratio
     */
    private Stake getBetStake(List<Stake> stakes) {
        return stakes.get(0).getRatio() > stakes.get(1).getRatio()
                ? stakes.get(1) : stakes.get(0);
    }


    /**
     * Save StakeType in DB if needed
     */
    private by.statistic.model.StakeType saveStakeType(StakeType stakeType) {
        return stakeTypeRepository.existsById(stakeType.getId()) ?
                stakeTypeRepository.getReferenceById(stakeType.getId()) :
                stakeTypeRepository.save(
                        stakeTypeMapper.toDbModel(stakeType));
    }

    /**
     * Get Stake type "Исход (2 исхода)"
     */
    private StakeType getTotalStakeType(Match match) {
        return match.getStakeTypes()
                .stream()
                .filter(stakeType -> stakeType.getId().toString().equals(totalStakeTypeId))
                .findFirst().orElseThrow();
    }

    /**
     * Save team in DB if needed
     */
    private by.statistic.model.Team saveTeam(String teamName) {
        return teamRepository.existsByName(teamName) ?
                teamRepository.findByName(teamName) :
                teamRepository.save(
                        teamMapper.toDbModel(teamName));
    }


    /**
     * Save tourney in DB if needed
     */
    private void saveTourneys(Sport sport) throws ParseException {
        by.statistic.model.SportType savedSportType = saveSportType(sport);
        Game game = getGameByName(sport.getGameList(), counterStrikeName);
        by.statistic.model.Game savedGame = saveGame(game);
        for (Tourney tourney : game.getTourneyList()) {
            if (isAvailableToSave(tourney)) {
                by.statistic.model.Tourney tourneyToSave = tourneyMapper.toDbModel(tourney);
                tourneyToSave.setSportType(savedSportType);
                tourneyToSave.setGame(savedGame);
                tourneyRepository.save(tourneyToSave);
            }
        }
    }

    /**
     * Save SportType in DB if needed
     */
    private by.statistic.model.SportType saveSportType(Sport sport) {
        return sportTypeRepository.existsById(sport.getId()) ?
                sportTypeRepository.getReferenceById(sport.getId()) :
                sportTypeRepository.save(
                        sportTypeMapper.toDbModel(sport));
    }

    /**
     * Save Game in DB if needed
     */
    private by.statistic.model.Game saveGame(Game game) {
        return gameRepository.existsById(game.getId()) ?
                gameRepository.getReferenceById(game.getId()) :
                gameRepository.save(
                        gameMapper.toDbModel(game));
    }

    /**
     * Return game by name
     */
    private Game getGameByName(List<Game> gameList, String name) {
        return gameList.stream()
                .filter(game -> game.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }

    /**
     * Return balance from DB or throw
     */
    private Balance totalBalance() {
        return balanceRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Balance with id = 1 not found. Check Database!!!"));
    }

    /**
     * Check is tourney available to save
     */
    private boolean isAvailableToSave(Tourney tourney) throws ParseException {
        if (totalBalance().getTourneyCount()
                == tourneyRepository.count()) {
            return false;
        } else if (tourney.getTitle().contains("America")
                || tourney.getTitle().contains("2x2")) {
            return false;
        } else return isTourneyMatchStartedIn3Days(tourney);
    }

    /**
     * Check all conditions availability to place bet at this betMatch
     */
    private boolean isAvailableToBet(Tourney tourney, Match betMatch) {
        if (matchRepository.existsById(betMatch.getId())) { //repeat bet check
            return false;
        }
        List<by.statistic.model.Match> notCompleteMatches = changeBalanceRepository.findAllByMatchTourneyTitle(tourney.getTitle())
                .stream()
                .filter(change -> change.getChange().equalsIgnoreCase("-"))
                .map(Change::getMatch)
                .toList();
        for (by.statistic.model.Match match : notCompleteMatches) {
            if (match.getTourney().getId().equals(tourney.getId()) && // is the same tourney
                    isTimeBetweenMatchesMoreThan30Min(betMatch.getDate(), match.getDate())) { //is time between matches more than 30 min
                return false;
            }
        }
        return true;
    }

}
