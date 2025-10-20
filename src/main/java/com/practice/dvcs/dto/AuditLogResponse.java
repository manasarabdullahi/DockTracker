package com.practice.dvcs.dto;

import lombok.Data;

@Data
public class AuditLogResponse {
    private Long id;
    private String action;
    private String timestamp;
    private String username;
}
