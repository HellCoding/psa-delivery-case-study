package io.hellcoding.psa.repository;

import io.hellcoding.psa.domain.ProjectTask;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {

    List<ProjectTask> findByProjectIdAndActiveTrueOrderBySortOrderAsc(Long projectId);
}
