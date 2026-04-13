package com.hanoisurvey.api.application.posts;

import com.hanoisurvey.api.domain.shared.ContentStatus;

public record PostCommand(Long categoryId, String title, String slug, String excerpt, String content, String coverImagePath, ContentStatus status) {
}
