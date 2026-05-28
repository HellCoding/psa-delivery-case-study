# 01. 프로젝트 개요

## 배경

이 케이스 스터디는 OutSystems ODC 기반 PSA(Professional Services Automation) PoC 딜리버리 업무를 공개 가능한 형태로 재구성한 문서입니다.

PSA는 영업 기회, 프로젝트, 태스크, 투입 시간, 승인, 원가, 수익률, 리포트를 하나의 업무 흐름으로 관리하기 위한 내부 운영 시스템입니다. 실제 프로젝트에서는 두 오프쇼어 팀이 같은 PSA 요구사항을 각각 구현했고, PM 역할에서 PRD 정리, Jira 운영, Confluence 산출물 관리, QA/UAT, 코드 품질 검토, 최종 baseline 비교를 수행했습니다.

## 프로젝트 목표

- PSA 핵심 업무 흐름을 요구사항, 도메인 모델, 화면/업무 규칙으로 구조화한다.
- OutSystems ODC의 앱/라이브러리 구조, 역할 기반 접근, Server Action, Aggregate, Forge 컴포넌트 사용 기준을 정의한다.
- Jira 기반으로 Phase Epic과 기능 Task를 나누고, PRD Acceptance Criteria와 QA 결과를 연결한다.
- 오프쇼어 팀별 Confluence 산출물(WBS, 주간보고, 모듈 명세, QA 리포트, 종료 보고)을 검토한다.
- UAT/QA에서 발견한 결함을 PRD 근거, 원인, 권고 조치, 재검증 결과까지 추적한다.
- ODC Code Quality(Mentor findings)를 Critical/High/Medium/Low 기준으로 분류하고 수용 기준을 세운다.
- 최종 앱 비교를 통해 baseline 후보와 후속 backlog를 결정한다.

## 역할과 기여

| 역할 | 수행 내용 |
|---|---|
| 요구사항 정리 | PSA PRD, 기능 요구사항, 업무 규칙, 화면 목록, 데이터 모델 정리 |
| Jira 운영 | 6개 Phase Epic, 기능 Task, Acceptance Criteria, QA 댓글, 완료 기준 관리 |
| QA/UAT | 브라우저 테스트와 ODC Studio 확인을 병행하여 결함 재현, 원인 추정, 재검증 |
| 품질 관리 | ODC Code Quality findings를 품질 게이트로 정리하고 프로덕션 전환 리스크 문서화 |
| 오프쇼어 관리 | 팀별 커뮤니케이션 방식, 문서화 수준, 일정 예측성, 산출물 품질 비교 |
| 의사결정 | 기능 완성도, 품질 리스크, 문서화, 유지보수성을 종합해 baseline 추천 |

## 공개용 재작성 기준

- 실제 회사명, 고객명, 벤더명, 담당자명, 이메일, 내부 URL은 사용하지 않는다.
- 실제 Jira ticket export 또는 Confluence export를 사용하지 않는다.
- 실제 화면 캡처, 소스코드, 계정 정보, 계약/상업 정보는 포함하지 않는다.
- 날짜, 수량, 이슈 유형은 포트폴리오 맥락을 설명할 수 있는 수준으로 일반화한다.
- 모든 문장은 내부 원문을 복사하지 않고 공개 가능한 표현으로 다시 쓴다.
