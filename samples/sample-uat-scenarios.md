# UAT 시나리오 예시

| ID | 관련 요구사항 | 사용자 | 사전 조건 | 실행 단계 | 기대 결과 | 우선순위 |
|---|---|---|---|---|---|---:|
| UAT-BIZ-001 | FR-001 | Sales Manager | 승인된 사용자로 로그인 | BizOpty를 생성하고 Deal Status를 변경한다 | 계약금액, 담당자, 상태가 저장되고 목록에 표시된다 | 1 |
| UAT-PRJ-001 | FR-002 | Project Manager | BizOpty가 존재 | BizOpty를 연결해 프로젝트를 생성한다 | 고객명은 BizOpty 기준으로 표시되고 중복 입력되지 않는다 | 1 |
| UAT-TASK-001 | FR-003 | Project Manager | 프로젝트가 존재 | 태스크를 생성하고 정렬 순서를 변경한다 | 태스크 목록이 지정한 순서로 표시된다 | 2 |
| UAT-MEM-001 | FR-004 | Project Manager | 직원과 프로젝트가 존재 | 동일 직원을 같은 프로젝트에 두 번 배정한다 | 중복 배정 validation이 표시된다 | 1 |
| UAT-TS-001 | FR-005 | Employee | 프로젝트/태스크에 배정됨 | 하루에 24시간을 초과해 입력한다 | 저장되지 않고 validation 메시지가 표시된다 | 1 |
| UAT-TS-002 | FR-006 | Employee | Draft 주차가 존재 | 주간 그리드에 프로젝트/태스크별 시간을 입력한다 | 일별/주간 합계가 올바르게 계산된다 | 1 |
| UAT-TS-003 | FR-007, BR-001 | Employee | Draft 주차에 시간이 입력됨 | Submit 후 동일 주차에서 수정 버튼을 확인한다 | Submitted 상태에서는 Add/Edit/Delete와 Submit이 불가능하다 | 1 |
| UAT-TS-004 | FR-008 | Employee | TimeEntry가 없는 주차 | 빈 주차를 Submit한다 | TotalHours 0인 WeeklyTimesheet가 Submitted로 생성된다 | 2 |
| UAT-APR-001 | FR-009 | Project Manager | 팀원이 제출한 주차가 존재 | 제출된 주차를 Approve한다 | WeeklyTimesheet와 TimeEntry가 Approved로 동기화된다 | 1 |
| UAT-APR-002 | FR-009, BR-002 | Project Manager | 팀원이 제출한 주차가 존재 | 반려 사유 없이 Reject를 시도한다 | 저장되지 않고 사유 입력 validation이 표시된다 | 1 |
| UAT-DASH-001 | FR-011, BR-006 | Project Manager | 여러 월의 TimeEntry가 존재 | 시작월/종료월을 바꿔 KPI를 조회한다 | 비용, 시간, 진척률, 가동률이 같은 기간 조건으로 재계산된다 | 2 |
| UAT-RPT-001 | FR-013 | Executive | ReportDefinition이 존재 | 필드, 필터, 그룹을 선택해 리포트를 실행한다 | 선택한 조건에 맞는 결과와 empty state가 표시된다 | 2 |
| UAT-ADM-001 | FR-014 | Admin | Pending User가 존재 | 사용자를 승인하고 역할을 부여한다 | 사용자는 승인 후 역할에 맞는 화면에 접근한다 | 1 |
