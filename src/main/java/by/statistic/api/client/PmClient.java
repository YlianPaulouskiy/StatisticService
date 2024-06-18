package by.statistic.api.client;

import by.pm.model.Cp;
import by.pm.model.Match;
import by.pm.model.Sport;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/pari-match")
public interface PmClient {

    @GetExchange("/events/live")
    Sport getLiveMatches();

    @GetExchange("/events/future")
    Sport getFutureMatches();

    @GetExchange("/matches/{id}")
    Match getMatchById(@PathVariable("id") Long matchId);

    @PostExchange("/place-bet")
    Cp placeBet(@RequestParam Long eventId, @RequestParam Long stakeId,
                @RequestParam Double cash);

}
