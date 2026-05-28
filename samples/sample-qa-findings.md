# QA Findings 예시

| ID | 영역 | 심각도 | 관련 요구사항 | 현상 | 기대 결과 | 권고 조치 | 재검증 |
|---|---|---|---|---|---|---|---|
| QA-TS-001 | Submit Workflow | High | BR-001 | 제출 후에도 Add/Edit/Delete가 활성화됨 | Submitted/Approved 주차는 수정 불가 | 상태가 Draft/Rejected일 때만 편집 버튼 노출 | Pending |
| QA-APR-001 | Reject Workflow | High | BR-002 | 반려 사유 없이 Reject 저장 가능 | RejectReason 필수 입력 | Reject popup과 서버 validation 추가 | Pending |
| QA-TS-002 | State Sync | High | BR-003 | WeeklyTimesheet는 Submitted이나 TimeEntry는 Draft로 남음 | 상태가 같은 트랜잭션에서 동기화 | Submit/Approve/Reject server action 정리 | Pending |
| QA-RPT-001 | Report Viewer | High | FR-013 | 빈 결과에서 viewer 오류 발생 | 결과 없음 안내 표시 | empty state와 예외 처리 추가 | Pass |
| QA-DASH-001 | PM Dashboard | Medium | BR-006 | 일부 KPI에 기간 필터 미적용 | 모든 KPI가 같은 기간 조건 사용 | 공통 filter input을 aggregate에 적용 | Pending |
| QA-DATA-001 | Project List | Medium | FR-002 | 목록에서 같은 프로젝트가 중복 표시 | 프로젝트는 한 번만 표시 | join 조건과 distinct 처리 검토 | Pending |
| QA-SEC-001 | Permission | High | 품질 게이트 | 일반 사용자도 관리 화면 접근 가능 | 역할별 화면 접근 제한 | Screen permission과 server-side role check 추가 | Pending |
