# 학습 내용 정리: Spring Boot와 Redis를 이용한 OTP 시스템 구현
학습목표 : 이 프로젝트를 통해 Spring Boot와 Redis를 이용한 실제 애플리케이션 개발 과정을 경험하고, 
OTP 시스템의 기본적인 구현 방법을 학습했습니다. 주요 코드를 통해 각 컴포넌트의 구현 방식과 Spring Boot의 주요 기능 습득

## 1. 프로젝트 구조
- 계층화된 아키텍처 사용 (Layered Architecture)
  - Controller
  - Service
  - Repository
  - Entity

## 2. 주요 컴포넌트와 코드

### EmailOtp 엔티티
```java
@RedisHash("otp")
@Getter
@Setter
public class EmailOtp {
    @Id
    private String id;
    @Indexed
    private String email;
    private String otp;
    private String refreshToken;
    @TimeToLive
    private Integer ttl;
}
```

### EmailOtpRepository
```java
@Repository
public interface EmailOtpRepository extends CrudRepository<EmailOtp, String> {
    EmailOtp findByRefreshToken(String refreshToken);
}
```

### EmailOtpService
```java
@Service
@RequiredArgsConstructor
public class EmailOtpService implements EmailOtpCreateUseCase, EmailOtpReadUseCase, EmailOtpDeleteUseCase {

    private final EmailOtpRepository repository;
    private final EmailOtpMapper mapper;

    @Override
    public EmailOtp create(EmailOtp emailOtp) {
        String refreshToken = SecureRandomUtil.generateSecureToken(32);
        emailOtp.setRefreshToken(refreshToken);
        return repository.save(emailOtp);
    }

    @Override
    public EmailOtp read(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public EmailOtp delete(String id) {
        EmailOtp emailOtp = repository.findById(id).orElse(null);
        if (emailOtp != null) {
            repository.deleteById(id);
            return mapper.copy(emailOtp);
        }
        return null;
    }

    public EmailOtp refreshToken(String refreshToken) {
        EmailOtp emailOtp = repository.findByRefreshToken(refreshToken);
        if (emailOtp != null) {
            String newRefreshToken = SecureRandomUtil.generateSecureToken(32);
            emailOtp.setRefreshToken(newRefreshToken);
            return repository.save(emailOtp);
        }
        return null;
    }
}
```

### EmailOtpController
```java
@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class EmailOtpController {

    private final EmailOtpService service;

    @GetMapping("/{id}")
    public EmailOtp getOtp(@PathVariable String id) {
        return service.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmailOtp createOtp(@RequestBody EmailOtp emailOtp) {
        return service.create(emailOtp);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOtp(@PathVariable String id) {
        service.delete(id);
    }

    @PostMapping("/refresh")
    public EmailOtp refreshToken(@RequestParam String refreshToken) {
        return service.refreshToken(refreshToken);
    }
}
```

## 3. 리프레시 토큰 구현
SecureRandomUtil 클래스를 이용한 안전한 랜덤 토큰 생성:

```java
public class SecureRandomUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Base64.Encoder BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();

    public static String generateSecureToken(int byteLength) {
        byte[] bytes = new byte[byteLength];
        RANDOM.nextBytes(bytes);
        return BASE64_ENCODER.encodeToString(bytes);
    }
}
```

## 4. Redis 활용
- @RedisHash 어노테이션을 통한 Redis 저장소 매핑
- @TimeToLive를 이용한 데이터 만료 시간 설정

## 5. RESTful API 설계
- POST /otp : OTP 생성
- GET /otp/{id} : OTP 조회
- DELETE /otp/{id} : OTP 삭제
- POST /otp/refresh : 리프레시 토큰을 이용한 새 OTP 발급

## 6. 보안 고려사항
- SecureRandom을 이용한 안전한 랜덤 값 생성
- 리프레시 토큰을 통한 OTP 갱신 메커니즘 구현

## 7. 테스트 및 디버깅
Postman을 이용한 API 테스트 예시:
```
POST http://localhost:8080/otp
Body:
{
    "email": "user@example.com",
    "otp": "123456",
    "ttl": 300
}
```

