package by.statistic.utils;

import by.pm.model.Tourney;
import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

@UtilityClass
public class DateTimeUtils {

    /**
     * Check if matches of Tourney is started the next 3 days
     */
    public static boolean isTourneyMatchStartedIn3Days(Tourney tourney) throws ParseException {
        return tourney.getMatchList().get(0)
                .getDate().before(dateFromNowPlus3Days());
    }

    /**
     * Convert utils.Date to LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * Check is startTime between betting match and last match of this tourney in db, witch not already end, need to be more than 30 min
     */
    public static boolean isTimeBetweenMatchesMoreThan30Min(
            Date bettingMatchDate, LocalDateTime dbMatchDate) {
        Period period = Period.between(
                toLocalDateTime(bettingMatchDate).toLocalDate(), dbMatchDate.toLocalDate());
        Duration duration = Duration.between(
                toLocalDateTime(bettingMatchDate).toLocalTime(), dbMatchDate.toLocalTime());

        return  period.getMonths() > 0 || period.getDays() > 0 || Math.abs(duration.toMinutes()) > 30;
    }


    /**
     * Get Date after 3 days from now
     */
    private static Date dateFromNowPlus3Days() throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(
                LocalDate.now().plusDays(3L).toString());
    }

}
