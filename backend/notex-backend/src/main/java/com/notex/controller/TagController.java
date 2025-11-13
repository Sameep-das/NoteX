package com.notex.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.Valid;

import com.notex.service.TagService;
import com.notex.model.dto.request.CreateTagRequest;
import com.notex.model.dto.response.TagResponse;
import com.notex.model.dto.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/tags")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class TagController {
    
    @Autowired
    private TagService tagService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<TagResponse>> createTag(
            @Valid @RequestBody CreateTagRequest request) {
        TagResponse tag = tagService.createTag(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tag created", tag));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResponse>>> getAllTags() {
        List<TagResponse> tags = tagService.getAllTags();
        return ResponseEntity.ok(new ApiResponse<>(true, "Tags retrieved", tags));
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<TagResponse>>> getTagsByCategory(
            @PathVariable String category) {
        List<TagResponse> tags = tagService.getTagsByCategory(category);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tags retrieved", tags));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TagResponse>>> searchTags(
            @RequestParam String keyword) {
        List<TagResponse> tags = tagService.searchTags(keyword);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tags retrieved", tags));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagResponse>> getTagById(@PathVariable Long id) {
        TagResponse tag = tagService.getTagById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tag retrieved", tag));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tag deleted"));
    }
}