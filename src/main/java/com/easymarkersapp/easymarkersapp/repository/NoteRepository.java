package com.easymarkersapp.easymarkersapp.repository;

import com.easymarkersapp.easymarkersapp.model.Note;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    @EntityGraph(attributePaths = {"project"})
    Optional<Note> findByIdAndProjectId(Long noteId, Long projectId);

    @EntityGraph(attributePaths = {"messages"})
    Optional<Note> findWithMessagesById(Long id);

    @EntityGraph(attributePaths = {"project"})
    Optional<Note> findWithProjectById(Long id);
}
