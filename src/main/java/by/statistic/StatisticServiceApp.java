package by.statistic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatisticServiceApp {

    public static void main(String[] args) {
        var context = SpringApplication.run(StatisticServiceApp.class, args);

        context.close();
    }

}
