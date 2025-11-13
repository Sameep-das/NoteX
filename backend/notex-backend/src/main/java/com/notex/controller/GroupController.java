package com.notex.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import jakarta.validation.Valid;

import com.notex.service.GroupService;
import com.notex.model.dto.request.CreateGroupRequest;
import com.notex.model.dto.request.UpdateGroupRequest;
import com.notex.model.dto.request.AddGroupMemberRequest;
import com.notex.model.dto.response.GroupResponse;
import com.notex.model.dto.response.GroupMemberResponse;
import com.notex.model.dto.response.ApiResponse;
import com.notex.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class GroupController {
    
    @Autowired
    private GroupService groupService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<GroupResponse>> createGroup(
            @Valid @RequestBody CreateGroupRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        GroupResponse group = groupService.createGroup(request, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Group created", group));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupResponse>> getGroupById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        GroupResponse group = groupService.getGroupById(id, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Group retrieved", group));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupResponse>> updateGroup(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGroupRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        GroupResponse group = groupService.updateGroup(id, request, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Group updated", group));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        groupService.deleteGroup(id, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Group deleted"));
    }
    
    @GetMapping("/my-groups")
    public ResponseEntity<ApiResponse<List<GroupResponse>>> getMyGroups(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<GroupResponse> groups = groupService.getUserGroups(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Your groups retrieved", groups));
    }
    
    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<GroupResponse>>> getPublicGroups() {
        List<GroupResponse> groups = groupService.getAllPublicGroups();
        return ResponseEntity.ok(new ApiResponse<>(true, "Public groups retrieved", groups));
    }
    
    @PostMapping("/{id}/members")
    public ResponseEntity<ApiResponse<Void>> addMember(
            @PathVariable Long id,
            @Valid @RequestBody AddGroupMemberRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        groupService.addMember(id, request, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Member added to group"));
    }
    
    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable Long id,
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        groupService.removeMember(id, userId, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Member removed from group"));
    }
    
    @GetMapping("/{id}/members")
    public ResponseEntity<ApiResponse<List<GroupMemberResponse>>> getGroupMembers(
            @PathVariable Long id) {
        List<GroupMemberResponse> members = groupService.getGroupMembers(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Group members retrieved", members));
    }
}
