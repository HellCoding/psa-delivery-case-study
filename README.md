# PSA Delivery Case Study

PSA(Professional Services Automation) PoC 딜리버리 경험을 공개 가능한 형태로 재구성한 문서형 포트폴리오입니다.

실제 업무에서는 OutSystems ODC 기반 PSA Timesheet 앱을 대상으로 내부 PRD를 정리하고, 두 오프쇼어 팀의 Jira/Confluence 산출물, QA 결과, ODC Code Quality 결과, 최종 앱 비교를 관리했습니다. 이 저장소는 해당 경험을 회사 자료 없이 **clean-room 방식**으로 다시 쓴 케이스 스터디입니다.

## 보여주는 역량

- PSA 도메인 요구사항을 PRD, 도메인 모델, 화면/업무 규칙으로 구조화
- Jira Epic/Task/QA 댓글을 이용한 오프쇼어 개발 관리
- Confluence 기반 WBS, 주간보고, 모듈 기능 명세, QA 리포트, 종료 보고 운영
- OutSystems ODC 산출물에 대한 품질 게이트 및 Mentor findings 관리
- UAT/QA 발견사항을 PRD 근거, 원인, 조치, 후속 backlog로 연결
- 두 오프쇼어 산출물을 기능 완성도, 품질, 문서화, 커뮤니케이션 기준으로 비교
- 최종 baseline 선정과 잔여 리스크 수용 기준 정리

> **기밀 보호 원칙**
> 이 저장소에는 회사명, 고객명, 벤더명, 내부 프로젝트명, 실제 URL, 실제 화면 캡처, Jira/Confluence 원문 export, 실제 소스코드, 계정 정보, 계약/상업 정보가 포함되어 있지 않습니다. 모든 문서는 공개 포트폴리오 용도로 일반화하여 새로 작성했습니다.

## 저장소 구성

```text
.
├── api/         # Spring Boot clean-room backend API
├── docs/        # 공개용으로 재작성한 케이스 스터디 문서
├── templates/   # Jira / Confluence 스타일 운영 템플릿
├── samples/     # WBS, UAT, traceability, QA findings, backlog 예시
└── diagrams/    # Mermaid 다이어그램
```

## Spring Boot API

백엔드 개발자 포지션을 위해 PSA 도메인의 핵심 업무 규칙을 Spring Boot API로 clean-room 구현했습니다. 회사 코드나 내부 데이터를 사용하지 않고, 공개용 PRD의 업무 규칙을 서버 사이드 도메인 로직으로 재구성했습니다.

- [API 모듈 README](api/README.md)
- Timesheet 제출/승인/반려 상태 전이
- Submitted/Approved 상태의 Time Entry 수정 차단
- RejectReason 필수 검증
- Time Entry 생성 시 hourly rate snapshot과 cost 계산
- Project dashboard 집계 API
- H2 기반 로컬 실행과 통합 테스트

## 다른 PC에서 이어 작업

Java 17이 설치되어 있으면 Maven 설치 없이 Wrapper로 실행할 수 있습니다.

```bash
git clone https://github.com/HellCoding/psa-delivery-case-study.git
cd psa-delivery-case-study/api
./mvnw test
./mvnw spring-boot:run
```

## 추천 읽기 순서

1. [프로젝트 개요](docs/01-project-overview.md)
2. [공개용 PRD](docs/02-sanitized-prd.md)
3. [도메인 모델](docs/03-domain-model.md)
4. [Jira 업무 흐름](docs/04-jira-workflow.md)
5. [Confluence 문서 운영](docs/05-confluence-templates.md)
6. [QA/UAT 계획](docs/06-qa-uat-plan.md)
7. [코드 품질 게이트](docs/07-code-quality-gate.md)
8. [벤더 비교 매트릭스](docs/08-vendor-comparison-matrix.md)
9. [최종 의사결정 및 후속 백로그](docs/09-final-decision-and-backlog.md)
10. [오프쇼어 딜리버리 회고](docs/10-offshore-delivery-retrospective.md)

## 샘플 산출물

- [Traceability Matrix](samples/sample-traceability-matrix.md)
- [QA Findings](samples/sample-qa-findings.md)
- [UAT Scenarios](samples/sample-uat-scenarios.md)
- [WBS](samples/sample-wbs.md)
- [Backlog](samples/sample-backlog.md)

## 포트폴리오 포지셔닝

이 저장소는 “회사 자료를 올린 저장소”가 아닙니다. 실제 업무에서 사용한 분석 기준, 운영 구조, 품질 판단 방식을 공개 가능한 형태로 재작성한 케이스 스터디입니다.
