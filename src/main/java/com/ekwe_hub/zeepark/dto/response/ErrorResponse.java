package com.ekwe_hub.zeepark.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse
        (LocalDateTime timeStamp, int status, String message, String error, String path) {
}