## 8. 주의사항 및 개선점
- ID 자동 생성 활용
- 리프레시 토큰 null 문제 해결을 위한 디버깅 방법
- 예외 처리 및 에러 핸들링 개선 필요

## 9. 사용된 Spring Boot 기능
- 의존성 주입 (Dependency Injection)
- Spring Data Redis
- RESTful API 구현
- 어노테이션 기반 설정


-------------------------------------------------------
# Update 학습 내용 요약

## 1. DTO (Data Transfer Object) 패턴 도입

DTO를 도입하여 계층 간 데이터 전송을 개선하고 엔티티 직접 노출을 방지했습니다.

```java
public final class EmailOtpDto {
    @Builder
    public record CreateRequest(String email, String otp, Integer ttl) {}

    @Builder
    public record Response(String id, String email, String otp, Integer ttl, String refreshToken) {}

    @Builder
    public record RefreshRequest(String refreshToken) {}
}
```

## 2. MapStruct를 이용한 매퍼 구현

MapStruct를 사용하여 DTO와 엔티티 간 변환을 효율적으로 처리하도록 했습니다.

```java
@Mapper(componentModel = "spring")
public interface EmailOtpMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    EmailOtp toEntity(EmailOtpDto.CreateRequest dto);

    EmailOtpDto.Response toDto(EmailOtp entity);
}
```

## 3. 컨트롤러 수정

컨트롤러에서 DTO를 사용하도록 변경하여 API 계층을 개선했습니다.

```java
@RestController
@RequestMapping("/otp")
public class EmailOtpController {
    @PostMapping
    public EmailOtpDto.Response createOtp(@RequestBody EmailOtpDto.CreateRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public EmailOtpDto.Response getOtp(@PathVariable String id) {
        return service.read(id);
    }
}
```

## 4. 서비스 계층 리팩토링

서비스 계층에서 DTO를 사용하고 UseCase 인터페이스를 구현하도록 변경했습니다.

```java
@Service
public class EmailOtpService implements EmailOtpCreateUseCase, EmailOtpReadUseCase, EmailOtpDeleteUseCase, EmailOtpRefreshUseCase {
    @Override
    public EmailOtpDto.Response create(EmailOtpDto.CreateRequest request) {
        EmailOtp emailOtp = mapper.toEntity(request);
        // ... (implementation)
        return mapper.toDto(savedEmailOtp);
    }

    @Override
    public EmailOtpDto.Response read(String idOrEmail) {
        EmailOtp emailOtp = repository.findById(idOrEmail)
                .orElseGet(() -> repository.findByEmail(idOrEmail));
        return mapper.toDto(emailOtp);
    }
}
```

## 5. UseCase 인터페이스 도입

각 기능에 대한 UseCase 인터페이스를 생성하여 관심사를 분리하고 코드의 가독성과 유지보수성을 향상시켰습니다.

```java
public interface EmailOtpCreateUseCase {
    EmailOtpDto.Response create(EmailOtpDto.CreateRequest request);
}

public interface EmailOtpReadUseCase {
    EmailOtpDto.Response read(String idOrEmail);
}

public interface EmailOtpDeleteUseCase {
    EmailOtpDto.Response delete(String idOrEmail);
}

public interface EmailOtpRefreshUseCase {
    EmailOtpDto.Response refreshToken(String refreshToken);
}
```

## 학습 요약
변경을 통해 프로젝트의 구조를 개선하고 클린 아키텍처 원칙 준수 
DTO 사용으로 계층 간 데이터 전송이 명확해짐 
UseCase 인터페이스 도입으로 비즈니스 로직의 의도가 분명하게 함
MapStruct를 이용한 매핑으로 코드 중복을 줄이고 효율성을 높임.

---------------------


# Redis 사용
Redis는 인메모리 데이터 저장소로서 빠른 접근 속도와 다양한 기능을 제공하며,
이 프로젝트에서는 주로 Valkey와 유사한 방식으로 활용되었습니다.

### 1. 인메모리 Key-Value 저장소
- `EmailOtp` 엔티티를 Redis에 저장합니다.
- `@RedisHash("otp")` 어노테이션은 Redis의 key-value 구조를 활용함을 나타냅니다.

