package com.hanson.cloudbasedproject.cloudbasedproject.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ImageMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filename;
    private String url;
    private LocalDateTime uploadTime;

    public ImageMetadata(String filename, String url) {
        this.filename = filename;
        this.url = url;
        this.uploadTime = LocalDateTime.now();
    }
}
