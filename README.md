# Spring Boot Redis Starter

## Prerequisite
- JDK 11
- Redis

## Setup local environment

## Quick start

### Setup 

Add dependency in `build.gradle`'s dependencies section:

```groovy
dependencies {
    // other dependencies
    implementation 'io.github.tranngockhoa:spring-boot-starter-redis:0.0.1'
}
```

or in `pom.xml`

```xml
<dependencies>
    <!-- other dependencies -->
        <dependency>
            <groupId>io.github.tranngockhoa</groupId>
            <artifactId>spring-boot-starter-redis</artifactId>
            <version>0.0.1</version>
        </dependency>
</dependencies>
```

Add following configuration in `application.yml`

```yaml
spring:
  cache:
    redis:
      time-to-live: 300000
      runtime-ignore-error: false
  redis:
    lettuce:
      pool:
        max-active: 5
        min-idle: 5
        max-wait: 1000
        max-idle: 10
    sentinel:
      master: redis-master
      password: admin123
      nodes:
        - 127.0.0.1:5001
        - 127.0.0.1:5002
        - 127.0.0.1:5003
    database: 14
```

`runtime-ignore-error` is for specifying whether avoid error when Redis is down or exception happen at runtime.

### In Java code:

Add cache

```java
    @Cacheable(value = "student", key = "#id")
    public Student getStudentWithName(long id, String name) {
        return Student.builder()
                .id(id)
                .person(new Person(name, Gender.FEMALE))
                .build();
    }
```

Remove cache

```java
    @CacheEvict(value = "student", key = "#id")
    public void updateStudent(long id) {

    }
```


## Disable module config

Remove dependency then rebuild.

Or add following configuration in `application.yml`

```yaml
spring:
    autoconfigure:
      exclude: RedisCacheAutoConfiguration
```
