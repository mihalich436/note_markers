package com.easymarkersapp.easymarkersapp.dto.message;

import com.easymarkersapp.easymarkersapp.model.MarkerMessage;

public record MarkerMessageUpdateResult(Long mapId, MarkerMessage message) {
}
