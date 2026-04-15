package com.hanoisurvey.api.application.settings;

import java.time.LocalDate;

public record SiteSettingCommand(
        String siteName,
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
        String representativeName,
        String representativeTitle,
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
