package io.github.tranngockhoa.redis;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Cacheable(value = "person", key = "#name")
    public Person getPerson(String name, Gender gender) {
        Person person = new Person();
        person.setName(name);
        person.setGender(gender);

        return person;
    }

    @CacheEvict(value = "person", key = "#name")
    public void updatePerson(String name) {

    }
}
