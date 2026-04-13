package com.hanoisurvey.api.application.pages;

import com.hanoisurvey.api.domain.shared.ContentStatus;

public record PageCommand(
        String title,
        String slug,
        String summary,
        String content,
        String templateCode,
        String contentJson,
        ContentStatus status
) {
}
