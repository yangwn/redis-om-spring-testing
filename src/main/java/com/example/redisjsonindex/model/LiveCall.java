package com.example.redisjsonindex.model;

import lombok.*;

/**
 * @author Bruce.Yang
 * @date 2023/2/7 -14:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiveCall {

    private String id;

    private String cmsId;

    private String callId;

    private String conferenceId;

    private long ttl;

    private long poolingOrder;
}
