package com.easymarkersapp.easymarkersapp.repository;

import com.easymarkersapp.easymarkersapp.model.NoteMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteMessageRepository extends JpaRepository<NoteMessage, Long> {

}
