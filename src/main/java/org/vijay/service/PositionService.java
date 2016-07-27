package org.vijay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.vijay.domain.Position;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tiwar_000 on 26-07-2016.
 */
@Service
public class PositionService {
    private static final Logger logger = LoggerFactory.getLogger(PositionService.class);
    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.goeuro.url}")
    private String baseUrl;

    public List<Position> getSuggestedPositions(String cityName) {
        List<Position> positions = null;
        RequestEntity requestEntity = RequestEntity
                .get(URI.create(baseUrl + "/" + cityName))
                .build();
        try {
            ResponseEntity<Position[]> responseEntity = restTemplate.exchange(requestEntity, Position[].class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                if (responseEntity.getBody() != null) {
                    positions = Arrays.asList(responseEntity.getBody());
                }
            } else {
                throw new RuntimeException("Error from server. HTTP Status Code: " + responseEntity.getStatusCode().value());
            }
        } catch (ResourceAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return positions;
    }

    public void writeToCSV(List<Position> positions, String filePath) {
        File file = new File(filePath);
        try (FileWriter fWriter = new FileWriter(filePath);
             BufferedWriter writer = new BufferedWriter(fWriter)) {
            writer.write("_id,name,type,latitude,longitude");
            if (positions != null && positions.size() > 0) {
                positions.forEach(position -> {
                    try {
                        writer.newLine();
                        StringBuffer line = new StringBuffer();
                        line.append(position.get_id())
                                .append(",")
                                .append(position.getName())
                                .append(",")
                                .append(position.getType())
                                .append(",")
                                .append(position.getGeo_position().getLatitude())
                                .append(",")
                                .append(position.getGeo_position().getLongitude());
                        writer.write(line.toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        logger.info("File written at: " + file.getAbsolutePath());
    }
}
