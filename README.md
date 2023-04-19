# Spring Boot Redis Starter

Spring Redis auto configuration module.

## Prerequisite
- JDK 11
- Redis

## Setup local environment

### Install Redis

```shell
brew install redis
```

### Setup Redis Sentinel

#### Node 1
`master.conf` file:

```
port 7001

requirepass "admin123"
enable-debug-command yes

logfile "~/redis-sentinel/log/master.log"
```

`sentinel-1.conf` file:
```
port 5001
sentinel auth-pass redis-master admin123
sentinel monitor redis-master 127.0.0.1 7003 2
sentinel down-after-milliseconds redis-master 4000
sentinel failover-timeout redis-master 2000

logfile "~/redis-sentinel/log/sentinel1.log"
```
#### Node 2
`slave-1.conf` file:

```
port 7002

masterauth "admin123"

replicaof 127.0.0.1 7001

logfile "~/redis-sentinel/loglog/slave1.log"
```

`sentinel-2.conf` file:
```
port 5002
sentinel auth-pass redis-master admin123
sentinel monitor redis-master 127.0.0.1 7002 2
sentinel down-after-milliseconds redis-master 4000
sentinel failover-timeout redis-master 2000

logfile "~/redis-sentinel/log/sentinel2.log"
```

#### Node 3
`slave-2.conf` file:

```
port 7003

masterauth "admin123"

replicaof 127.0.0.1 7001

logfile "~/redis-sentinel/loglog/slave2.log"
```

`sentinel-3.conf` file:
```
port 5003
sentinel auth-pass redis-master admin123
sentinel monitor redis-master 127.0.0.1 7003 2
sentinel down-after-milliseconds redis-master 4000
sentinel failover-timeout redis-master 2000

logfile "~/redis-sentinel/log/sentinel3.log"
```

#### Start sentinel

`start-sentinel.sh` file:

```shell
redis-server master.conf &
redis-server slave-1.conf &
redis-server slave-2.conf &

redis-server sentinel-1.conf --sentinel &
redis-server sentinel-2.conf --sentinel &
redis-server sentinel-3.conf --sentinel &
```

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
      exclude: io.github.tranngockhoa.redis.config.RedisCacheAutoConfiguration
```
