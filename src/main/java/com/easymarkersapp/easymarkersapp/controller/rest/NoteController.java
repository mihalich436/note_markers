package com.easymarkersapp.easymarkersapp.controller.rest;

import com.easymarkersapp.easymarkersapp.config.AccessContext;
import com.easymarkersapp.easymarkersapp.dto.note.NoteWithRoleDto;
import com.easymarkersapp.easymarkersapp.model.Note;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/notes")
@CrossOrigin() //> add addr and port
public class NoteController {
    @Autowired
    private NoteService noteService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getNote(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        NoteWithRoleDto noteDto = null;
        if (AccessContext.isUser(auth)) {
            User currentUser = (User) auth.getPrincipal();
            noteDto = noteService.findByIdAndCheckAccess(id, currentUser);
        }
        if (noteDto == null) {
            Long shareProjectId = AccessContext.shareProjectId(auth);
            if (shareProjectId != null) {
                noteDto = noteService.findByIdAndCheckShareAccess(id, shareProjectId);
            }else {
                return ResponseEntity.status(401).body("Unauthorized");
            }
        }

        if (noteDto != null) {
            return ResponseEntity.ok(noteDto);
        }

        return ResponseEntity.status(404).body("Cannot access note");
    }
}
