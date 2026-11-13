package com.siddhant.backend.resume.controller;

import com.siddhant.backend.resume.entity.Resume;
import com.siddhant.backend.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController @RequestMapping("/api/resumes") @RequiredArgsConstructor
public class ResumeController {
    private final ResumeService service;
    @PostMapping("/me") public Map<String,Object> upload(@RequestParam("file") MultipartFile file, Authentication auth) {
        Resume r = service.upload(auth.getName(), file); return Map.of("id", r.getId(), "fileName", r.getOriginalName(), "size", r.getSize(), "uploadedAt", r.getUploadedAt());
    }
    @GetMapping("/me") public Map<String,Object> info(Authentication auth) {
        Resume r = service.getForUser(auth.getName()); return Map.of("id", r.getId(), "fileName", r.getOriginalName(), "size", r.getSize(), "uploadedAt", r.getUploadedAt());
    }
    @GetMapping("/me/download") public ResponseEntity<ByteArrayResource> download(Authentication auth) {
        Resume r = service.getForUser(auth.getName()); String name = URLEncoder.encode(r.getOriginalName(), StandardCharsets.UTF_8).replace("+", "%20");
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(r.getContentType())).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + name).body(new ByteArrayResource(service.download(auth.getName())));
    }
    @DeleteMapping("/me") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(Authentication auth) { service.delete(auth.getName()); }
}
