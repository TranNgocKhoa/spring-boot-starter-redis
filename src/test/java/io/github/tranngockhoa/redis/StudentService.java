package io.github.tranngockhoa.redis;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Cacheable(value = "student", key = "#id")
    public Student getStudent(long id) {
        return Student.builder()
                .id(id)
                .person(new Person("John", Gender.MALE))
                .build();
    }

    @Cacheable(value = "student", key = "#id")
    public Student getStudentWithName(long id, String name) {
        return Student.builder()
                .id(id)
                .person(new Person(name, Gender.FEMALE))
                .build();
    }

    @CacheEvict(value = "student", key = "#id")
    public void updateStudent(long id) {

    }

}
