package com.hanoisurvey.api.interfaces.rest.settings;

import com.hanoisurvey.api.application.settings.SiteSettingService;
import com.hanoisurvey.api.application.settings.SiteSettingCommand;
import com.hanoisurvey.api.interfaces.rest.settings.dto.SiteSettingRequest;
import com.hanoisurvey.api.interfaces.rest.settings.dto.SiteSettingResponse;
import com.hanoisurvey.api.interfaces.rest.shared.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SiteSettingController {

    private final SiteSettingService service;

    public SiteSettingController(SiteSettingService service) {
        this.service = service;
    }

    @GetMapping("/api/public/v1/site-settings")
    public ApiResponse<SiteSettingResponse> publicSettings() {
        return ApiResponse.ok(SiteSettingResponse.from(service.getCurrent()));
    }

    @GetMapping("/api/admin/v1/site-settings")
    public ApiResponse<SiteSettingResponse> adminSettings() {
        return ApiResponse.ok(SiteSettingResponse.from(service.getCurrent()));
    }

    @PutMapping("/api/admin/v1/site-settings")
    public ApiResponse<SiteSettingResponse> update(@Valid @RequestBody SiteSettingRequest request) {
        return ApiResponse.ok(SiteSettingResponse.from(service.update(new SiteSettingCommand(
                request.siteName(),
                request.companyNameEn(),
                request.companyTradeName(),
                request.shortName(),
                request.slogan(),
                request.logoPath(),
                request.footerLogoPath(),
                request.heroBannerPath(),
                request.companyIntro(),
                request.taxCode(),
                request.licenseNumber(),
                request.businessLicensePath(),
                request.licenseIssuedBy(),
                request.licenseIssuedDate(),
                request.officeAddress(),
                request.branchAddress(),
                request.hotline(),
                request.phone(),
                request.email(),
                request.website(),
                request.representativeName(),
                request.representativeTitle(),
                request.establishmentInfo(),
                request.address(),
                request.mapEmbed(),
                request.workingHours(),
                request.facebookUrl(),
                request.zaloUrl(),
                request.youtubeUrl(),
                request.linkedinUrl(),
                request.footerText()
        ))));
    }
}
