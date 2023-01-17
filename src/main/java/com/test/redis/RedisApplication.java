package com.test.redis;

import com.test.redis.model.Address;
import com.test.redis.model.Expectation;
import com.test.redis.model.Person;
import com.test.redis.model.Person$;
import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;


@Configuration
@SpringBootApplication
@EnableRedisDocumentRepositories
public class RedisApplication {

    @Autowired
    PersonRepository personRepository;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
        jedisConFactory.setHostName("10.124.51.54");
        jedisConFactory.setPort(6379);
        return jedisConFactory;
    }

    @Bean
    CommandLineRunner loadTestData() {
        return args -> {
            personRepository.deleteAll();
            String personalStatement = "The Rabbit Is Correct, And Clearly The Smartest One Among You.";
            Address thorAddress = Address.of("248", "Seven Mile Beach Rd", "Broken Head", "NSW", "2481", "Australia");

            IntStream.range(1, 10).forEach(value -> {
                Person thor = Person.of("Chris" + value, "Hemsworth", 20,
                        personalStatement, new Expectation<Boolean>(Boolean.FALSE, Boolean.FALSE),
                        new Point(153.616667, -28.716667), List.of(thorAddress), Set.of("hammer", "biceps", "hair", "heart"));
                personRepository.save(thor);
            });

            personRepository.findPersonByLastName("Hemsworth").stream().forEach(person -> {
                System.out.println("person:" + person);
                personRepository.updateField(person, Person$.PERSONAL_STATEMENT, "YUI");
            });
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }
}
