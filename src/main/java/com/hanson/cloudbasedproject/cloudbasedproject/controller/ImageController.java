package com.hanson.cloudbasedproject.cloudbasedproject.controller;

import com.hanson.cloudbasedproject.cloudbasedproject.dtos.ImageTransfer;
import com.hanson.cloudbasedproject.cloudbasedproject.service.ImageService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/")
    public String home(Model model, @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("images", imageService.home(page));
        return "gallery";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("files") @NotNull MultipartFile[] files) throws IOException {
        imageService.uploadFile(files);
        return "redirect:/";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long id) throws IOException {
        ImageTransfer imageData = imageService.downloadImage(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageData.getFileName() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageData.getImageByte());
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<InputStreamResource> renderImage(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok().body(imageService.renderImage(id));
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws IOException {
        imageService.deleteImage(id);
        return "redirect:/";
    }
}
