package com.practice.dvcs.controller;

import com.practice.dvcs.dto.AuditLogResponse;
import com.practice.dvcs.dto.DocumentRequest;
import com.practice.dvcs.dto.DocumentVersionRequest;
import com.practice.dvcs.dto.DocumentVersionResponse;
import com.practice.dvcs.model.Document;
import com.practice.dvcs.model.DocumentVersion;
import com.practice.dvcs.model.User;
import com.practice.dvcs.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {


    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;

    }

    @PostMapping("/{userId}")
    public ResponseEntity<Document> createDocument(@PathVariable Long userId, @RequestBody DocumentRequest documentRequest) {
        Document createdDoc = documentService.createDocument(userId, documentRequest);
        return ResponseEntity.ok(createdDoc);
    }

    @PostMapping("/versions/{userId}/{docId}")
    public ResponseEntity<DocumentVersionResponse> addVersion(@PathVariable Long userId, @PathVariable Long docId,@RequestBody DocumentVersionRequest versionRequest) {
        DocumentVersionResponse newVersion = documentService.addVersion(userId,docId,versionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVersion);
    }

    @GetMapping("/{id}")
    public Document getOriginalDocumentById(@PathVariable Long id) {
        return documentService.getDocumentById(id).get();
    }

    @GetMapping
    public List<Document> getAllOriginalDocuments() {
        return documentService.getAllDocuments();

    }

    @GetMapping("/{id}/versions")
    public List<DocumentVersion> getAllVersionsByDocumentId(@PathVariable Long id) {
        return documentService.getAllVersions(id);

    }

    @GetMapping("{documentId}/{versionId}")
    public DocumentVersion getVersionById(@PathVariable Long documentId, @PathVariable Long versionId) {
        return documentService.getVersionDocumentIdAndVersionId(documentId, versionId).get();
    }

    @GetMapping("/users/{versionId}")
    public User getEditorByVersionId(@PathVariable Long versionId){
        return documentService.getEditorByVersionId(versionId);
    }

    @GetMapping("/auditLog/{versionId}")
    public AuditLogResponse getAuditLogByDocumentVersion(@PathVariable Long versionId) {
        return documentService.getAuditLogByDocumentVersion(versionId);
    }
}

