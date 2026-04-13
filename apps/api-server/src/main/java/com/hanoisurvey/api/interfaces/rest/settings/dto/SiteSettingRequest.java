package com.hanoisurvey.api.interfaces.rest.settings.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record SiteSettingRequest(
        @NotBlank String siteName,
        String companyNameEn,
        String shortName,
        String slogan,
        String logoPath,
        String footerLogoPath,
        String heroBannerPath,
        String companyIntro,
        String taxCode,
        String licenseNumber,
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
}
