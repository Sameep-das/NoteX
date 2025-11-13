// package com.notex.service;

// import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Autowired;
// import com.notex.model.entity.Tag;
// import com.notex.repository.TagRepository;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class TagService {

//     @Autowired
//     private TagRepository tagRepository;

//     public List<Tag> getAllTags() {
//         return tagRepository.findAll();
//     }

//     public Optional<Tag> getTagById(Long id) {
//         return tagRepository.findById(id);
//     }

//     public Tag createTag(Tag tag) {
//         return tagRepository.save(tag);
//     }

//     public void deleteTag(Long id) {
//         tagRepository.deleteById(id);
//     }

//     public List<Tag> findTagsByName(String query) {
//         return tagRepository.findByNameContainingIgnoreCase(query);
//     }
// }

//New Code -
package com.notex.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import com.notex.model.entity.Tag;
import com.notex.model.dto.request.CreateTagRequest;
import com.notex.model.dto.response.TagResponse;
import com.notex.repository.TagRepository;
import com.notex.exception.ResourceNotFoundException;
import com.notex.exception.BadRequestException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    
    @Autowired
    private TagRepository tagRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Transactional
    public TagResponse createTag(CreateTagRequest request) {
        if (tagRepository.existsByName(request.getName())) {
            throw new BadRequestException("Tag with this name already exists");
        }
        
        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setCategory(request.getCategory());
        
        Tag savedTag = tagRepository.save(tag);
        return modelMapper.map(savedTag, TagResponse.class);
    }
    
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll()
            .stream()
            .map(tag -> modelMapper.map(tag, TagResponse.class))
            .collect(Collectors.toList());
    }
    
    public List<TagResponse> getTagsByCategory(String category) {
        return tagRepository.findByCategory(category)
            .stream()
            .map(tag -> modelMapper.map(tag, TagResponse.class))
            .collect(Collectors.toList());
    }
    
    public List<TagResponse> searchTags(String keyword) {
        return tagRepository.findByNameContainingIgnoreCase(keyword)
            .stream()
            .map(tag -> modelMapper.map(tag, TagResponse.class))
            .collect(Collectors.toList());
    }
    
    public TagResponse getTagById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        return modelMapper.map(tag, TagResponse.class);
    }
    
    @Transactional
    public void deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        tagRepository.delete(tag);
    }
}
