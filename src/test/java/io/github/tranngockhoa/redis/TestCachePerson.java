package io.github.tranngockhoa.redis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModuleRedisApplicationTests.class)
public class TestCachePerson {
    @Autowired
    PersonService personService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    @AfterEach
    void setup() {
        cacheManager.getCacheNames()
                .forEach(n -> Objects.requireNonNull(cacheManager.getCache(n)).clear());
    }

    @Test
    void personCacheable() {
        personService.getPerson("John", Gender.MALE);

        Person john = personService.getPerson("John", Gender.FEMALE);
        Assertions.assertEquals(Gender.MALE, john.getGender());
    }

    @Test
    void personCacheEvict() {
        personService.getPerson("Alice", Gender.MALE);

        Person alice = personService.getPerson("Alice", Gender.FEMALE);
        Assertions.assertEquals(Gender.MALE, alice.getGender());

        personService.updatePerson("Alice");
        Person aliceUpdated = personService.getPerson("Alice", Gender.FEMALE);
        Assertions.assertEquals(Gender.FEMALE, aliceUpdated.getGender());
    }
}
