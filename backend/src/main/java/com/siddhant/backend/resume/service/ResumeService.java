package com.siddhant.backend.resume.service;

import com.siddhant.backend.auth.repository.UserRepository;
import com.siddhant.backend.resume.entity.Resume;
import com.siddhant.backend.resume.repository.ResumeRepository;
import com.siddhant.backend.student.entity.Student;
import com.siddhant.backend.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service @RequiredArgsConstructor @Transactional
public class ResumeService {
    private final ResumeRepository resumes;
    private final StudentRepository students;
    private final UserRepository users;
    @Value("${app.upload.directory:./uploads/resumes}") private String uploadDirectory;
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    public Resume upload(String email, MultipartFile file) {
        if (file == null || file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Choose a resume file");
        if (file.getSize() > MAX_SIZE) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resume must be 5 MB or smaller");
        String original = file.getOriginalFilename() == null ? "resume" : Paths.get(file.getOriginalFilename()).getFileName().toString();
        String lower = original.toLowerCase();
        boolean pdf = lower.endsWith(".pdf") && "application/pdf".equalsIgnoreCase(file.getContentType());
        boolean docx = lower.endsWith(".docx") && ("application/vnd.openxmlformats-officedocument.wordprocessingml.document".equalsIgnoreCase(file.getContentType()) || "application/octet-stream".equalsIgnoreCase(file.getContentType()));
        if (!pdf && !docx) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PDF and DOCX resumes are allowed");
        byte[] content;
        try { content = file.getBytes(); } catch (IOException ex) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not read the uploaded file"); }
        boolean pdfSignature = content.length >= 5 && content[0] == '%' && content[1] == 'P' && content[2] == 'D' && content[3] == 'F' && content[4] == '-';
        boolean zipSignature = content.length >= 4 && content[0] == 'P' && content[1] == 'K' && content[2] == 3 && content[3] == 4;
        if ((pdf && !pdfSignature) || (docx && !zipSignature)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The file content does not match the selected resume format");
        Student student = currentStudent(email);
        Resume old = resumes.findByStudentId(student.getId()).orElse(null);
        String stored = UUID.randomUUID() + (pdf ? ".pdf" : ".docx");
        Path dir = Paths.get(uploadDirectory).toAbsolutePath().normalize();
        try {
            Files.createDirectories(dir);
            Path target = dir.resolve(stored).normalize();
            if (!target.startsWith(dir)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file name");
            Files.write(target, content, StandardOpenOption.CREATE_NEW);
            if (old != null) { Files.deleteIfExists(dir.resolve(old.getStoredName())); old.setStoredName(stored); old.setOriginalName(original); old.setContentType(pdf ? "application/pdf" : "application/vnd.openxmlformats-officedocument.wordprocessingml.document"); old.setSize(file.getSize()); return resumes.save(old); }
            return resumes.save(Resume.builder().student(student).storedName(stored).originalName(original).contentType(pdf ? "application/pdf" : "application/vnd.openxmlformats-officedocument.wordprocessingml.document").size(file.getSize()).build());
        } catch (IOException ex) { throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store resume"); }
    }
    @Transactional(readOnly = true)
    public Resume getForUser(String email) { return resumes.findByStudentId(currentStudent(email).getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found")); }
    public byte[] download(String email) {
        Resume r = getForUser(email); Path path = Paths.get(uploadDirectory).toAbsolutePath().normalize().resolve(r.getStoredName()).normalize();
        try { if (!path.startsWith(Paths.get(uploadDirectory).toAbsolutePath().normalize())) throw new IOException(); return Files.readAllBytes(path); }
        catch (IOException ex) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume file is unavailable"); }
    }
    public void delete(String email) { Resume r = getForUser(email); try { Files.deleteIfExists(Paths.get(uploadDirectory).toAbsolutePath().normalize().resolve(r.getStoredName())); } catch (IOException ignored) { } resumes.delete(r); }
    private Student currentStudent(String email) {
        var user = users.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return students.findByUserId(user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Create your student profile first"));
    }
}
