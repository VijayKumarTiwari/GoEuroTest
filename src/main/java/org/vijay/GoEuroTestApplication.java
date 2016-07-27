package org.vijay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.vijay.service.PositionService;

@SpringBootApplication
public class GoEuroTestApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(GoEuroTestApplication.class);

    @Autowired
    private PositionService positionService;
    @Value("${output.file.path.and.name}")
    private String outputFilePath;

    public static void main(String[] args) {
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
        SpringApplication.run(GoEuroTestApplication.class, args);
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void run(String... args) throws Exception {
        String cityName = getCityNameFromArgs(args);
        logger.info("City Name read from command line: " + cityName);
        if (cityName != null) {
            try {
                positionService.writeToCSV(positionService.getSuggestedPositions(cityName), outputFilePath);
                logger.info("Process completed Bye...");
            } catch (RuntimeException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private String getCityNameFromArgs(String... args) {
        if (args == null || args.length == 0 || args[0].trim().equals("")) {
            logger.error("Please pass the City Name");
            return null;
        } else {
            //spring will add its argument to the end that's why we take [0] as the cityName and not [1]
            return args[0];
        }
    }
}
