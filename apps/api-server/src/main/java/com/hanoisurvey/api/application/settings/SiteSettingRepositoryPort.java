package com.hanoisurvey.api.application.settings;

import com.hanoisurvey.api.domain.settings.SiteSetting;

import java.util.Optional;

public interface SiteSettingRepositoryPort {
    Optional<SiteSetting> findFirst();

    SiteSetting save(SiteSetting siteSetting);
}
