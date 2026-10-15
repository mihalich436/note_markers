package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.dto.map.MapWithRoleDTO;
import com.easymarkersapp.easymarkersapp.dto.marker.MarkerRequest;
import com.easymarkersapp.easymarkersapp.dto.note.NoteUpdateRequest;
import com.easymarkersapp.easymarkersapp.dto.note.NoteWithRoleDto;
import com.easymarkersapp.easymarkersapp.model.*;
import com.easymarkersapp.easymarkersapp.repository.NoteRepository;
import com.easymarkersapp.easymarkersapp.repository.ProjectAccessRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private NoteMessageService messageService;
    @Autowired
    private ProjectAccessRepository accessRepository;

    public Note save(Note note) {
        return noteRepository.save(note);
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }

    @Transactional
    public Note findByIdAndProjectIdAndCheckRole(Long id, Long projectId, User user, AccessRole requiredRole) {
        Optional<Note> noteOptional = noteRepository.findByIdAndProjectId(id, projectId);
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            Project project = note.getProject();
            Optional<ProjectAccess> projectAccessOptional = accessRepository.findByProjectAndUser(project, user);
            if (projectAccessOptional.isPresent() && projectAccessOptional.get().getRole().hasAccess(requiredRole)) {
                return note;
            }
        }
        return null;
    }

    @Transactional
    public NoteWithRoleDto findByIdAndCheckAccess(Long id, User user) {
        Optional<Note> noteOptional = noteRepository.findWithMessagesById(id);
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            Project project = note.getProject();
            List<ProjectAccess> projectAccessList = accessRepository.findByProject(project);
            Optional<ProjectAccess> projectAccessOptional = projectAccessList.stream()
                    .filter(projectAccess -> user.getId().equals(projectAccess.getUser().getId()))
                    .findFirst();
            if (projectAccessOptional.isPresent()) {
                ProjectAccess projectAccess = projectAccessOptional.get();
                if (projectAccess.getRole() != AccessRole.ADMIN) {
                    if (!note.getVisibility()) return null;
                    note.setMessages(note.getMessages().stream().filter(NoteMessage::getVisibility).toList());
                }
                java.util.Map<Long, String> userIdToNick = projectAccessList.stream()
                        .collect(Collectors.toMap(
                                access -> access.getUser().getId(),
                                ProjectAccess::getNickname
                        ));
                return new NoteWithRoleDto(note, projectAccess.getRole().name(), user.getId(), userIdToNick);
            }
        }
        return null;
    }
    @Transactional
    public Note findByIdAndCheckAccess(Long id, User user, AccessRole requiredRole) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            Project project = new Project(note.getProjectId());
            ProjectAccess access =  accessRepository.findByProjectAndUser(project, user).orElse(null);
            if (access != null && access.getRole().hasAccess(requiredRole)) {
                return note;
            }
        }
        return null;
    }
    @Transactional
    public Note updateByIdAndCheckAccess(NoteUpdateRequest request, Long id, User user, AccessRole requiredRole) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            boolean visibilityPrev = note.getVisibility();
            Project project = new Project(note.getProjectId());
            ProjectAccess access =  accessRepository.findByProjectAndUser(project, user).orElse(null);
            if (access != null && access.getRole().hasAccess(requiredRole)) {
                request.updateNote(note);
                Note updatedNote = this.save(note);
                if (!visibilityPrev && updatedNote.getVisibility()) {
                    updatedNote.getMessages();
                }
                return updatedNote;
            }
        }
        return null;
    }
    @Transactional
    public NoteWithRoleDto findByIdAndCheckShareAccess(Long id, Long projectId) {
        Optional<Note> noteOptional = noteRepository.findByIdAndProjectId(id, projectId);
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            Project project = note.getProject();
            List<ProjectAccess> projectAccessList = accessRepository.findByProject(project);

            if (!note.getVisibility()) return null;
            note.setMessages(note.getMessages().stream().filter(NoteMessage::getVisibility).toList());
            java.util.Map<Long, String> userIdToNick = projectAccessList.stream()
                    .collect(Collectors.toMap(
                            access -> access.getUser().getId(),
                            ProjectAccess::getNickname
                    ));
            return new NoteWithRoleDto(note, AccessRole.READ_ONLY.name(), null, userIdToNick);
        }
        return null;
    }
}
