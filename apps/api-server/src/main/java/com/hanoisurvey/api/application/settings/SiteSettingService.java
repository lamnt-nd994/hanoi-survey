package com.hanoisurvey.api.application.settings;

import com.hanoisurvey.api.domain.settings.SiteSetting;
import org.springframework.stereotype.Service;

@Service
public class SiteSettingService {

    private final SiteSettingRepositoryPort repository;

    public SiteSettingService(SiteSettingRepositoryPort repository) {
        this.repository = repository;
    }

    public SiteSetting getCurrent() {
        return repository.findFirst().orElseGet(() -> repository.save(defaultSetting()));
    }

    public SiteSetting update(SiteSettingCommand command) {
        SiteSetting current = repository.findFirst().orElseGet(() -> repository.save(defaultSetting()));
        return repository.save(new SiteSetting(
                current.id(),
                command.siteName(),
                command.companyNameEn(),
                command.companyTradeName(),
                command.shortName(),
                command.slogan(),
                command.logoPath(),
                command.footerLogoPath(),
                command.heroBannerPath(),
                command.companyIntro(),
                command.taxCode(),
                command.licenseNumber(),
                command.businessLicensePath(),
                command.licenseIssuedBy(),
                command.licenseIssuedDate(),
                command.officeAddress(),
                command.branchAddress(),
                command.hotline(),
                command.phone(),
                command.email(),
                command.website(),
                command.representativeName(),
                command.representativeTitle(),
                command.establishmentInfo(),
                command.address(),
                command.mapEmbed(),
                command.workingHours(),
                command.facebookUrl(),
                command.zaloUrl(),
                command.youtubeUrl(),
                command.linkedinUrl(),
                command.footerText()
        ));
    }

    private SiteSetting defaultSetting() {
        return new SiteSetting(
                null, // id
                "Hanoi Survey", // siteName
                null, // companyNameEn
                null, // companyTradeName
                null, // shortName
                null, // slogan
                null, // logoPath
                null, // footerLogoPath
                null, // heroBannerPath
                null, // companyIntro
                null, // taxCode
                null, // licenseNumber
                null, // businessLicensePath
                null, // licenseIssuedBy
                null, // licenseIssuedDate
                null, // officeAddress
                null, // branchAddress
                null, // hotline
                null, // phone
                null, // email
                null, // website
                null, // representativeName
                null, // representativeTitle
                null, // establishmentInfo
                null, // address
                null, // mapEmbed
                null, // workingHours
                null, // facebookUrl
                null, // zaloUrl
                null, // youtubeUrl
                null, // linkedinUrl
                null // footerText
        );
    }
}
