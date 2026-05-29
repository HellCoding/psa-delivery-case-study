# PSA Spring Boot API

PSA 도메인을 Spring Boot API로 clean-room 구현한 백엔드 포트폴리오 모듈입니다. 회사 코드, 내부 데이터, 실제 Jira/Confluence export를 사용하지 않고 공개용 PRD의 업무 규칙을 서버 사이드 도메인 로직으로 다시 작성했습니다.

## 구현 범위

| 영역 | 구현 내용 |
|---|---|
| Employee | 승인된 사용자와 시간당 단가 관리 |
| BizOpty/Project | 영업 기회 기반 프로젝트 생성 |
| ProjectMember/Task | 팀원 배정, 태스크 생성, 중복 배정 방지 |
| TimeEntry | 0.5시간 단위 입력, 일 24시간 제한, 단가 snapshot, 원가 계산 |
| Timesheet | Submit, Approve, Reject 상태 전이 |
| Quality Rule | Submitted/Approved 주차 수정 차단, RejectReason 필수 |
| Dashboard | 프로젝트 기간별 시간, 원가, 수익률, 진척률 집계 |

## 실행

macOS/Linux:

```bash
./mvnw test
./mvnw spring-boot:run
```

Windows PowerShell:

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

앱 실행 후 H2 Console은 `http://localhost:8080/h2-console`에서 확인할 수 있습니다.

## 주요 엔드포인트

| Method | Path | 설명 |
|---|---|---|
| POST | `/api/employees` | 직원 생성 |
| POST | `/api/biz-opties` | 영업 기회 생성 |
| POST | `/api/projects` | 프로젝트 생성 |
| POST | `/api/projects/{projectId}/members` | 프로젝트 팀원 배정 |
| POST | `/api/projects/{projectId}/tasks` | 프로젝트 태스크 생성 |
| POST | `/api/time-entries` | 투입 시간 입력 |
| PATCH | `/api/time-entries/{entryId}` | 투입 시간 수정 |
| POST | `/api/timesheets/submit` | 주간 timesheet 제출 |
| POST | `/api/timesheets/{timesheetId}/approve` | 주간 timesheet 승인 |
| POST | `/api/timesheets/{timesheetId}/reject` | 주간 timesheet 반려 |
| GET | `/api/dashboard/projects/{projectId}/summary` | 프로젝트 집계 조회 |

## 샘플 요청

```http
POST /api/time-entries
Content-Type: application/json

{
  "employeeId": 2,
  "projectId": 1,
  "taskId": 1,
  "entryDate": "2026-05-25",
  "hours": 8.0
}
```

```http
POST /api/timesheets/submit
Content-Type: application/json

{
  "employeeId": 2,
  "weekStartDate": "2026-05-25"
}
```

```http
POST /api/timesheets/1/reject
Content-Type: application/json

{
  "reviewerId": 1,
  "rejectReason": "Project task selection needs correction."
}
```
