package com.practice.dvcs.dto;

import lombok.Data;

@Data
public class DocumentVersionResponse {
    private Long id;
    private long originalDocumentId;
    private int versionNumber;
    private String title;
    private String content;
    private String updateNote;
}
