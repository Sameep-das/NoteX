// package com.notex.repository;

// import com.notex.model.entity.Note;
// import com.notex.model.enums.Visibility;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;

// import java.util.List;

// /**
//  * NoteRepository - Database operations for Note entity
//  */
// @Repository
// public interface NoteRepository extends JpaRepository<Note, Long> {
    
//     /**
//      * Find all notes by creator
//      * SQL: SELECT * FROM notes WHERE created_by = ?
//      */
//     List<Note> findByCreatedById(Long userId);
    
//     /**
//      * Find all notes in a group
//      * SQL: SELECT * FROM notes WHERE group_id = ?
//      */
//     List<Note> findByGroupId(Long groupId);
    
//     /**
//      * Find public notes (paginated)
//      * Page and Pageable handle pagination automatically
//      * SQL: SELECT * FROM notes WHERE visibility = 'PUBLIC'
//      */
//     Page<Note> findByVisibility(Visibility visibility, Pageable pageable);
    
//     /**
//      * Custom query to search notes by title or content
//      * @Query lets you write custom JPQL (Java Persistence Query Language)
//      * 
//      * %:keyword% means "contains keyword" (like SQL LIKE)
//      */
//     @Query("SELECT n FROM Note n WHERE " +
//            "n.visibility = 'PUBLIC' AND " +
//            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
//     Page<Note> searchPublicNotes(@Param("keyword") String keyword, Pageable pageable);
    
//     /**
//      * Find notes by tag
//      * This joins note_tags table
//      */
//     @Query("SELECT DISTINCT n FROM Note n JOIN n.tags t WHERE t.id = :tagId AND n.visibility = 'PUBLIC'")
//     Page<Note> findByTagId(@Param("tagId") Long tagId, Pageable pageable);

//     // find notes created by a user (assumes Note.createdBy -> User with username property)
//     List<Note> findByCreatedByUsername(String username);

//     // simple search helpers used by service
//     List<Note> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
// }

//New Code -

package com.notex.repository;

import com.notex.model.entity.Note;
import com.notex.model.enums.Visibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    
    List<Note> findByCreatedById(Long userId);
    
    Page<Note> findByCreatedById(Long userId, Pageable pageable);
    
    List<Note> findByGroupId(Long groupId);
    
    Page<Note> findByGroupId(Long groupId, Pageable pageable);
    
    Page<Note> findByVisibility(Visibility visibility, Pageable pageable);
    
    @Query("SELECT n FROM Note n WHERE n.visibility = 'PUBLIC' AND " +
           "(LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Note> searchPublicNotes(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT DISTINCT n FROM Note n JOIN n.tags t WHERE t.id = :tagId AND n.visibility = 'PUBLIC'")
    Page<Note> findByTagId(@Param("tagId") Long tagId, Pageable pageable);
    
    @Modifying
    @Query("UPDATE Note n SET n.viewCount = n.viewCount + 1 WHERE n.id = :noteId")
    void incrementViewCount(@Param("noteId") Long noteId);
    
    @Modifying
    @Query("UPDATE Note n SET n.likeCount = n.likeCount + 1 WHERE n.id = :noteId")
    void incrementLikeCount(@Param("noteId") Long noteId);
    
    @Modifying
    @Query("UPDATE Note n SET n.likeCount = n.likeCount - 1 WHERE n.id = :noteId AND n.likeCount > 0")
    void decrementLikeCount(@Param("noteId") Long noteId);
}