### 2. 분산 캐시
- Redis는 OTP 정보를 빠르게 저장하고 조회하는 캐시로 사용됩니다.
- 이는 Valkey의 분산 캐시 기능과 유사합니다.

### 3. 옵션 지속성
- Redis는 인메모리 저장소이지만, 필요에 따라 데이터를 디스크에 저장할 수 있습니다.
- `RedisConfiguration`에서 이러한 옵션을 설정할 수 있습니다.

### 4. 메시지 브로커 기능
- 현재 코드에서는 직접적으로 사용되지 않지만, Redis의 pub/sub 기능을 통해 메시지 브로커로 사용 가능합니다.

### 5. TTL (Time To Live) 지원
- `@TimeToLive` 어노테이션을 통해 OTP의 만료 시간을 설정합니다.
  ```java
  @TimeToLive
  public Integer ttl;
  ```

### 6. 분산 환경 지원
- `@Indexed` 어노테이션을 사용하여 클러스터 환경에서의 효율적인 조회를 지원합니다.

### 7. 설정
- `RedisConfiguration` 클래스와 `redis.yml` 파일을 통해 Redis 연결을 구성합니다.
  ```java
  @Configuration
  public class RedisConfiguration {
      @Bean
      public RedisConnectionFactory redisConnectionFactory(){
          // Redis 연결 설정
      }
  }
  ```

## @Indexed 어노테이션 설명

`@Indexed` 어노테이션은 Spring Data에서 제공하는 기능으로, 특정 필드에 대한 인덱스를 생성하도록 지시합니다. Redis 환경에서 이 어노테이션은 특별한 의미를 가집니다.

### EmailOtp 엔티티에서의 사용 예

```java
@RedisHash("otp")
public class EmailOtp {
    @Id
    private String id;
    
    @Indexed
    public String email;
    
    // ... 다른 필드들
}
```

### @Indexed의 목적과 효과

1. **빠른 조회**: `@Indexed`가 적용된 필드는 Redis에서 별도의 인덱스로 관리되어, 해당 필드를 기준으로 한 조회 연산의 성능이 크게 향상됩니다.

2. **보조 인덱스**: Redis는 기본적으로 키-값 저장소이지만, `@Indexed`를 사용하면 보조 인덱스를 생성하여 다양한 필드로 빠른 검색이 가능합니다.

3. **클러스터 환경 지원**: Redis 클러스터에서 데이터는 여러 노드에 분산되는데, `@Indexed`를 사용하면 특정 필드로 전체 클러스터에서 효율적인 검색이 가능합니다.

4. **유연한 쿼리**: `id` 외의 필드로도 엔티티를 쉽게 조회할 수 있게 해줍니다. 예를 들어, `EmailOtp`를 `email`로 조회하는 메서드를 쉽게 구현할 수 있습니다.

### 사용 예시

```java
public interface EmailOtpRepository extends CrudRepository<EmailOtp, String> {
    EmailOtp findByEmail(String email);
}
```

위 레포지토리 메서드는 `@Indexed`로 인해 효율적으로 동작합니다. `email` 필드에 대한 인덱스가 있어 빠른 조회가 가능합니다.

### 주의사항

1. **인덱스 오버헤드**: 인덱스는 추가 저장 공간을 사용하며, 쓰기 작업 시 약간의 성능 저하가 있을 수 있습니다.
2. **선별적 사용**: 모든 필드에 `@Indexed`를 사용하는 것은 권장되지 않습니다. 자주 조회되는 필드에만 선별적으로 사용하세요.

`@Indexed` 어노테이션을 적절히 사용하면 Redis 기반의 애플리케이션에서 데이터 조회 성능을 크게 향상시킬 수 있습니다.

---

이 프로젝트는 Redis의 다양한 기능을 활용하여 OTP 관리 시스템을 구현한 예제입니다. Redis의 인메모리 저장소, 분산 캐시, TTL 지원 등 다양한 특징들을 통해 빠르고 효율적인 데이터 관리를 실현합니다.