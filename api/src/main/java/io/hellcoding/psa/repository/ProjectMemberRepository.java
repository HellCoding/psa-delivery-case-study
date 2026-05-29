package io.hellcoding.psa.repository;

import io.hellcoding.psa.domain.ProjectMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    boolean existsByProjectIdAndEmployeeId(Long projectId, Long employeeId);

    long countByProjectId(Long projectId);

    List<ProjectMember> findByProjectId(Long projectId);
}
