package com.hanoisurvey.api.infrastructure.persistence.services;

import com.hanoisurvey.api.infrastructure.persistence.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "service_documents")
public class ServiceDocumentEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private SurveyServiceEntity service;

    @Column(nullable = false)
    private String title;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    public SurveyServiceEntity getService() {
        return service;
    }

    public void setService(SurveyServiceEntity service) {
        this.service = service;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
