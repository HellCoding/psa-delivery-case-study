# 06. QA/UAT 계획

## QA 전략

QA/UAT의 목적은 구현 결과가 비즈니스 요구사항을 충족하는지 확인하고, 어떤 산출물을 baseline으로 채택할 수 있는지 판단할 근거를 만드는 것입니다.

이 프로젝트에서는 브라우저 기반 시나리오 검증, OutSystems Studio/ODC 구조 확인, Jira QA 댓글, 최종 비교표를 함께 사용했습니다.

## 검증 원칙

| 원칙 | 설명 |
|---|---|
| 요구사항 기준 검증 | 화면이 동작하는지만 보지 않고 PRD의 FR/BR 기준으로 판단 |
| 상태 전이 우선 | Draft, Submitted, Approved, Rejected 상태별 편집 가능성을 집중 확인 |
| 데이터 정합성 확인 | WeeklyTimesheet와 TimeEntry 상태/합계/비용 동기화 확인 |
| 역할별 접근 확인 | Employee, PM, Executive, Admin, Pending User의 접근 범위 확인 |
| 재검증 기록 | 수정 완료 댓글만 믿지 않고 동일 시나리오로 다시 확인 |

## 시나리오 작성 형식

각 시나리오는 아래 정보를 포함합니다.

- 시나리오 ID
- 관련 FR/BR ID
- 사용자 유형
- 사전 조건
- 실행 단계
- 기대 결과
- 실제 결과
- 우선순위
- 관련 Jira issue
- 실패 시 결함 ID
- 재검증 결과

## 검증 범위

| 영역 | 예시 시나리오 | 주요 리스크 |
|---|---|---|
| BizOpty | 영업 기회 생성/수정, Deal Status 변경 | 수익률 계산 조건 누락 |
| Project | BizOpty 기반 프로젝트 생성 | 고객명 중복 저장, 필수값 우회 |
| Task | 프로젝트 태스크 등록/정렬/비활성화 | 중복 또는 비활성 task 선택 가능 |
| Time Entry | 일일/주간 투입 입력 | 24시간 초과, 미래일 입력, 단가 snapshot 누락 |
| Submit | 주간 timesheet 제출 | 제출 후에도 수정 가능 |
| Approve/Reject | PM 승인/반려 | 반려 사유 누락, 상태 동기화 실패 |
| Dashboard | 기간 필터와 KPI 확인 | 필터가 KPI 전체에 적용되지 않음 |
| Report Builder | 필드/필터/그룹 실행 | 빈 결과 오류, allow-list 누락, SQL risk |
| Permission | 역할별 화면 접근 | 모든 사용자에게 화면 노출 |

## 대표 결함 패턴

| 결함 | 심각도 | 근거 | 기대 조치 |
|---|---|---|---|
| 제출 완료 후 Add/Edit/Delete가 계속 활성화 | High | BR-001 | Draft/Rejected에서만 편집 허용 |
| RejectReason 없이 반려 가능 | High | BR-002 | 반려 팝업과 필수 검증 추가 |
| WeeklyTimesheet와 TimeEntry 상태 불일치 | High | BR-003 | 같은 트랜잭션에서 상태 동기화 |
| Report Viewer가 빈 결과에서 오류 | High | FR-013 | empty state와 예외 처리 추가 |
| Project 목록 중복 row 표시 | Medium | 데이터 모델 관계 | join 조건과 distinct 처리 보강 |
| 대시보드 기간 필터 일부 KPI 미적용 | Medium | BR-006 | 모든 aggregate에 동일 기간 조건 적용 |

## 종료 기준

- 우선순위 1 시나리오는 모두 통과하거나 명시적으로 defer 결정한다.
- Critical 결함은 0이어야 한다.
- High 결함은 해결하거나 production 전 조치 권고와 후속 backlog를 남긴다.
- 미해결 Medium/Low 결함은 최종 baseline 결정의 잔여 리스크에 포함한다.
- 최종 비교 문서에는 기능 완성도뿐 아니라 재검증 여부를 함께 적는다.

샘플 시나리오는 [`samples/sample-uat-scenarios.md`](../samples/sample-uat-scenarios.md), 샘플 결함 목록은 [`samples/sample-qa-findings.md`](../samples/sample-qa-findings.md)를 참고합니다.
