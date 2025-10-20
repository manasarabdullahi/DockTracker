package com.practice.dvcs.service;

import com.practice.dvcs.dto.AuditLogResponse;
import com.practice.dvcs.dto.DocumentRequest;
import com.practice.dvcs.dto.DocumentVersionRequest;
import com.practice.dvcs.dto.DocumentVersionResponse;
import com.practice.dvcs.model.AuditLog;
import com.practice.dvcs.model.Document;
import com.practice.dvcs.model.DocumentVersion;
import com.practice.dvcs.model.User;
import com.practice.dvcs.repository.AuditLogRepository;
import com.practice.dvcs.repository.DocumentRepository;
import com.practice.dvcs.repository.DocumentVersionRepository;
import com.practice.dvcs.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepository documentRepo;
    private final DocumentVersionRepository versionRepository;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepo;

    public DocumentService(DocumentRepository documentRepo,DocumentVersionRepository versionRepository, AuditLogRepository auditLogRepository, UserRepository userRepo) {
        this.documentRepo = documentRepo;
        this.versionRepository = versionRepository;
        this.auditLogRepository = auditLogRepository;
        this.userRepo = userRepo;
    }

    public Document createDocument(Long id, DocumentRequest docRequest) {

        User owner = userRepo.findById(id).get();

        Document doc = new Document();
        doc.setTitle(docRequest.getTitle());
        doc.setContent(docRequest.getContent());
        doc.setOwner(owner);
        documentRepo.save(doc);

        DocumentVersion version = new DocumentVersion();
        version.setDocument(doc);
        version.setEditor(owner);
        version.setVersionNumber(1);
        version.setContent(docRequest.getContent());
        version.setUpdateNote("Initial version");
        versionRepository.save(version);

        doc.setCurrentVersion(version);
        documentRepo.save(doc);

        logAction(owner, version ,"CREATE_DOCUMENT");
        return doc;
    }

    public DocumentVersionResponse addVersion(Long userId,Long docId,DocumentVersionRequest DVRequest) {

        Document doc = documentRepo.findById(docId).get();
        User editor = userRepo.findById(userId).get();

        int nextVersion = doc.getCurrentVersion().getVersionNumber() + 1;

        DocumentVersion newVersion = new DocumentVersion();
        newVersion.setDocument(doc);
        newVersion.setEditor(editor);
        newVersion.setVersionNumber(nextVersion);
        newVersion.setContent(DVRequest.getContent());
        newVersion.setUpdateNote(DVRequest.getMessage());

        versionRepository.save(newVersion);

        doc.setCurrentVersion(newVersion);
        documentRepo.save(doc);

        logAction(editor, newVersion, "ADD_VERSION");

        return DocumentVersionToDtoConversion(newVersion);

    }

    public List<DocumentVersion> getAllVersions(Long documentId) {
        return versionRepository.findByDocumentIdOrderByVersionNumberDesc(documentId);
    }

    public Optional<DocumentVersion> getVersionDocumentIdAndVersionId(Long documentId,Long versionId) {
        return versionRepository.findByDocumentIdAndId(documentId,versionId);
    }

    public Optional<Document> getDocumentById(Long documentId) {
        return documentRepo.findById(documentId);
    }

    public List<Document> getAllDocuments() {
        return documentRepo.findAll();
    }

    public List<Document> getDocumentsByUser(Long userId) {
        return documentRepo.findByOwnerId(userId);
    }

    public User getEditorByVersionId(Long versionId) {
        return versionRepository.findById(versionId).get().getEditor();
    }

    public AuditLogResponse getAuditLogByDocumentVersion(Long versionId) {
        return AuditLogToDtoConversion(auditLogRepository.findById(versionId).get());
    }


    private void logAction(User user, DocumentVersion doc, String action) {
        AuditLog log = new AuditLog();
        log.setUser(user);
        log.setDocumentVersion(doc);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }


    public DocumentVersionResponse DocumentVersionToDtoConversion(DocumentVersion dv) {
        DocumentVersionResponse dvr = new DocumentVersionResponse();
        dvr.setId(dv.getId());
        dvr.setVersionNumber(dv.getVersionNumber());
        dvr.setOriginalDocumentId(dv.getDocument().getId());
        dvr.setTitle(dv.getDocument().getTitle());
        dvr.setContent(dv.getContent());
        dvr.setUpdateNote(dv.getUpdateNote());
        return dvr;
    }


    public AuditLogResponse AuditLogToDtoConversion(AuditLog auditLog) {
        AuditLogResponse auditLogResponse = new AuditLogResponse();
        auditLogResponse.setId(auditLog.getId());
        auditLogResponse.setAction(auditLog.getAction());
        auditLogResponse.setTimestamp(auditLog.getTimestamp().toString());
        auditLogResponse.setUsername(auditLog.getUser().getUsername());
        return auditLogResponse;
    }
}
