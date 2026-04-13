package com.hanoisurvey.api.interfaces.rest.settings.dto;

import com.hanoisurvey.api.domain.settings.SiteSetting;

import java.time.LocalDate;

public record SiteSettingResponse(
        Long id,
        String siteName,
        String companyNameEn,
        String shortName,
        String slogan,
        String logoPath,
        String footerLogoPath,
        String heroBannerPath,
        String companyIntro,
        String taxCode,
        String businessLicensePath,
        String licenseIssuedBy,
        LocalDate licenseIssuedDate,
        String officeAddress,
        String branchAddress,
        String hotline,
        String phone,
        String email,
        String website,
        String address,
        String mapEmbed,
        String workingHours,
        String facebookUrl,
        String zaloUrl,
        String youtubeUrl,
        String linkedinUrl,
        String footerText
) {
    public static SiteSettingResponse from(SiteSetting siteSetting) {
        return new SiteSettingResponse(
                siteSetting.id(),
                siteSetting.siteName(),
                siteSetting.companyNameEn(),
                siteSetting.shortName(),
                siteSetting.slogan(),
                siteSetting.logoPath(),
                siteSetting.footerLogoPath(),
                siteSetting.heroBannerPath(),
                siteSetting.companyIntro(),
                siteSetting.taxCode(),
                siteSetting.businessLicensePath(),
                siteSetting.licenseIssuedBy(),
                siteSetting.licenseIssuedDate(),
                siteSetting.officeAddress(),
                siteSetting.branchAddress(),
                siteSetting.hotline(),
                siteSetting.phone(),
                siteSetting.email(),
                siteSetting.website(),
                siteSetting.address(),
                siteSetting.mapEmbed(),
                siteSetting.workingHours(),
                siteSetting.facebookUrl(),
                siteSetting.zaloUrl(),
                siteSetting.youtubeUrl(),
                siteSetting.linkedinUrl(),
                siteSetting.footerText()
        );
    }
}
