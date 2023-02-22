package com.example.redisjsonindex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bruce.Yang
 * @date 2023/2/21 -23:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiveInfo {

    private String id;

    private Expectation<Boolean> expectation;

    private LiveCall liveCall;
}
