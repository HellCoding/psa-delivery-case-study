# 03. 도메인 모델

## 핵심 엔티티

| 엔티티 | 설명 | 주요 필드 |
|---|---|---|
| BizOpty | 영업 기회/사업 단위 | 이름, 고객명, Deal Type, Deal Status, 예상/계약 금액, 영업 담당자, PM |
| Employee | 직원 및 승인 상태 | UserId, 이름, 이메일, 부서, 직급, 시간당 단가, 승인 상태, 승인자, 반려 사유 |
| Project | 수행 프로젝트 | BizOptyId, 코드, 이름, 기간, 예산, 예상 시간, 상태, PM |
| Task | 프로젝트 하위 업무 | ProjectId, 이름, 설명, 예상 시간, 정렬 순서, 활성 여부 |
| ProjectMember | 프로젝트 팀원 배정 | ProjectId, EmployeeId, 역할, 투입 시작/종료일, 투입률 |
| TimeEntry | 일자/프로젝트/태스크 단위 투입 기록 | EmployeeId, ProjectId, TaskId, 날짜, 시간, 단가 snapshot, 원가, 상태 |
| WeeklyTimesheet | 주간 제출 단위 | EmployeeId, 주 시작일, 총 시간, 상태, 제출/승인 일시, 반려 사유 |
| ReportDefinition | 리포트 정의 | 이름, 유형, 공개 여부, 폴더, 소유자 |
| ReportColumn | 리포트 컬럼 | 필드명, 라벨, 표시 순서, 표시 여부, 그룹화 여부 |
| ReportFilter | 리포트 필터 | 필드명, 연산자, 필터값, 날짜 범위 |
| ReportGrouping | 리포트 그룹화 | 필드명, 그룹 레벨, 정렬, 부분합 여부 |

## Static Entity 예시

| 분류 | 예시 |
|---|---|
| Deal | DealType, DealStatus |
| 조직 | Department, Position |
| 프로젝트 | ProjectStatus, ProjectRole |
| 시간 | EntryStatus, TimesheetStatus |
| 리포트 | ReportType, FieldType, FilterOperator, DateRange |

## 계산 필드와 업무 규칙

| 항목 | 계산/규칙 |
|---|---|
| Project Cost | `SUM(TimeEntry.CostAmount)` |
| CostAmount | `TimeEntry.Hours * TimeEntry.HourlyRateSnapshot` |
| ProfitAmount | `BizOpty.ContractAmount - ProjectCost` |
| ProfitRate | `ProfitAmount / ContractAmount * 100` |
| ProgressRate | `SUM(TimeEntry.Hours) / SUM(Task.EstimatedHours)` |
| Utilization | `기간 내 투입 시간 / (근무일 수 * 8)` |
| Cost Snapshot | TimeEntry 생성 시 직원 단가를 복사하고 이후 단가 변경과 분리 |
| Unique Assignment | 동일 프로젝트에 동일 직원 중복 배정 금지 |

## 상태 모델

```text
BizOpty: Lead → Qualified → Proposal → Closed/Lost
Project: Planned → Active → On Hold → Completed/Cancelled
TimeEntry: Draft → Submitted → Approved
WeeklyTimesheet: Draft → Submitted → Approved/Rejected
EmployeeApproval: Pending → Approved/Rejected
```

## 관계 요약

- BizOpty는 여러 Project를 가질 수 있다.
- Project는 여러 Task와 ProjectMember를 가진다.
- Employee는 ProjectMember로 프로젝트에 배정되고 TimeEntry를 작성한다.
- WeeklyTimesheet는 특정 직원과 주차를 기준으로 TimeEntry를 묶는다.
- WeeklyTimesheet 승인/반려 결과는 관련 TimeEntry 상태와 동기화된다.
- ReportDefinition은 ReportColumn, ReportFilter, ReportGrouping을 조합해 실행된다.

다이어그램은 [`diagrams/domain-model.mmd`](../diagrams/domain-model.mmd)를 참고합니다.
