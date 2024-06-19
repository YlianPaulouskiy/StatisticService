package by.statistic;

import by.statistic.api.client.PmClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatisticServiceApp {

    public static void main(String[] args) {
        try (var context = SpringApplication.run(StatisticServiceApp.class, args)){
            var pmClient = context.getBean(PmClient.class);
            System.out.println(pmClient.getLiveMatches());
        }
    }

}
