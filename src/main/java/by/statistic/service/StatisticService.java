package by.statistic.service;

import by.statistic.model.Change;
import by.statistic.model.Match;
import by.statistic.model.Tourney;
import by.statistic.repository.BalanceRepository;
import by.statistic.repository.ChangeRepository;
import by.statistic.repository.TourneyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final TourneyRepository tourneyRepository;
    private final BalanceRepository balanceRepository;
    private final ChangeRepository changeBalanceRepository;


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

    // check new matches
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

        var matches = List.of(new Match()); // future

        for (Match match : matches) {
            if (isTourneyAvailable(match.getTourney())) {
                if (isSaveTourneyAvailable()) {
                    //save tourney
                    //if need save game and sportType
                }

                //проверка есть ли на этот турнир уже ставка
                // выбор подходящего турнира
            }
        }


    }

    private boolean isNotBetAtThisTourney(Tourney tourney) {
        return changeBalanceRepository
                .findAllByMatchTourneyTitle(tourney.getTitle())
                .stream()
                .anyMatch(change -> change.getChange().equals("-"));
    }


    private boolean isSaveTourneyAvailable() {
        return balanceRepository.getReferenceById(1).getTourneyCount()
                != tourneyRepository.count();
    }

    private boolean isTourneyAvailable(Tourney tourney) {
        return !tourney.getTitle().contains("America")
                && !tourney.getTitle().contains("2x2");
    }

    private boolean isTomorrowDate(LocalDateTime date) {
        return date.toLocalDate()
                .isEqual(
                        LocalDate.now().plusDays(1L));
    }

}
