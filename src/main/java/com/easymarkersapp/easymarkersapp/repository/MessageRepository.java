package com.easymarkersapp.easymarkersapp.repository;

import com.easymarkersapp.easymarkersapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
