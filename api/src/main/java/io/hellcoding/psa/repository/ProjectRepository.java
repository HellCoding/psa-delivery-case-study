package io.hellcoding.psa.repository;

import io.hellcoding.psa.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByCode(String code);
}
