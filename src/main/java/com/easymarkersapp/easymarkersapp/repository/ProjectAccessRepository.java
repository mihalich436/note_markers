package com.easymarkersapp.easymarkersapp.repository;

import com.easymarkersapp.easymarkersapp.model.Project;
import com.easymarkersapp.easymarkersapp.model.ProjectAccess;
import com.easymarkersapp.easymarkersapp.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectAccessRepository extends JpaRepository<ProjectAccess, Long> {

    List<ProjectAccess> findByProject(Project project);

    @EntityGraph(attributePaths = {"project"})
    List<ProjectAccess> findByUser(User user);

    Optional<ProjectAccess> findByProjectAndUser(Project project, User user);

    @EntityGraph(attributePaths = {"project"})
    Optional<ProjectAccess> findByProjectIdAndUser(Long projectId, User user);

    void deleteByProjectAndUser(Project project, User user);

    @Query("SELECT pa.user FROM ProjectAccess pa WHERE pa.project = :project")
    List<User> findUsersWithAccess(@Param("project") Project project);

    boolean existsByProjectAndUser(Project project, User user);
}
