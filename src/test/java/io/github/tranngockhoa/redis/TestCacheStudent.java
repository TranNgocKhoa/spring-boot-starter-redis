package io.github.tranngockhoa.redis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.Objects;

@SpringBootTest(classes = ModuleRedisApplicationTests.class)
public class TestCacheStudent {
    @Autowired
    StudentService studentService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    @AfterEach
    void setup() {
        cacheManager.getCacheNames()
                .forEach(n -> Objects.requireNonNull(cacheManager.getCache(n)).clear());
    }

    @Test
    void studentCacheable() {
        studentService.getStudent(1);

        Student john = studentService.getStudent(1);
        Assertions.assertEquals(Gender.MALE, john.getPerson().getGender());

        Student studentWithName = studentService.getStudentWithName(1, "XXXXXX");
        Assertions.assertEquals(john.getId(), studentWithName.getId());
        Assertions.assertEquals(john.getPerson().getGender(), studentWithName.getPerson().getGender());
        Assertions.assertEquals(john.getPerson().getName(), studentWithName.getPerson().getName());
    }

    @Test
    void studentCacheEvict() {
        studentService.getStudent(1);

        Student john = studentService.getStudent(1);
        Assertions.assertEquals(Gender.MALE, john.getPerson().getGender());

        studentService.updateStudent(1);

        Student studentWithName = studentService.getStudentWithName(1, "XXXXXX");
        Assertions.assertEquals(john.getId(), studentWithName.getId());
        Assertions.assertEquals(studentWithName.getPerson().getName(), "XXXXXX");
    }
}
