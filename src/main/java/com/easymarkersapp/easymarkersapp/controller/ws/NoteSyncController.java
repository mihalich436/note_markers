package com.easymarkersapp.easymarkersapp.controller.ws;

import com.easymarkersapp.easymarkersapp.dto.message.*;
import com.easymarkersapp.easymarkersapp.dto.note_message.NoteMessageTextUpdateRequest;
import com.easymarkersapp.easymarkersapp.dto.note_message.NoteMessageUpdateResult;
import com.easymarkersapp.easymarkersapp.dto.note_message.NoteMessageVisibilityUpdateRequest;
import com.easymarkersapp.easymarkersapp.dto.ws.SyncResponse;
import com.easymarkersapp.easymarkersapp.model.*;
import com.easymarkersapp.easymarkersapp.service.NoteMessageService;
import com.easymarkersapp.easymarkersapp.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

@Controller
public class NoteSyncController {
    @Autowired
    private NoteMessageService noteMessageService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("notes/{noteId}/messages")
    public void addMessage(MessageSaveRequest request,
                           @DestinationVariable Long noteId,
                           SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        Note note = noteService.findByIdAndCheckAccess(noteId, currentUser, AccessRole.CHAT);
        if (note != null) {
            NoteMessage messageToAdd = new NoteMessage(request, noteId, currentUser.getId());
            //> second bd transaction
            NoteMessage message = noteMessageService.save(messageToAdd);
            this.template.convertAndSend("/topic/note/" + note.getId(),
                    new SyncResponse<>("message", "add", message));
        }
    }

    @MessageMapping("notes/{noteId}/messages/{messageId}/text")
    public void updateMessageText(NoteMessageTextUpdateRequest request,
                                  @DestinationVariable Long messageId,
                                  @DestinationVariable Long noteId,
                                  SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        NoteMessageUpdateResult result = noteMessageService.updateByIdAndCheckAccess(messageId, noteId, request, currentUser);
        if (result != null) {
            this.template.convertAndSend("/topic/note/" + noteId,
                    new SyncResponse<>("message", "upd", result.message()));
        }
    }

    @MessageMapping("notes/{noteId}/messages/{messageId}/visibility")
    public void updateNoteVisibility(NoteMessageVisibilityUpdateRequest request,
                                     @DestinationVariable Long messageId,
                                     @DestinationVariable Long noteId,
                                     SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        NoteMessageUpdateResult result = noteMessageService.updateByIdAndCheckAccess(messageId, noteId, request, currentUser);
        if (result != null) {
            this.template.convertAndSend("/topic/note/" + noteId,
                    new SyncResponse<>("message", "upd", result.message()));
        }
    }

    @MessageMapping("notes/{noteId}/messages/{messageId}/delete")
    public void deleteMessage(@DestinationVariable Long noteId,
                              @DestinationVariable Long messageId,
                              SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();

        if (noteMessageService.deleteByIdAndCheckAccess(messageId, noteId, currentUser)) {
            this.template.convertAndSend("/topic/note/" + noteId,
                    new SyncResponse<>("message", "del", new MessageDeleteResponse(messageId, noteId)));
        }
    }
}
