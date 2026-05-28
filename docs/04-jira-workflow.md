# 04. Jira 업무 흐름

## 목적

Jira는 오프쇼어 팀에게 일을 배정하는 도구가 아니라, PRD 요구사항이 구현, QA, 재검증, 최종 baseline 의사결정까지 이어졌는지 확인하는 추적성 도구로 운영했습니다.

이 케이스 스터디에서는 실제 티켓 원문을 사용하지 않고, 공개 가능한 형태로 Phase Epic, 기능 Task, QA 댓글, 결함 재검증 흐름을 재구성했습니다.

## Issue 계층

| 계층 | 역할 | 예시 |
|---|---|---|
| Phase Epic | 일정과 범위의 큰 단위 | Foundation, Core Data, Workflow/Dashboard, Report Builder |
| Feature Task | 구현 가능한 기능 단위 | Weekly Timesheet, Submit Workflow, Approve/Reject, PM Dashboard |
| QA Finding | 검증 중 발견한 결함 | 제출 후 그리드 수정 가능, 반려 사유 없이 Reject 가능 |
| Follow-up Backlog | baseline 선정 후 후속 처리 | 리포트 성능 개선, 권한 보강, 상태값 정규화 |

## Phase 구성

| Phase | 목표 | 대표 작업 | 완료 기준 |
|---|---|---|---|
| 1. Foundation | ODC 앱 구조와 기본 보안 준비 | 앱/모듈 생성, 역할 정의, 공통 레이아웃 | 로그인 후 역할별 기본 화면 접근 |
| 2. Core Data | PSA 핵심 데이터 모델 구현 | BizOpty, Project, Task, ProjectMember, Employee | CRUD와 필수 검증 통과 |
| 3. Timesheet Workflow | 일일/주간 시간 입력과 제출 | Daily Entry, Weekly Grid, Submit Workflow | Draft/Submitted 상태 전이와 수정 제한 |
| 4. Approval & Dashboard | 승인/반려와 PM 관점 조회 | Approve/Reject, PM Dashboard, period filter | RejectReason 필수, KPI 재조회 |
| 5. Admin & Executive | 사용자 승인과 경영 지표 | Employee Approval, Executive Dashboard | Pending user 승인, 경영 KPI 확인 |
| 6. Report Builder | 동적 리포트 생성/실행 | Report Home, Builder, Execute, Viewer | allow-list 기반 필드/필터 실행 |

## Task 작성 기준

기능 Task에는 구현 설명보다 검증 가능한 인수 기준을 먼저 적었습니다.

| 항목 | 작성 방식 |
|---|---|
| Requirement 링크 | PRD의 FR/BR ID를 연결 |
| Acceptance Criteria | Given/When/Then 또는 체크리스트로 작성 |
| Data Rule | 상태 전이, 필수값, 중복 방지, 계산식 명시 |
| UI Rule | 버튼 노출 조건, 편집 가능 상태, 오류 메시지 명시 |
| QA Scenario | UAT ID와 예상 결과 연결 |
| Done 조건 | QA 재검증 또는 후속 backlog 생성까지 포함 |

## 대표 Task 예시

### Submit Workflow

| 구분 | 내용 |
|---|---|
| 관련 요구사항 | FR-007, FR-008, BR-001, BR-003 |
| 인수 기준 | Draft 주차를 Submit하면 WeeklyTimesheet와 관련 TimeEntry가 Submitted로 전환된다 |
| 예외 | 빈 주차도 TotalHours 0으로 제출 가능해야 한다 |
| UI 규칙 | Submitted/Approved 주차에서는 Add/Edit/Delete와 Submit 버튼을 숨기거나 비활성화한다 |
| QA 포인트 | 제출 후 Daily Entry와 Weekly Grid 양쪽에서 수정이 차단되는지 확인 |

### Approve/Reject Workflow

| 구분 | 내용 |
|---|---|
| 관련 요구사항 | FR-009, BR-002, BR-003 |
| 인수 기준 | PM은 본인 프로젝트 팀원의 제출 주차를 승인 또는 반려할 수 있다 |
| 반려 규칙 | RejectReason은 필수이며 저장 후 제출자가 확인할 수 있어야 한다 |
| 상태 동기화 | WeeklyTimesheet 상태와 관련 TimeEntry 상태가 함께 변경된다 |
| QA 포인트 | 반려 사유 없이 Reject가 저장되지 않는지 확인 |

### Dynamic Report Execution

| 구분 | 내용 |
|---|---|
| 관련 요구사항 | FR-013 |
| 인수 기준 | 선택한 report type, column, filter, grouping 기준으로 결과가 표시된다 |
| 보안 규칙 | 필드와 필터는 allow-list로 제한하고 parameter binding을 사용한다 |
| QA 포인트 | 빈 결과, 잘못된 필터, 날짜 범위, 미리보기/실행 화면 전환을 확인 |

## 권장 상태 흐름

```text
Backlog -> Ready -> In Progress -> Review -> QA/UAT -> Done
                         |                         ^
                         v                         |
                      Blocked ----------------------
```

PoC에서는 팀별 Jira 설정이 다를 수 있으므로 상태명보다 전이 조건을 더 중요하게 봤습니다. 핵심은 "구현 완료"와 "QA 재검증 완료"를 분리해서 보는 것입니다.

## QA 댓글 운영 방식

QA 댓글은 단순히 "안 됩니다"라고 남기지 않고, 개발자가 바로 재현하고 수정할 수 있게 작성했습니다.

| 순서 | 포함 내용 |
|---|---|
| 1 | 결함 ID와 심각도 |
| 2 | 관련 PRD/BR/AC |
| 3 | 재현 단계 |
| 4 | 실제 결과와 기대 결과 |
| 5 | 원인 추정 또는 확인 위치 |
| 6 | 권고 조치 |
| 7 | 재검증 결과 |

## Definition of Ready

- 비즈니스 목적이 명확하다.
- 인수 기준이 테스트 가능하다.
- 의존성과 제약사항이 식별되어 있다.
- 관련 PRD, 화면, 데이터 규칙, 정책 문서가 연결되어 있다.
- 담당자와 리뷰어가 지정되어 있다.

## Definition of Done

- 인수 기준을 충족했다.
- QA/UAT 시나리오를 통과했거나 결함으로 기록했다.
- 품질 게이트에서 Critical/High 리스크를 확인했다.
- 관련 Confluence 문서 또는 결정 로그를 갱신했다.
- 미해결 항목은 후속 backlog로 등록했다.

## 운영 메트릭

| 지표 | 확인 의도 |
|---|---|
| Phase별 완료율 | 일정 예측성과 scope burn-down 확인 |
| QA finding 해결률 | 결함 대응 속도와 재검증 가능성 확인 |
| PRD-Task-UAT 연결률 | 요구사항 누락 여부 확인 |
| 댓글 품질 | 질문/응답이 의사결정 근거로 남는지 확인 |
| 재오픈 비율 | 수정 품질과 회귀 위험 확인 |

## 공개 저장소 주의사항

실제 Jira key, 담당자명, 내부 URL, 댓글 원문, 화면 캡처는 공개하지 않습니다. 이 문서의 티켓 구조와 예시는 실제 운영 방식을 바탕으로 새로 작성한 clean-room 버전입니다.
