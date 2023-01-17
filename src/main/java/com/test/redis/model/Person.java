package com.test.redis.model;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

import java.util.List;
import java.util.Set;


/**
 * @author Bruce.Yang
 * @date 2022/12/11 -19:12
 */

@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Document(value = "cmma::person", timeToLive = 60000)
public class Person {

    @Id
    @Indexed
    private String id;

    @Indexed
    @NonNull
    private String firstName;

    @Indexed
    @NonNull
    private String lastName;

    @NonNull
    private Integer age;

    @Searchable
    @NonNull
    private String personalStatement;

    @NonNull
    private Expectation<Boolean> isAdmin;

    @Indexed
    @NonNull
    private Point homeLoc;

    @Indexed
    @NonNull
    private List<Address> addresses;

    @Indexed
    @NonNull
    private Set<String> skills;
}
