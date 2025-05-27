package com.ebao.teamsapproval.dto;

public class InsuredProfileDTO {

    private int age;
    private String gender;
    private int yearsMarried;
    private String maritalStatus;
    private String occupation;
    private double insuredAmount;
    private String country;
    private String customerGrade;
    private int yearsWithCompany;
    private int numberOfClaims;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getYearsMarried() {
        return yearsMarried;
    }

    public void setYearsMarried(int yearsMarried) {
        this.yearsMarried = yearsMarried;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public double getInsuredAmount() {
        return insuredAmount;
    }

    public void setInsuredAmount(double insuredAmount) {
        this.insuredAmount = insuredAmount;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCustomerGrade() {
        return customerGrade;
    }

    public void setCustomerGrade(String customerGrade) {
        this.customerGrade = customerGrade;
    }

    public int getYearsWithCompany() {
        return yearsWithCompany;
    }

    public void setYearsWithCompany(int yearsWithCompany) {
        this.yearsWithCompany = yearsWithCompany;
    }

    public int getNumberOfClaims() {
        return numberOfClaims;
    }

    public void setNumberOfClaims(int numberOfClaims) {
        this.numberOfClaims = numberOfClaims;
    }

}