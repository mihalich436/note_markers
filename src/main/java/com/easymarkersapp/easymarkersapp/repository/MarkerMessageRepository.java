package com.easymarkersapp.easymarkersapp.repository;

import com.easymarkersapp.easymarkersapp.model.MarkerMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkerMessageRepository extends JpaRepository<MarkerMessage, Long> {

}
