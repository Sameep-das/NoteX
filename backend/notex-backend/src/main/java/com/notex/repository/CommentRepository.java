package com.notex.repository;

import com.notex.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByNoteIdOrderByCreatedAtDesc(Long noteId);
    
    List<Comment> findByNoteIdAndParentCommentIsNullOrderByCreatedAtDesc(Long noteId);
    
    List<Comment> findByParentCommentIdOrderByCreatedAtAsc(Long parentCommentId);
    
    Long countByNoteId(Long noteId);
}