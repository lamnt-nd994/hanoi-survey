package com.hanoisurvey.api.infrastructure.persistence.settings;

import com.hanoisurvey.api.application.settings.SiteSettingRepositoryPort;
import com.hanoisurvey.api.domain.settings.SiteSetting;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SiteSettingRepositoryAdapter implements SiteSettingRepositoryPort {

    private final SiteSettingRepository repository;

    public SiteSettingRepositoryAdapter(SiteSettingRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<SiteSetting> findFirst() {
        return repository.findAll().stream().findFirst().map(this::toDomain);
    }

    @Override
    public SiteSetting save(SiteSetting siteSetting) {
        SiteSettingEntity entity = siteSetting.id() == null ? new SiteSettingEntity() : repository.findById(siteSetting.id()).orElseGet(SiteSettingEntity::new);
        entity.setId(siteSetting.id());
        entity.setSiteName(siteSetting.siteName());
        entity.setCompanyNameEn(siteSetting.companyNameEn());
        entity.setShortName(siteSetting.shortName());
        entity.setSlogan(siteSetting.slogan());
        entity.setLogoPath(siteSetting.logoPath());
        entity.setFooterLogoPath(siteSetting.footerLogoPath());
        entity.setHeroBannerPath(siteSetting.heroBannerPath());
        entity.setCompanyIntro(siteSetting.companyIntro());
        entity.setTaxCode(siteSetting.taxCode());
        entity.setLicenseNumber(siteSetting.licenseNumber());
        entity.setBusinessLicensePath(siteSetting.businessLicensePath());
        entity.setLicenseIssuedBy(siteSetting.licenseIssuedBy());
        entity.setLicenseIssuedDate(siteSetting.licenseIssuedDate());
        entity.setOfficeAddress(siteSetting.officeAddress());
        entity.setBranchAddress(siteSetting.branchAddress());
        entity.setHotline(siteSetting.hotline());
        entity.setPhone(siteSetting.phone());
        entity.setEmail(siteSetting.email());
        entity.setWebsite(siteSetting.website());
        entity.setAddress(siteSetting.address());
        entity.setMapEmbed(siteSetting.mapEmbed());
        entity.setWorkingHours(siteSetting.workingHours());
        entity.setFacebookUrl(siteSetting.facebookUrl());
        entity.setZaloUrl(siteSetting.zaloUrl());
        entity.setYoutubeUrl(siteSetting.youtubeUrl());
        entity.setLinkedinUrl(siteSetting.linkedinUrl());
        entity.setFooterText(siteSetting.footerText());
        return toDomain(repository.save(entity));
    }

    private SiteSetting toDomain(SiteSettingEntity entity) {
        return new SiteSetting(
                entity.getId(),
                entity.getSiteName(),
                entity.getCompanyNameEn(),
                entity.getShortName(),
                entity.getSlogan(),
                entity.getLogoPath(),
                entity.getFooterLogoPath(),
                entity.getHeroBannerPath(),
                entity.getCompanyIntro(),
                entity.getTaxCode(),
                entity.getLicenseNumber(),
                entity.getBusinessLicensePath(),
                entity.getLicenseIssuedBy(),
                entity.getLicenseIssuedDate(),
                entity.getOfficeAddress(),
                entity.getBranchAddress(),
                entity.getHotline(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getWebsite(),
                entity.getAddress(),
                entity.getMapEmbed(),
                entity.getWorkingHours(),
                entity.getFacebookUrl(),
                entity.getZaloUrl(),
                entity.getYoutubeUrl(),
                entity.getLinkedinUrl(),
                entity.getFooterText()
        );
    }
}
