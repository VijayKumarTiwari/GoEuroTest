package org.vijay.domain;

import lombok.Data;

/**
 * Created by tiwar_000 on 26-07-2016.
 */
@Data
public class Position {
    private Long _id;
    private String name;
    private String type;
    private GeoPosition geo_position;
}
