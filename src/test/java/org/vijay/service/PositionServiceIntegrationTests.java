package org.vijay.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vijay.GoEuroTestApplication;
import org.vijay.domain.Position;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by tiwar_000 on 27-07-2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GoEuroTestApplication.class)
@IntegrationTest
public class PositionServiceIntegrationTests {
    @Autowired
    private PositionService positionService;

    @Test
    public final void testGetSuggestedPositionsValidName() {
        String cityName = "Berlin";
        List<Position> positionList = positionService.getSuggestedPositions(cityName);
        assertNotNull(positionList);
        assertTrue(positionList.size() > 0);
        positionList.forEach(position -> {
            assertTrue(position.getName().contains(cityName));
        });
    }

    @Test
    public final void testGetSuggestedPositionsInvalidName() {
        String cityName = "Berlin123123";
        List<Position> positionList = positionService.getSuggestedPositions(cityName);
        assertNotNull(positionList);
        assertEquals(0, positionList.size());
    }
}
