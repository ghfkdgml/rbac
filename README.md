# Spring Boot RBAC + Plan-limited Project CRUD

## 요구사항 반영 요약

- RBAC(역할 A/B/C/D) × Action(CREATE/READ/UPDATE/DELETE) 권한 매트릭스 코드로 고정
- 요금제(BASIC/PRO)별 프로젝트 생성 상한: BASIC=1, PRO=5
- 중복 생성 금지: (ownerId, title) 유니크
- 상세조회는 "진행 중 태스크 개수"(status=IN_PROGRESS) 포함

## 빠른 실행

```bash
./gradlew bootRun
# Swagger UI: http://localhost:8080/swagger-ui/index.html
```

## 인증/인가 (데모)

- 헤더 기반

```
X-User-Id: u1
X-Role: A|B|C|D
X-Plan: BASIC|PRO
```

## API

- POST /projects (A/B/C/D 모두, 플랜 한도 검사)
- GET /projects/{id} (A, D만)
- PATCH /projects/{id} (A, B만)
- DELETE /projects/{id} (A, B, C)

### 요청/응답 예시는 Swagger에서 확인

## 성능 테스트

성능 테스트는 k6로 실행됩니다.  
k6 설치(필요 시):

- macOS: `brew install k6`
- Windows: https://grafana.com/docs/k6/latest/set-up/install-k6/
- Linux: https://grafana.com/docs/k6/latest/set-up/install-k6/

### 실행 방법

```bash
./gradlew bootRun(서버실행 필요)

k6 run -e ROLE=A -e PLAN=PRO -e BASE=http://localhost:8080 scripts/perf/k6-create-read.js
```
