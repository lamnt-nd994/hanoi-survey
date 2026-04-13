package com.hanoisurvey.api.interfaces.rest.media.admin;

import com.hanoisurvey.api.application.media.MediaService;
import com.hanoisurvey.api.interfaces.rest.media.dto.MediaFileResponse;
import com.hanoisurvey.api.interfaces.rest.shared.ApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/media")
public class MediaAdminController {

    private final MediaService service;

    public MediaAdminController(MediaService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<MediaFileResponse>> all() {
        return ApiResponse.ok(service.all().stream().map(MediaFileResponse::from).toList());
    }

    @PostMapping("/upload")
    public ApiResponse<MediaFileResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String altText
    ) {
        return ApiResponse.ok(MediaFileResponse.from(service.upload(file, altText)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok("Deleted");
    }
}
