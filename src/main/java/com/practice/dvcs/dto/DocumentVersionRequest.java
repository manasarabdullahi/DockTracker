package com.practice.dvcs.dto;

import lombok.Data;

@Data
public class DocumentVersionRequest {
    private String content;
    private String message;
}
