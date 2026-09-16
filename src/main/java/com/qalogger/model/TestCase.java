package com.qalogger.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "test_cases")
public class TestCase {

    @Id
    private String id;

    @Column(nullable = false)
    private String projectId;

    @Column(nullable = false)
    private Integer srNo;

    @Column(nullable = false)
    private String module;

    @Column(length = 4000, nullable = false)
    private String title;

    @Column(length = 4000)
    private String expectedResult;

    @Column(nullable = false)
    private String status;

    private String assignedBy;

    @Column(length = 4000)
    private String remarks;

    private String linkedIssueId;

    private String createdAt;
    private String updatedAt;

    public TestCase() {
    }

    public TestCase(String id, String projectId, Integer srNo, String module, String title,
                    String expectedResult, String status, String assignedBy, String remarks,
                    String linkedIssueId, String createdAt, String updatedAt) {
        this.id = id;
        this.projectId = projectId;
        this.srNo = srNo;
        this.module = module;
        this.title = title;
        this.expectedResult = expectedResult;
        this.status = status;
        this.assignedBy = assignedBy;
        this.remarks = remarks;
        this.linkedIssueId = linkedIssueId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getSrNo() {
        return srNo;
    }

    public void setSrNo(Integer srNo) {
        this.srNo = srNo;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLinkedIssueId() {
        return linkedIssueId;
    }

    public void setLinkedIssueId(String linkedIssueId) {
        this.linkedIssueId = linkedIssueId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
