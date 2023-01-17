package com.test.redis;

import com.test.redis.model.Person;
import com.redis.om.spring.repository.RedisDocumentRepository;

import java.util.List;

/**
 * @author Bruce.Yang
 * @date 2023/1/16 -14:48
 */
public interface PersonRepository extends RedisDocumentRepository<Person, String> {

    List<Person> findByAddresses_City(String city);

    List<Person> findPersonByLastName(String lastName);
}
