// package com.notex.repository;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import com.notex.model.entity.Tag;

// @Repository
// public interface TagRepository extends JpaRepository<Tag, Long> {
    
//     /**
//      * Find tag by name
//      */
//     Optional<Tag> findByName(String name);
    
//     /**
//      * Find tags by category
//      */
//     List<Tag> findByCategory(String category);
    
//     /**
//      * Check if tag exists
//      */
//     Boolean existsByName(String name);
    
//     /**
//      * Find tags by name containing the query string, case insensitive
//      */
//     List<Tag> findByNameContainingIgnoreCase(String query);
// }

package com.notex.repository;

import com.notex.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    
    Optional<Tag> findByName(String name);
    
    List<Tag> findByCategory(String category);
    
    Boolean existsByName(String name);
    
    List<Tag> findByNameContainingIgnoreCase(String keyword);
}