package io.hellcoding.psa.repository;

import io.hellcoding.psa.domain.ProjectMember;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    boolean existsByProjectIdAndEmployeeId(Long projectId, Long employeeId);

    long countByProjectId(Long projectId);

    List<ProjectMember> findByProjectId(Long projectId);

    @Query("""
            select count(pm)
            from ProjectMember pm
            where pm.project.id = :projectId
              and pm.employee.id = :employeeId
              and pm.startDate <= :entryDate
              and pm.endDate >= :entryDate
            """)
    long countActiveAssignment(
            @Param("projectId") Long projectId,
            @Param("employeeId") Long employeeId,
            @Param("entryDate") LocalDate entryDate
    );

    @Query("""
            select count(pm)
            from ProjectMember pm
            where pm.employee.id = :employeeId
              and pm.project.projectManager.id = :reviewerId
              and pm.startDate <= :weekEnd
              and pm.endDate >= :weekStart
            """)
    long countActiveAssignmentsManagedByReviewer(
            @Param("employeeId") Long employeeId,
            @Param("reviewerId") Long reviewerId,
            @Param("weekStart") LocalDate weekStart,
            @Param("weekEnd") LocalDate weekEnd
    );
}
