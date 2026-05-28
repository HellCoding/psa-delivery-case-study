# 02. 공개용 PRD

## 문제 정의

서비스 딜리버리 조직은 영업 기회, 프로젝트, 태스크, 투입 시간, 승인, 비용/수익성 리포트를 여러 도구에서 분산 관리하는 경우가 많습니다. 이로 인해 프로젝트별 원가, 사업별 수익률, 팀원 가동률, 승인 누락 여부를 확인하기 위해 반복적인 수작업이 발생합니다.

## 제품 목표

1. 영업 기회에서 프로젝트 수행, 투입 시간, 승인, 리포트까지 이어지는 흐름을 표준화한다.
2. 주간 timesheet 제출/승인/반려 상태 전이를 명확히 한다.
3. 투입 당시 단가를 snapshot으로 저장하여 과거 원가 데이터를 보존한다.
4. PM/Executive 대시보드로 프로젝트 원가, 진척률, 가동률을 확인한다.
5. Report Builder로 자주 쓰는 PSA 리포트를 조건별로 실행한다.
6. PoC baseline을 기준으로 후속 개선 backlog와 운영 전 리스크를 도출한다.

## 제외 범위

- ERP 전체 기능 대체
- 급여 정산 처리
- 실시간 회계 처리
- 외부 고객용 청구 포털 구현
- 실제 계약/상업 데이터 관리
- 휴가/근태 시스템 대체

## 주요 사용자

| 사용자          | 필요 사항                                                 |
| --------------- | --------------------------------------------------------- |
| Employee        | 본인 프로젝트/태스크별 일일/주간 투입 시간 입력           |
| Project Manager | 프로젝트 팀원의 timesheet 조회, 승인/반려, 투입 현황 확인 |
| Sales Manager   | 영업 기회와 Deal Status 관리                              |
| Executive       | 사업별 수익률, 프로젝트 원가, 가동률, 경영 지표 확인      |
| Admin           | 사용자 승인, 역할 부여, 기준 데이터 관리                  |
| Pending User    | 최초 로그인 후 승인 전 안내 화면만 확인                   |

## 핵심 요구사항

| ID     | 영역                | 요구사항                 | 인수 기준                                                                                   |
| ------ | ------------------- | ------------------------ | ------------------------------------------------------------------------------------------- |
| FR-001 | BizOpty             | 영업 기회 생성/조회/수정 | Deal Status, 계약금액, 영업/PM 담당자를 관리할 수 있다                                      |
| FR-002 | Project             | 프로젝트 생성/수정       | BizOpty 연결 시 고객명은 원본에서 표시하고 중복 저장하지 않는다                             |
| FR-003 | Task                | 프로젝트 태스크 관리     | 태스크명, 예상 시간, 정렬 순서, 활성 여부를 관리한다                                        |
| FR-004 | Project Member      | 팀원 배정                | 동일 직원이 같은 프로젝트에 중복 배정되지 않는다                                            |
| FR-005 | Time Entry          | 일일 시간 입력           | 0.5시간 단위 입력, 미래 날짜 제한, 일일 합계 24시간 제한을 검증한다                         |
| FR-006 | Weekly Timesheet    | 주간 그리드 입력         | 월-금 그리드에서 프로젝트/태스크별 시간을 입력하고 합계를 확인한다                          |
| FR-007 | Submit Workflow     | 주간 제출                | Draft에서 Submitted로 전환되고 관련 Time Entry 상태가 일괄 변경된다                         |
| FR-008 | Empty Week Submit   | 빈 주 제출               | Time Entry가 없는 주도 TotalHours 0으로 제출해 월말 마감 상태를 명확히 한다                 |
| FR-009 | Approval Workflow   | 승인/반려                | 반려 시 사유 입력이 필수이며, 승인/반려 결과가 관련 Time Entry와 동기화된다                 |
| FR-010 | Cost Snapshot       | 원가 보존                | Time Entry 생성 시 직원 단가를 snapshot으로 저장하고, 단가 변경 후에도 과거 원가를 보존한다 |
| FR-011 | PM Dashboard        | 프로젝트 대시보드        | 투입 시간, 투입 원가, 진척률, 팀원별 가동률, 주간 추이를 조회한다                           |
| FR-012 | Executive Dashboard | 경영 대시보드            | 전체 인력, 가동률, 프로젝트별 비용, 예산 초과 항목을 확인한다                               |
| FR-013 | Report Builder      | 동적 리포트              | 사전 정의된 리포트 유형, 필드, 필터, 그룹화 기준으로 조회 결과를 표시한다                   |
| FR-014 | Employee Approval   | 사용자 승인              | 최초 로그인 사용자를 Pending으로 등록하고 Admin 승인 후 역할을 부여한다                     |

## 주요 업무 규칙

| ID     | 규칙                     | 설명                                                                          |
| ------ | ------------------------ | ----------------------------------------------------------------------------- |
| BR-001 | 제출/승인 상태 수정 금지 | Submitted 또는 Approved 상태인 주의 Time Entry는 수정할 수 없다               |
| BR-002 | 반려 사유 필수           | Reject 처리 시 RejectReason을 반드시 저장한다                                 |
| BR-003 | 단일 트랜잭션            | WeeklyTimesheet 상태 변경과 TimeEntry 상태 변경은 같은 트랜잭션에서 처리한다  |
| BR-004 | Cost Snapshot            | TimeEntry 생성 시 HourlyRateSnapshot과 CostAmount를 함께 저장한다             |
| BR-005 | ClientName 단일 출처     | BizOpty 연결 프로젝트는 고객명을 중복 저장하지 않고 BizOpty에서 표시한다      |
| BR-006 | Period Filter            | PM Dashboard는 시작월/종료월 기준으로 모든 KPI를 재조회한다                   |
| BR-007 | Known Limitation 표시    | 휴가/병가가 가동률 분모에서 제외되지 않는 경우 대시보드에 제한사항을 표시한다 |

## OutSystems 구현 고려사항

- ODC 앱은 End-user app과 공용 Library/Service 성격의 모듈로 나누되, PoC 범위에서는 단순화를 허용한다.
- OutSystems 내장 Role 시스템을 사용하고, 승인 시 User에 Role을 부여한다.
- Aggregate/SQL은 대량 데이터 조회, 루프 내부 쿼리, 무제한 조회에 대해 품질 게이트에서 점검한다.
- Forge 컴포넌트는 Charts, Data Grid, Calendar, OutSystems UI 정도로 제한한다.
- Report Builder는 SQL injection 방지를 위해 필드/필터 allow-list와 파라미터 바인딩을 사용한다.

## 품질 요구사항

- PRD Requirement → Jira Task → QA Scenario → Defect → Backlog 간 추적성이 있어야 한다.
- 승인/반려 이력은 감사 가능해야 한다.
- 상태값과 명칭은 PRD, Jira, UI, QA 리포트에서 일관되어야 한다.
- Critical findings는 0이어야 하며, High findings는 사유와 프로덕션 전환 권고사항을 문서화해야 한다.
- 미해결 결함은 최종 baseline 결정 또는 후속 backlog와 연결해야 한다.
