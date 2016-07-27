package org.vijay.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.vijay.domain.GeoPosition;
import org.vijay.domain.Position;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.stub;

/**
 * Created by tiwar_000 on 27-07-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class PositionServiceTests {
    @InjectMocks
    private PositionService positionService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    public final void testGetSuggestedPositionsStatusOKNoBody() {
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        stub(restTemplate.exchange(any(RequestEntity.class), any(Class.class))).toReturn(responseEntity);
        assertNull(positionService.getSuggestedPositions("test"));
    }

    @Test
    public final void testGetSuggestedPositionsStatusOK() {
        Position[] positions = new Position[1];
        ResponseEntity responseEntity = new ResponseEntity(positions, HttpStatus.OK);
        stub(restTemplate.exchange(any(RequestEntity.class), any(Class.class))).toReturn(responseEntity);
        assertNotNull(positionService.getSuggestedPositions("test"));
    }

    @Test(expected = RuntimeException.class)
    public final void testGetSuggestedPositionsStatusNotOK() {
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
        stub(restTemplate.exchange(any(RequestEntity.class), any(Class.class))).toReturn(responseEntity);
        assertNull(positionService.getSuggestedPositions("test"));
    }

    @Test
    public final void testWriteToCSV() throws IOException {
        Position position = new Position();
        position.set_id(1L);
        position.setName("test_name");
        position.setType("test_type");
        GeoPosition geoPosition = new GeoPosition();
        geoPosition.setLatitude(1d);
        geoPosition.setLongitude(1d);
        position.setGeo_position(geoPosition);
        List<Position> positions = new ArrayList<>();
        positions.add(position);
        positionService.writeToCSV(positions, "output.csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("output.csv")))) {
            assertNotNull(reader);
            assertEquals("_id,name,type,latitude,longitude", reader.readLine());
            assertEquals("1,test_name,test_type,1.0,1.0", reader.readLine());
            assertNull(reader.readLine());
        } finally {

        }

    }
}
