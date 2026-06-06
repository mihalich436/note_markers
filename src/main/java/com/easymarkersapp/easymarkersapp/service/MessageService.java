package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.model.Message;
import com.easymarkersapp.easymarkersapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message save(Message message) {
        return messageRepository.save(message);
    }

}
