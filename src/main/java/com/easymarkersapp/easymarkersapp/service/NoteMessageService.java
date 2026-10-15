package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.dto.note_message.NoteMessageUpdateRequest;
import com.easymarkersapp.easymarkersapp.dto.note_message.NoteMessageUpdateResult;
import com.easymarkersapp.easymarkersapp.model.*;
import com.easymarkersapp.easymarkersapp.repository.NoteMessageRepository;
import com.easymarkersapp.easymarkersapp.repository.NoteRepository;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NoteMessageService {
    @Autowired
    private NoteMessageRepository messageRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private ProjectAccessService accessService;

    public NoteMessage save(NoteMessage message) {
        String cleanText = Jsoup.clean(message.getText(), Safelist.relaxed());
        message.setText(cleanText);
        return messageRepository.save(message);
    }

    @Transactional
    public NoteMessageUpdateResult updateByIdAndCheckAccess(Long messageId, Long noteId,
                                                                  NoteMessageUpdateRequest request, User user) {
        Optional<NoteMessage> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isEmpty()) return null;
        NoteMessage message = messageOptional.get();

        if (!message.getNoteId().equals(noteId)) return null;

        Optional<Note> noteOptional = noteRepository.findById(noteId);
        if (noteOptional.isEmpty()) return null;
        Note note = noteOptional.get();

        Optional<ProjectAccess> accessOptional = accessService.findByProjectIdAndUser(note.getProjectId(), user);
        if (accessOptional.isEmpty()) return null;

        ProjectAccess access = accessOptional.get();
        if (access.getRole().hasAccess(AccessRole.ADMIN)
                || message.getUserId().equals(user.getId())) {
            request.updateMessage(message);
            this.save(message);
            return new NoteMessageUpdateResult(noteId, message);
        }
        return null;
    }

    @Transactional
    public boolean deleteByIdAndCheckAccess(Long messageId, Long noteId, User user) {
        Optional<NoteMessage> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isEmpty()) return false;
        NoteMessage message = messageOptional.get();

        if (!message.getNoteId().equals(noteId)) return false;

        Optional<Note> noteOptional = noteRepository.findById(noteId);
        if (noteOptional.isEmpty()) return false;
        Note note = noteOptional.get();

        Optional<ProjectAccess> accessOptional = accessService.findByProjectIdAndUser(note.getProjectId(), user);
        if (accessOptional.isEmpty()) return false;

        ProjectAccess access = accessOptional.get();
        if (access.getRole().hasAccess(AccessRole.ADMIN)
                || message.getUserId().equals(user.getId())) {
            messageRepository.delete(message);
            return true;
        }
        return false;
    }
}
