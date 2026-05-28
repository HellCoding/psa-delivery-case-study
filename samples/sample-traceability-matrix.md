# Traceability Matrix 예시

| Requirement | Jira Task | UAT Scenario | QA Finding | Backlog | 상태 |
|---|---|---|---|---|---|
| FR-001 BizOpty 관리 | PSA-TASK-010 | UAT-BIZ-001 | - | - | Pass |
| FR-002 Project 생성 | PSA-TASK-020 | UAT-PRJ-001 | QA-DATA-001 | BL-PRJ-001 | Conditional Pass |
| FR-006 Weekly Timesheet | PSA-TASK-040 | UAT-TS-002 | - | - | Pass |
| FR-007 Submit Workflow | PSA-TASK-050 | UAT-TS-003 | QA-TS-001 | BL-TS-001 | Fail -> Recheck |
| FR-009 Approval Workflow | PSA-TASK-060 | UAT-APR-002 | QA-APR-001 | BL-APR-001 | Fail |
| FR-011 PM Dashboard | PSA-TASK-070 | UAT-DASH-001 | QA-DASH-001 | BL-DASH-001 | Conditional Pass |
| FR-013 Report Builder | PSA-TASK-080 | UAT-RPT-001 | QA-RPT-001 | BL-RPT-001 | Rechecked |
| FR-014 Employee Approval | PSA-TASK-090 | UAT-ADM-001 | - | - | Pass |

이 표의 목적은 요구사항이 문서에만 남지 않고 구현, 테스트, 결함, 후속 backlog까지 이어졌는지 한눈에 확인하는 것입니다.
