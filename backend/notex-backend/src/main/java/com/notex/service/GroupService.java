// New Code -

package com.notex.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import com.notex.model.entity.Group;
import com.notex.model.entity.GroupMember;
import com.notex.model.entity.User;
import com.notex.model.dto.request.CreateGroupRequest;
import com.notex.model.dto.request.UpdateGroupRequest;
import com.notex.model.dto.request.AddGroupMemberRequest;
import com.notex.model.dto.response.GroupResponse;
import com.notex.model.dto.response.GroupMemberResponse;
import com.notex.model.dto.response.UserResponse;
import com.notex.model.enums.GroupRole;
import com.notex.repository.GroupRepository;
import com.notex.repository.GroupMemberRepository;
import com.notex.repository.UserRepository;
import com.notex.exception.ResourceNotFoundException;
import com.notex.exception.UnauthorizedException;
import com.notex.exception.BadRequestException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Transactional
    public GroupResponse createGroup(CreateGroupRequest request, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Group group = new Group();
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setCreatedBy(user);
        group.setIsPrivate(request.getIsPrivate() != null ? request.getIsPrivate() : false);
        
        Group savedGroup = groupRepository.save(group);
        
        GroupMember member = new GroupMember();
        member.setGroup(savedGroup);
        member.setUser(user);
        member.setRole(GroupRole.ADMIN);
        groupMemberRepository.save(member);
        
        return convertToResponse(savedGroup);
    }
    
    @Transactional
    public GroupResponse updateGroup(Long groupId, UpdateGroupRequest request, Long userId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        
        GroupMember member = groupMemberRepository.findByGroupIdAndUserId(groupId, userId)
            .orElseThrow(() -> new UnauthorizedException("You are not a member of this group"));
        
        if (member.getRole() != GroupRole.ADMIN) {
            throw new UnauthorizedException("Only admins can update group details");
        }
        
        if (request.getName() != null) {
            group.setName(request.getName());
        }
        if (request.getDescription() != null) {
            group.setDescription(request.getDescription());
        }
        if (request.getIsPrivate() != null) {
            group.setIsPrivate(request.getIsPrivate());
        }
        
        Group updatedGroup = groupRepository.save(group);
        return convertToResponse(updatedGroup);
    }
    
    @Transactional
    public void deleteGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        
        if (!group.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedException("Only group creator can delete the group");
        }
        
        groupRepository.delete(group);
    }
    
    public GroupResponse getGroupById(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        
        if (group.getIsPrivate() && !groupMemberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            throw new UnauthorizedException("This is a private group");
        }
        
        return convertToResponse(group);
    }
    
    public List<GroupResponse> getUserGroups(Long userId) {
        return groupMemberRepository.findByUserId(userId)
            .stream()
            .map(member -> convertToResponse(member.getGroup()))
            .collect(Collectors.toList());
    }
    
    public List<GroupResponse> getAllPublicGroups() {
        return groupRepository.findPublicGroups()
            .stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void addMember(Long groupId, AddGroupMemberRequest request, Long currentUserId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        
        GroupMember currentMember = groupMemberRepository.findByGroupIdAndUserId(groupId, currentUserId)
            .orElseThrow(() -> new UnauthorizedException("You are not a member of this group"));
        
        if (currentMember.getRole() != GroupRole.ADMIN && currentMember.getRole() != GroupRole.MODERATOR) {
            throw new UnauthorizedException("Only admins and moderators can add members");
        }
        
        if (groupMemberRepository.existsByGroupIdAndUserId(groupId, request.getUserId())) {
            throw new BadRequestException("User is already a member of this group");
        }
        
        User newUser = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        GroupMember newMember = new GroupMember();
        newMember.setGroup(group);
        newMember.setUser(newUser);
        newMember.setRole(request.getRole() != null ? request.getRole() : GroupRole.MEMBER);
        
        groupMemberRepository.save(newMember);
    }
    
    @Transactional
    public void removeMember(Long groupId, Long memberUserId, Long currentUserId) {
        GroupMember currentMember = groupMemberRepository.findByGroupIdAndUserId(groupId, currentUserId)
            .orElseThrow(() -> new UnauthorizedException("You are not a member of this group"));
        
        if (currentMember.getRole() != GroupRole.ADMIN) {
            if (!memberUserId.equals(currentUserId)) {
                throw new UnauthorizedException("Only admins can remove other members");
            }
        }
        
        groupMemberRepository.deleteByGroupIdAndUserId(groupId, memberUserId);
    }
    
    public List<GroupMemberResponse> getGroupMembers(Long groupId) {
        return groupMemberRepository.findByGroupId(groupId)
            .stream()
            .map(this::convertToMemberResponse)
            .collect(Collectors.toList());
    }
    
    private GroupResponse convertToResponse(Group group) {
        GroupResponse response = new GroupResponse();
        response.setId(group.getId());
        response.setName(group.getName());
        response.setDescription(group.getDescription());
        response.setCreatedBy(modelMapper.map(group.getCreatedBy(), UserResponse.class));
        response.setIsPrivate(group.getIsPrivate());
        response.setGroupPictureUrl(group.getGroupPictureUrl());
        response.setMemberCount(groupMemberRepository.countByGroupId(group.getId()).intValue());
        response.setCreatedAt(group.getCreatedAt());
        response.setUpdatedAt(group.getUpdatedAt());
        
        List<GroupMemberResponse> members = groupMemberRepository.findByGroupId(group.getId())
            .stream()
            .map(this::convertToMemberResponse)
            .collect(Collectors.toList());
        response.setMembers(members);
        
        return response;
    }
    
    private GroupMemberResponse convertToMemberResponse(GroupMember member) {
        GroupMemberResponse response = new GroupMemberResponse();
        response.setId(member.getId());
        response.setUser(modelMapper.map(member.getUser(), UserResponse.class));
        response.setRole(member.getRole());
        response.setJoinedAt(member.getJoinedAt());
        return response;
    }
}
