package com.courier.eventprocessor.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Index {
    private String name;
    private int shard;
    private int replica;
    private int from;
    private int size;
    private int timeout;
}
