package com.hanoisurvey.api.infrastructure.persistence.services;

import com.hanoisurvey.api.infrastructure.persistence.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "service_images")
public class ServiceImageEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private SurveyServiceEntity service;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "alt_text")
    private String altText;

    private String caption;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    public SurveyServiceEntity getService() {
        return service;
    }

    public void setService(SurveyServiceEntity service) {
        this.service = service;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
