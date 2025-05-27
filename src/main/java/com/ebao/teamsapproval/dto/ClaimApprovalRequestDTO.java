package com.ebao.teamsapproval.dto;

import java.util.List;

public class ClaimApprovalRequestDTO {
    // H000033991
    private String policyNumber;

    // XXXMED001
    private String productCode;

    // Comprehensive hospital and surgical plan
    private String productDescription;

    // 1. The case violates the policy terms and conditions.
    // 2. The case is over [USERNAME] authority.
    private List<String> reasons;

    // Insured: [42, Male, 35, Married, Engineer, 1000000, Brazil]
    // Customer grade: [Gold, 5 years, 10 claims]
    private InsuredProfileDTO profile;

    private String claimNumber;
    private String claimDetails;

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public InsuredProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(InsuredProfileDTO profile) {
        this.profile = profile;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getClaimDetails() {
        return claimDetails;
    }

    public void setClaimDetails(String claimDetails) {
        this.claimDetails = claimDetails;
    }
}