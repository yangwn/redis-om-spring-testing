package com.test.redis.model;

import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

/**
 * @author Bruce.Yang
 * @date 2022/12/11 -19:13
 */
@Data
@RequiredArgsConstructor(staticName = "of")
public class Address {

    @Id
    @Indexed
    private String id = UUID.randomUUID().toString();

    @NonNull
    @Indexed
    private String houseNumber;

    @NonNull
    @Searchable(nostem = true)
    private String street;

    @NonNull
    @Indexed
    private String city;

    @NonNull
    @Indexed
    private String state;

    @NonNull
    @Indexed
    private String postalCode;

    @NonNull
    @Indexed
    private String country;
}
