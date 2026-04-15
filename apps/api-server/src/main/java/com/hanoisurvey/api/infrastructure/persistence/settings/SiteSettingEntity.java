package com.hanoisurvey.api.infrastructure.persistence.settings;

import com.hanoisurvey.api.infrastructure.persistence.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "site_settings")
public class SiteSettingEntity extends BaseEntity {

    @Column(name = "site_name", nullable = false)
    private String siteName;

    @Column(name = "company_name_en")
    private String companyNameEn;

    @Column(name = "short_name")
    private String shortName;

    private String slogan;

    @Column(name = "logo_path")
    private String logoPath;

    @Column(name = "footer_logo_path")
    private String footerLogoPath;

    @Column(name = "hero_banner_path")
    private String heroBannerPath;

    @Column(name = "company_intro", columnDefinition = "TEXT")
    private String companyIntro;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "business_license_path")
    private String businessLicensePath;

    @Column(name = "license_issued_by")
    private String licenseIssuedBy;

    @Column(name = "license_issued_date")
    private java.time.LocalDate licenseIssuedDate;

    @Column(name = "office_address")
    private String officeAddress;

    @Column(name = "branch_address")
    private String branchAddress;

    private String hotline;

    private String phone;

    private String email;

    private String website;

    @Column(name = "representative_name")
    private String representativeName;

    @Column(name = "representative_title")
    private String representativeTitle;

    private String address;

    @Column(name = "map_embed", columnDefinition = "TEXT")
    private String mapEmbed;

    @Column(name = "working_hours")
    private String workingHours;

    @Column(name = "facebook_url")
    private String facebookUrl;

    @Column(name = "zalo_url")
    private String zaloUrl;

    @Column(name = "youtube_url")
    private String youtubeUrl;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "footer_text", columnDefinition = "TEXT")
    private String footerText;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getCompanyNameEn() {
        return companyNameEn;
    }

    public void setCompanyNameEn(String companyNameEn) {
        this.companyNameEn = companyNameEn;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getFooterLogoPath() {
        return footerLogoPath;
    }

    public void setFooterLogoPath(String footerLogoPath) {
        this.footerLogoPath = footerLogoPath;
    }

    public String getHeroBannerPath() {
        return heroBannerPath;
    }

    public void setHeroBannerPath(String heroBannerPath) {
        this.heroBannerPath = heroBannerPath;
    }

    public String getCompanyIntro() {
        return companyIntro;
    }

    public void setCompanyIntro(String companyIntro) {
        this.companyIntro = companyIntro;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getBusinessLicensePath() {
        return businessLicensePath;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setBusinessLicensePath(String businessLicensePath) {
        this.businessLicensePath = businessLicensePath;
    }

    public String getLicenseIssuedBy() {
        return licenseIssuedBy;
    }

    public void setLicenseIssuedBy(String licenseIssuedBy) {
        this.licenseIssuedBy = licenseIssuedBy;
    }

    public java.time.LocalDate getLicenseIssuedDate() {
        return licenseIssuedDate;
    }

    public void setLicenseIssuedDate(java.time.LocalDate licenseIssuedDate) {
        this.licenseIssuedDate = licenseIssuedDate;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getRepresentativeTitle() {
        return representativeTitle;
    }

    public void setRepresentativeTitle(String representativeTitle) {
        this.representativeTitle = representativeTitle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMapEmbed() {
        return mapEmbed;
    }

    public void setMapEmbed(String mapEmbed) {
        this.mapEmbed = mapEmbed;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getZaloUrl() {
        return zaloUrl;
    }

    public void setZaloUrl(String zaloUrl) {
        this.zaloUrl = zaloUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }
}
