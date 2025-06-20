package com.hanson.cloudbasedproject.cloudbasedproject.repository;

import com.hanson.cloudbasedproject.cloudbasedproject.entities.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, Long> {
}
