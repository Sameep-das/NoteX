package com.notex.repository;

import com.notex.model.entity.NoteLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoteLikeRepository extends JpaRepository<NoteLike, Long> {
    
    Optional<NoteLike> findByNoteIdAndUserId(Long noteId, Long userId);
    
    Boolean existsByNoteIdAndUserId(Long noteId, Long userId);
    
    Long countByNoteId(Long noteId);
    
    void deleteByNoteIdAndUserId(Long noteId, Long userId);
}