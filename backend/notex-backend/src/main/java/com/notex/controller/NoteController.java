package com.notex.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import com.notex.service.NoteService;
import com.notex.model.dto.request.CreateNoteRequest;
import com.notex.model.dto.request.UpdateNoteRequest;
import com.notex.model.dto.response.NoteResponse;
import com.notex.model.dto.response.PageResponse;
import com.notex.model.dto.response.ApiResponse;
import com.notex.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/notes")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class NoteController {
    
    @Autowired
    private NoteService noteService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<NoteResponse>> createNote(
            @Valid @RequestBody CreateNoteRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        NoteResponse note = noteService.createNote(request, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Note created", note));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NoteResponse>> getNoteById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        NoteResponse note = noteService.getNoteById(id, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Note retrieved", note));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NoteResponse>> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNoteRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        NoteResponse note = noteService.updateNote(id, request, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Note updated", note));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNote(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        noteService.deleteNote(id, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Note deleted"));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NoteResponse>>> getAllNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String search,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        PageResponse<NoteResponse> notes = noteService.getAllNotes(page, size, tag, search, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Notes retrieved", notes));
    }
    
    @GetMapping("/my-notes")
    public ResponseEntity<ApiResponse<List<NoteResponse>>> getMyNotes(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<NoteResponse> notes = noteService.getUserNotes(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Your notes retrieved", notes));
    }
    
    @GetMapping("/group/{groupId}")
    public ResponseEntity<ApiResponse<PageResponse<NoteResponse>>> getGroupNotes(
            @PathVariable Long groupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<NoteResponse> notes = noteService.getGroupNotes(groupId, page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Group notes retrieved", notes));
    }
    
    @PostMapping("/{id}/upload")
    public ResponseEntity<ApiResponse<String>> uploadNoteFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        String fileUrl = noteService.uploadNoteFile(id, file, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "File uploaded", fileUrl));
    }
}