// package com.notex.service;

// import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Autowired;
// import com.notex.model.entity.Note;
// import com.notex.repository.NoteRepository;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class NoteService {

//     @Autowired
//     private NoteRepository noteRepository;

//     public List<Note> listNotesByUser(String username, int page, int size) {
//         // assumes repository has method findByCreatedByUsername
//         return noteRepository.findByCreatedByUsername(username);
//     }

//     public Optional<Note> getNoteById(Long id) {
//         return noteRepository.findById(id);
//     }

//     public Note createNote(Note note, String username) {
//         // set creator relationship before save if needed
//         return noteRepository.save(note);
//     }

//     public Optional<Note> updateNote(Long id, Note note) {
//         return noteRepository.findById(id).map(existing -> {
//             existing.setTitle(note.getTitle());
//             existing.setContent(note.getContent());
//             existing.setFileUrl(note.getFileUrl());
//             return noteRepository.save(existing);
//         });
//     }

//     public void deleteNote(Long id) {
//         noteRepository.deleteById(id);
//     }

//     public List<Note> searchNotes(String query, int page, int size) {
//         return noteRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
//     }
// }

//New Code -

package com.notex.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.modelmapper.ModelMapper;

import com.notex.model.entity.Note;
import com.notex.model.entity.User;
import com.notex.model.entity.Group;
import com.notex.model.entity.Tag;
import com.notex.model.dto.request.CreateNoteRequest;
import com.notex.model.dto.request.UpdateNoteRequest;
import com.notex.model.dto.response.NoteResponse;
import com.notex.model.dto.response.PageResponse;
import com.notex.model.dto.response.UserResponse;
import com.notex.model.enums.Visibility;
import com.notex.repository.NoteRepository;
import com.notex.repository.UserRepository;
import com.notex.repository.GroupRepository;
import com.notex.repository.TagRepository;
import com.notex.exception.ResourceNotFoundException;
import com.notex.exception.UnauthorizedException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NoteService {
    
    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private TagRepository tagRepository;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Transactional
    public NoteResponse createNote(CreateNoteRequest request, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setNoteType(request.getNoteType());
        note.setVisibility(request.getVisibility() != null ? request.getVisibility() : Visibility.PRIVATE);
        note.setCreatedBy(user);
        
        if (request.getGroupId() != null) {
            Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
            note.setGroup(group);
        }
        
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + tagId));
                tags.add(tag);
            }
            note.setTags(tags);
        }
        
        Note savedNote = noteRepository.save(note);
        return convertToResponse(savedNote);
    }
    
    @Transactional
    public NoteResponse updateNote(Long noteId, UpdateNoteRequest request, Long userId) {
        Note note = noteRepository.findById(noteId)
            .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        
        if (!note.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to update this note");
        }
        
        if (request.getTitle() != null) {
            note.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            note.setContent(request.getContent());
        }
        if (request.getVisibility() != null) {
            note.setVisibility(request.getVisibility());
        }
        if (request.getGroupId() != null) {
            Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
            note.setGroup(group);
        }
        if (request.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>();
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + tagId));
                tags.add(tag);
            }
            note.setTags(tags);
        }
        
        Note updatedNote = noteRepository.save(note);
        return convertToResponse(updatedNote);
    }
    
    @Transactional
    public void deleteNote(Long noteId, Long userId) {
        Note note = noteRepository.findById(noteId)
            .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        
        if (!note.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to delete this note");
        }
        
        if (note.getFileUrl() != null) {
            fileStorageService.deleteFile(note.getFileUrl());
        }
        
        noteRepository.delete(note);
    }
    
    @Transactional
    public NoteResponse getNoteById(Long noteId, Long userId) {
        Note note = noteRepository.findById(noteId)
            .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        
        if (note.getVisibility() == Visibility.PRIVATE && !note.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to view this note");
        }
        
        noteRepository.incrementViewCount(noteId);
        return convertToResponse(note);
    }
    
    public PageResponse<NoteResponse> getAllNotes(int page, int size, String tag, String search, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Note> notesPage;
        
        if (search != null && !search.isEmpty()) {
            notesPage = noteRepository.searchPublicNotes(search, pageable);
        } else if (tag != null) {
            Tag tagEntity = tagRepository.findByName(tag)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
            notesPage = noteRepository.findByTagId(tagEntity.getId(), pageable);
        } else {
            notesPage = noteRepository.findByVisibility(Visibility.PUBLIC, pageable);
        }
        
        return convertToPageResponse(notesPage);
    }
    
    public List<NoteResponse> getUserNotes(Long userId) {
        return noteRepository.findByCreatedById(userId)
            .stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    public PageResponse<NoteResponse> getGroupNotes(Long groupId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Note> notesPage = noteRepository.findByGroupId(groupId, pageable);
        return convertToPageResponse(notesPage);
    }
    
    @Transactional
    public String uploadNoteFile(Long noteId, MultipartFile file, Long userId) {
        Note note = noteRepository.findById(noteId)
            .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        
        if (!note.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to upload file for this note");
        }
        
        String fileUrl = fileStorageService.storeFile(file);
        note.setFileUrl(fileUrl);
        noteRepository.save(note);
        
        return fileUrl;
    }
    
    private NoteResponse convertToResponse(Note note) {
        NoteResponse response = new NoteResponse();
        response.setId(note.getId());
        response.setTitle(note.getTitle());
        response.setContent(note.getContent());
        response.setNoteType(note.getNoteType());
        response.setVisibility(note.getVisibility());
        response.setFileUrl(note.getFileUrl());
        response.setThumbnailUrl(note.getThumbnailUrl());
        response.setViewCount(note.getViewCount());
        response.setLikeCount(note.getLikeCount());
        response.setCreatedBy(modelMapper.map(note.getCreatedBy(), UserResponse.class));
        
        if (note.getGroup() != null) {
            response.setGroupId(note.getGroup().getId());
            response.setGroupName(note.getGroup().getName());
        }
        
        Set<String> tagNames = note.getTags().stream()
            .map(Tag::getName)
            .collect(Collectors.toSet());
        response.setTags(tagNames);
        
        response.setCreatedAt(note.getCreatedAt());
        response.setUpdatedAt(note.getUpdatedAt());
        
        return response;
    }
    
    private PageResponse<NoteResponse> convertToPageResponse(Page<Note> notesPage) {
        List<NoteResponse> content = notesPage.getContent()
            .stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        
        PageResponse<NoteResponse> response = new PageResponse<>();
        response.setContent(content);
        response.setPageNumber(notesPage.getNumber());
        response.setPageSize(notesPage.getSize());
        response.setTotalElements(notesPage.getTotalElements());
        response.setTotalPages(notesPage.getTotalPages());
        response.setLast(notesPage.isLast());
        response.setFirst(notesPage.isFirst());
        
        return response;
    }
}