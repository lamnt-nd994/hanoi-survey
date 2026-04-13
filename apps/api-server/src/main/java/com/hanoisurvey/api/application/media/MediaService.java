package com.hanoisurvey.api.application.media;

import com.hanoisurvey.api.domain.media.MediaFile;
import com.hanoisurvey.api.domain.shared.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Set;
import java.util.List;
import java.util.UUID;

@Service
public class MediaService {

    private static final long MAX_SIZE = 10 * 1024 * 1024;
    private static final Set<String> SUPPORTED_DOCUMENT_CONTENT_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "text/plain"
    );

    private final MediaFileRepositoryPort repository;
    private final Path uploadRoot;
    private final String uploadPublicPrefix;

    public MediaService(
            MediaFileRepositoryPort repository,
            @Value("${app.upload-dir}") String uploadDir,
            @Value("${app.upload-public-prefix:uploads}") String uploadPublicPrefix
    ) {
        this.repository = repository;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.uploadPublicPrefix = normalizePublicPrefix(uploadPublicPrefix);
    }

    public List<MediaFile> all() {
        return repository.findAll();
    }

    public MediaFile upload(MultipartFile file, String altText) {
        validate(file);

        try {
            String cleanedFileName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());
            String contentType = file.getContentType();
            String extension = "";
            int extIndex = cleanedFileName.lastIndexOf('.');
            if (extIndex >= 0) {
                extension = cleanedFileName.substring(extIndex);
            }

            String contentFolder = resolveContentFolder(contentType);
            String dailySegment = LocalDate.now().toString();
            Path dailyFolder = uploadRoot.resolve(contentFolder).resolve(dailySegment);
            Files.createDirectories(dailyFolder);

            String newFileName = UUID.randomUUID() + extension;
            Path destination = dailyFolder.resolve(newFileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            String publicPath = uploadPublicPrefix + "/" + contentFolder + "/" + dailySegment + "/" + newFileName;

            return repository.save(new MediaFile(null, cleanedFileName, contentType, file.getSize(), publicPath, altText, null));
        } catch (IOException exception) {
            throw new IllegalArgumentException("Upload thất bại: " + exception.getMessage());
        }
    }

    public void delete(Long id) {
        MediaFile entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy tệp"));

        try {
            Files.deleteIfExists(resolveStoredPath(entity.storagePath()));
        } catch (IOException ignored) {
        }
        repository.deleteById(id);
    }

    private void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Tệp upload rỗng");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("Tệp vượt quá 10MB");
        }
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !SUPPORTED_DOCUMENT_CONTENT_TYPES.contains(contentType))) {
            throw new IllegalArgumentException("Chỉ hỗ trợ ảnh hoặc tài liệu phổ biến");
        }
    }

    private String resolveContentFolder(String contentType) {
        if (contentType != null && contentType.startsWith("image/")) {
            return "images";
        }
        return "documents";
    }

    private Path resolveStoredPath(String storedPath) {
        if (storedPath == null || storedPath.isBlank()) {
            return uploadRoot;
        }

        String normalized = storedPath.replace('\\', '/');
        String prefix = uploadPublicPrefix + "/";
        if (normalized.startsWith(prefix)) {
            String relativePath = normalized.substring(prefix.length());
            return uploadRoot.resolve(relativePath).normalize();
        }

        return Paths.get(storedPath).toAbsolutePath().normalize();
    }

    private String normalizePublicPrefix(String value) {
        return value.replaceAll("^/+|/+$", "");
    }

}
