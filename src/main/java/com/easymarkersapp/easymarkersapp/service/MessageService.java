package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.dto.message.MessageDeleteResult;
import com.easymarkersapp.easymarkersapp.dto.message.MessageUpdateRequest;
import com.easymarkersapp.easymarkersapp.dto.message.MessageUpdateResult;
import com.easymarkersapp.easymarkersapp.model.*;
import com.easymarkersapp.easymarkersapp.repository.MarkerRepository;
import com.easymarkersapp.easymarkersapp.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MarkerRepository markerRepository;
    @Autowired
    private MapService mapService;

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Transactional
    public MessageUpdateResult updateByIdAndCheckAccess(Long id, MessageUpdateRequest request, User user) {
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isEmpty()) return null;
        Message message = messageOptional.get();
        Optional<Marker> markerOptional = markerRepository.findById(message.getMarkerId());
        if (markerOptional.isPresent()) {
            Marker marker = markerOptional.get();
            ProjectAccess access = mapService.getRoleByIdAndUser(marker.getMapId(), user);
            if (access != null && access.getRole().hasAccess(AccessRole.EDITOR)
                    || message.getUserId().equals(user.getId())) {
                request.updateMessage(message);
                messageRepository.save(message);
                return new MessageUpdateResult(marker.getMapId(), message);
            }
        }
        return null;
    }

    @Transactional
    public MessageDeleteResult deleteByIdAndCheckAccess(Long id, User user) {
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isEmpty()) return null;
        Message message = messageOptional.get();
        Optional<Marker> markerOptional = markerRepository.findById(message.getMarkerId());
        if (markerOptional.isPresent()) {
            Marker marker = markerOptional.get();
            ProjectAccess access = mapService.getRoleByIdAndUser(marker.getMapId(), user);
            if (access != null && access.getRole().hasAccess(AccessRole.EDITOR)
                    || message.getUserId().equals(user.getId())) {
                messageRepository.delete(message);
                marker.getMessages().remove(message);
                return new MessageDeleteResult(marker.getId(), marker.getMapId());
            }
        }
        return null;
    }
}
