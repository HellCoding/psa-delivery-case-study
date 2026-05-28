# 09. 최종 의사결정 및 후속 Backlog

## Baseline 결정 형식

최종 baseline 추천 문서는 아래 내용을 포함해야 합니다.

1. 추천 baseline
2. 선택 사유
3. 수용하는 잔여 리스크
4. 배포 전 반드시 해결할 항목
5. 후속 개선 backlog
6. 담당자와 다음 리뷰 일정

## 예시 결정 요약

| 항목 | 내용 |
|---|---|
| 추천 baseline | Vendor A 구현안을 기준 baseline으로 선정 |
| 선택 사유 | PSA 핵심 업무 규칙, 제출/승인 상태 전이, 리포트 안정성이 더 높음 |
| 보완 방향 | Vendor B의 문서화 방식과 일부 UX 아이디어를 후속 개선에 반영 |
| 수용 리스크 | Medium/Low 품질 항목, 일부 대시보드 UX, 리포트 성능 최적화 |
| 필수 해결 | 권한 예외, 상태 수정 제한, 반려 사유, 동적 리포트 예외 처리 |
| 후속 개선 | 대시보드 필터, 리포트 템플릿, 상태값 표준화, 인수인계 문서 보강 |

## Defer 판단 기준

| 항목 | Defer 가능 여부 | 판단 기준 |
|---|---|---|
| Critical 보안/데이터 손상 | 불가 | 운영 전 반드시 해결 |
| 제출/승인 상태 위반 | 불가 | 비용/승인 데이터 신뢰성에 직접 영향 |
| 반려 사유 누락 | 불가 | 감사 추적과 사용자 피드백에 직접 영향 |
| Medium 성능 개선 | 조건부 가능 | 데이터량 제한과 후속 backlog가 명확한 경우 |
| 대시보드 UX 개선 | 가능 | 핵심 KPI가 정확히 계산되는 경우 |
| 문서 보강 | 조건부 가능 | 인수인계에 필요한 최소 문서가 존재하는 경우 |

## 후속 Backlog 예시

| 우선순위 | 유형 | Backlog 항목 | 이유 | 인수 기준 |
|---:|---|---|---|---|
| 1 | Bug/Quality | Submitted/Approved 주차 편집 차단 | 승인 데이터 신뢰성 확보 | 해당 상태에서 Add/Edit/Delete가 불가능하다 |
| 1 | Bug/Quality | RejectReason 필수 검증 | 감사 추적과 반려 피드백 확보 | 빈 사유로 Reject 저장 불가 |
| 1 | Security | 역할별 Screen Permission 보강 | 권한 우회 방지 | Employee/PM/Admin 역할별 접근 테스트 통과 |
| 2 | Refactor | 상태값 표준화 | 리포트와 QA 기준 불일치 방지 | 상태값 static entity와 UI 라벨 매핑 정리 |
| 2 | Feature | Report Builder 예외 처리 | 빈 결과/잘못된 필터에서 오류 방지 | empty state와 validation 메시지 표시 |
| 3 | Enhancement | PM Dashboard UX 개선 | 탐색성과 의사결정 속도 향상 | 기간/프로젝트/팀원 필터가 KPI 전체에 적용 |
| 3 | Documentation | 기능 명세와 운영 인수인계 보강 | 후속 유지보수 비용 감소 | 주요 화면/Server Action/데이터 규칙 문서화 |

## 이력서 연결 문구 예시

```text
OutSystems ODC 기반 PSA PoC에서 PRD 정리, Jira/Confluence 운영, QA/UAT, ODC Code Quality gate, 오프쇼어 산출물 비교와 baseline 의사결정을 수행했습니다. 실제 산출물은 공개하지 않고 요구사항-구현-QA-품질-의사결정 추적 구조를 clean-room case study로 재구성했습니다.
```
