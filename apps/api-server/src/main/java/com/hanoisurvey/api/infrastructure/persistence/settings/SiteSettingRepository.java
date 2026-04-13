package com.hanoisurvey.api.infrastructure.persistence.settings;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteSettingRepository extends JpaRepository<SiteSettingEntity, Long> {
}
