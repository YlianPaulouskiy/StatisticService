package by.statistic.service;

import by.pm.model.*;
import by.statistic.api.client.PmClient;
import by.statistic.model.SportType;
import by.statistic.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {

    @Value("${pm.cs.name}")
    private String counterStrikeName;
    private final PmClient pmClient;
    private final TourneyRepository tourneyRepository;
    private final BalanceRepository balanceRepository;
    private final ChangeRepository changeBalanceRepository;
    private final SportTypeRepository sportTypeRepository;
    private final GameRepository gameRepository;


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


    }

    private void saveTourneys() throws ParseException {
        Sport sport = pmClient.getFutureMatches();
        saveSportType(sport);
        Game game = getGameByName(sport.getGameList(), counterStrikeName);
        saveGame(game);
        for (Tourney tourney : game.getTourneyList()) {
            if (isAvailableToSave(tourney)) {
                by.statistic.model.Tourney tourneyToSave = new by.statistic.model.Tourney();
                tourneyToSave.setId(tourney.getId());
                tourneyToSave.setTitle(tourney.getTitle());
                if (sportTypeRepository.existsById(sport.getId())) {
                    tourneyToSave.setSportType(
                            sportTypeRepository.getReferenceById(sport.getId()));
                } else {
                    throw new RuntimeException("SportType with id " + sport.getId() + " is not exists.");
                }
                if (gameRepository.existsById(game.getId())) {
                    tourneyToSave.setGame(
                            gameRepository.getReferenceById(game.getId()));
                } else {
                    throw new RuntimeException("Game with id " + game.getId() + " is not exists.");
                }
                tourneyRepository.save(tourneyToSave);
            }
        }
    }

    /**
     * Save SportType in DB if needed
     */
    private void saveSportType(Sport sport) {
        if (!sportTypeRepository.existsById(sport.getId())) {
            by.statistic.model.SportType sportType = new by.statistic.model.SportType();
            sportType.setId(sport.getId());
            sportType.setName(sportType.getName());
            sportTypeRepository.save(sportType);
        }
    }

    /**
     * Save Game in DB if needed
     */
    private void saveGame(Game game) {
        if (!gameRepository.existsById(game.getId())) {
            by.statistic.model.Game gameToSave = new by.statistic.model.Game();
            gameToSave.setId(game.getId());
            gameToSave.setName(game.getName());
            gameRepository.save(gameToSave);
        }
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
     * Check is tourney available to save
     */
    private boolean isAvailableToSave(Tourney tourney) throws ParseException {
        if (balanceRepository.getReferenceById(1).getTourneyCount()
                == tourneyRepository.count()) {
            return false;
        } else if (tourney.getTitle().contains("America")
                || tourney.getTitle().contains("2x2")) {
            return false;
        } else return isWaitingIn3Days(tourney);
    }

    /**
     * Check if matches of Tourney is started the next 3 days
     */
    private boolean isWaitingIn3Days(Tourney tourney) throws ParseException {
        return tourney.getMatchList().get(0)
                .getDate().before(after3daysFromNow());
    }

    /**
     * Get Date after 3 days from now
     */
    private Date after3daysFromNow() throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(
                LocalDate.now().plusDays(3L).toString());
    }

}
