# 07. 코드 품질 게이트

## 목적

Low-code 산출물도 운영 baseline으로 채택하려면 기능 통과 여부만으로는 부족합니다. OutSystems ODC 앱은 화면, Server Action, Aggregate, SQL, Role, Forge 컴포넌트가 빠르게 늘어나기 때문에 품질 게이트를 통해 유지보수성, 보안, 성능, 데이터 정합성을 확인했습니다.

## 품질 게이트

| Gate | 기준 | 판정 방식 |
|---|---|---|
| QG-1 | Critical finding = 0 | Critical이 남아 있으면 baseline 후보 제외 |
| QG-2 | Medium/Low finding 처리 | 해결, dismiss 사유, follow-up backlog 중 하나로 정리 |
| QG-3 | High finding 문서화 | PoC 수용 사유와 production 전 권고 조치 기록 |

ODC 품질 점수는 발견 항목의 심각도와 계산 방식에 따라 PoC 맥락과 다르게 보일 수 있습니다. 그래서 숫자 점수만 보지 않고, finding의 성격과 운영 전 영향도를 함께 판단했습니다.

## 검토 기준

| 기준 | 검토 질문 |
|---|---|
| 보안 | 역할/권한 검증이 화면과 Server Action에 적용되어 있는가? |
| 데이터 정합성 | 필수값, 중복 방지, 상태 전이, 승인 조건이 서버에서 검증되는가? |
| 성능 | Aggregate/SQL이 루프 내부에서 반복 실행되거나 무제한 조회되지 않는가? |
| 유지보수성 | 모듈, entity, action, screen naming이 일관적인가? |
| 트랜잭션 | WeeklyTimesheet와 TimeEntry 상태 변경이 원자적으로 처리되는가? |
| 리포트 안전성 | 동적 리포트 필드/필터가 allow-list와 parameter binding을 사용하는가? |

## 대표 finding과 처리 방향

| Finding 유형 | 심각도 | 리스크 | 처리 방향 |
|---|---|---|---|
| 화면 접근 권한이 Everyone으로 열림 | High | 승인 전 사용자 또는 일반 사용자가 민감 화면 접근 | 역할별 screen permission 적용 |
| 클라이언트에서 사용자 식별값 직접 사용 | High | 권한 우회 또는 감사 추적 약화 | 서버 측 사용자 확인으로 이전 |
| 루프 내부 Aggregate/SQL 실행 | High/Medium | 데이터 증가 시 성능 저하 | bulk query 또는 사전 조회로 변경 |
| Static Entity와 상태값 명칭 불일치 | Medium | 리포트/필터/QA 기준 불일치 | 상태값 표준화 |
| 미사용 action 또는 중복 로직 | Low | 유지보수 비용 증가 | refactor backlog 등록 |

## 품질 보고서 형식

| 항목 | 작성 내용 |
|---|---|
| 앱/모듈 범위 | 검토 대상 ODC 앱과 주요 모듈 |
| 전체 finding 수 | Critical/High/Medium/Low 분포 |
| Gate 결과 | QG-1/QG-2/QG-3 통과 여부 |
| 남은 High finding | PoC 수용 사유와 production 전 조치 |
| 해결한 항목 | resolved/dismissed/follow-up 분류 |
| 최종 판단 | baseline 채택 가능 여부와 조건 |

## Baseline 수용 기준

Critical finding이 남아 있으면 baseline으로 수용하지 않습니다. High finding은 PoC에서는 제한적으로 수용할 수 있지만, 반드시 운영 전 조치 권고와 backlog를 남겨야 합니다. Medium/Low는 단순히 닫는 것이 아니라 해결, dismiss 사유, 후속 개선 중 하나로 정리해야 합니다.
