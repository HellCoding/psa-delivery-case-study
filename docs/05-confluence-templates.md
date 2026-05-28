# 05. Confluence 문서 운영

이 저장소는 실제 Confluence 문서를 export하지 않고, 공개 가능한 운영 구조와 템플릿으로 재작성했습니다. 핵심은 문서가 "보고용"으로 끝나지 않고 Jira, QA, 품질 게이트, 최종 의사결정과 연결되도록 설계하는 것입니다.

## 운영한 문서 유형

| 문서 | 목적 | 연결되는 산출물 |
|---|---|---|
| Project Scope & PRD | 범위, 역할, 화면, 데이터, 업무 규칙의 기준점 | Jira Epic/Task, UAT Scenario |
| WBS | Phase별 일정과 책임 구분 | Sprint/Phase tracking |
| Daily/Weekly Report | 진행률, 이슈, blocker, 다음 액션 공유 | Jira 상태, 리스크 로그 |
| Module Functional Spec | OutSystems 모듈, 화면, Server Action, entity 기준 정리 | 코드 품질 리뷰, 인수인계 |
| QA Report | UAT 실행 결과와 결함 요약 | Jira QA 댓글, follow-up backlog |
| Code Quality Report | ODC Mentor findings와 수용 기준 정리 | 품질 게이트, production recommendation |
| Closure Report | 최종 완료율, 잔여 리스크, 교훈 정리 | 벤더 비교, baseline 결정 |
| Final Comparison | 두 산출물의 기능/품질/문서화/운영성 비교 | 최종 의사결정 |

## 좋은 문서의 기준

| 기준 | 설명 |
|---|---|
| 추적성 | 요구사항, Jira task, QA scenario, defect, backlog가 서로 연결된다 |
| 실행 가능성 | 개발자가 문서를 보고 바로 구현/수정할 수 있다 |
| 검증 가능성 | QA가 기대 결과와 예외 조건을 명확히 확인할 수 있다 |
| 최신성 | 요구사항 변경, 일정 변경, QA 결과가 문서에 반영된다 |
| 인수인계성 | 새 담당자가 들어와도 현재 상태와 남은 리스크를 파악할 수 있다 |

## 문서 품질에서 확인한 차이

두 오프쇼어 팀은 같은 PSA 요구사항을 구현했지만 문서화 방식은 달랐습니다. 한 팀은 회의/댓글 기반 커뮤니케이션이 강했고, 다른 팀은 PRD, WBS, 주간보고, 기능 명세, 코드 품질 리포트처럼 formal artifact가 더 풍부했습니다.

최종 평가는 어느 한쪽 문서 수량만으로 결정하지 않았습니다. 실제 기준은 문서가 다음 질문에 답할 수 있는지였습니다.

- 이 기능의 원래 요구사항은 무엇인가?
- 어떤 Jira task에서 구현했는가?
- 어떤 UAT에서 통과 또는 실패했는가?
- 실패했다면 어떤 결함으로 기록했고 재검증했는가?
- 운영 전 반드시 해결해야 할 품질 리스크는 무엇인가?

## 템플릿 매핑

| 템플릿 | 사용 시점 | 파일 |
|---|---|---|
| Weekly Report | 매주 진행률, 이슈, 다음 액션 공유 | [`templates/weekly-report-template.md`](../templates/weekly-report-template.md) |
| QA Report | UAT 실행 후 결함과 재검증 결과 정리 | [`templates/qa-report-template.md`](../templates/qa-report-template.md) |
| Decision Log | scope, baseline, defer 결정 기록 | [`templates/decision-log-template.md`](../templates/decision-log-template.md) |
| Jira Epic Template | Phase 또는 큰 기능 단위 정의 | [`templates/jira-epic-template.md`](../templates/jira-epic-template.md) |
| Jira Task Template | 개발 가능한 작업 단위 정의 | [`templates/jira-task-template.md`](../templates/jira-task-template.md) |

## 문서 운영 원칙

- 회의록보다 결정사항과 open action을 남긴다.
- PRD 변경은 version note와 영향받는 Jira task를 함께 기록한다.
- "구현됨"과 "검증됨"을 분리해서 쓴다.
- QA 실패는 감정 표현이 아니라 재현 단계, 기대 결과, 근거 요구사항으로 작성한다.
- 품질 게이트에서 수용한 High finding은 production 전 권고 조치까지 남긴다.
- 공개 저장소에는 실제 내부 링크, 화면 캡처, 실명, 상업 정보를 남기지 않는다.
