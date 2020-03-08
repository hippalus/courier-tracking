package com.courier.eventprocessor.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Clients {
    private String hostname;
    private String scheme;
    private int httpPort;
    private int containerPort;
}
