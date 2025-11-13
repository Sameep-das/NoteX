package com.notex.repository;

import com.notex.model.entity.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    
    List<Template> findByCreatedById(Long userId);
    
    Page<Template> findByIsPublicTrue(Pageable pageable);
    
    @Modifying
    @Query("UPDATE Template t SET t.usageCount = t.usageCount + 1 WHERE t.id = :templateId")
    void incrementUsageCount(@Param("templateId") Long templateId);
}