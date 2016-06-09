/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ias.suraencuestas.solucionescgr.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user-
 */
public class BeanCompany {

    private String type = "NIT";
    private String nit;
    private String businessName;
    private String acronym;
    private String address;
    private String phone;
    private String email;
    private String numEmployees;
    private String economicActivity;
    private String country;
    private String department;
    private String city;
    private String nameContact;
    private String phoneContact;
    private String emailContact;
    private String arl;
    private List<String> tags = new ArrayList<String>();
    private String comment = "";
    
    private int numFileds = 14;

    public BeanCompany() {}

    public BeanCompany(String nit, String businessName, String acronym, String address, String phone, String email, String numEmployees, String economicActivity, String country, String department, String city, String nameContact, String phoneContact, String emailContact, List<String> tags, String arl) {
        this.nit = nit;
        this.businessName = businessName;
        this.acronym = acronym;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.numEmployees = numEmployees;
        this.economicActivity = economicActivity;
        this.country = country;
        this.department = department;
        this.city = city;
        this.nameContact = nameContact;
        this.phoneContact = phoneContact;
        this.emailContact = emailContact;
        this.tags = tags;
        this.arl = arl;
    }
    
    public int getNumFields(){
        return 15;
    }

    public String getNit() {
        return nit;
    }

    public String getArl() {
        return arl;
    }

    public void setArl(String arl) {
        this.arl = arl;
    }

    public int getNumFileds() {
        return numFileds;
    }

    public void setNumFileds(int numFileds) {
        this.numFileds = numFileds;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getNumEmployees() {
        return numEmployees;
    }

    public void setNumEmployees(String numEmployees) {
        this.numEmployees = numEmployees;
    }

    public String getEconomicActivity() {
        return economicActivity;
    }

    public void setEconomicActivity(String economicActivity) {
        this.economicActivity = economicActivity;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNameContact() {
        return nameContact;
    }

    public void setNameContact(String nameContact) {
        this.nameContact = nameContact;
    }

    public String getPhoneContact() {
        return phoneContact;
    }

    public void setPhoneContact(String phoneContact) {
        this.phoneContact = phoneContact;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getComment() {
        return comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "BeanCompany{" + "type=" + type + ", nit=" + nit + ", businessName=" + businessName + ", acronym=" + acronym + ", address=" + address + ", phone=" + phone + ", email=" + email + ", numEmployees=" + numEmployees + ", economicActivity=" + economicActivity + ", country=" + country + ", department=" + department + ", city=" + city + ", nameContact=" + nameContact + ", phoneContact=" + phoneContact + ", emailContact=" + emailContact + ", arl=" + arl + ", tags=" + getTags(tags) + ", comment=" + comment + ", numFileds=" + numFileds + '}';
    }
    
    private String getTags(List<String> tags){
        String t = "";
        for (String tag : tags) {
            t = t + tag + ", ";
        }
        return t;
    }

    public String getValue(int field) {
        switch(field){
            case 1:
                return this.getNit();
            case 2:
                return this.getBusinessName();
            case 3:
                return this.getAcronym();
            case 4:
                return this.getAddress();
            case 5:
                return this.getPhone();
            case 6:
                return this.getEmail();
            case 7:
                return this.getNumEmployees();
            case 8:
                return this.getEconomicActivity();
            case 9:
                return this.getArl();
            case 10:
                return this.getCountry();
            case 11:
                return this.getDepartment();
            case 12:
                return this.getCity();
            case 13:
                return this.getNameContact();
            case 14:
                return this.getPhoneContact();
            case 15:
                return this.getEmailContact();
            default:
                return "";
        }
    }
    
    public void setValue(int field, String value) {
        switch(field){
            case 1:
                this.setNit(value);
                break;
            case 2:
                this.setBusinessName(value);
                break;
            case 3:
                this.setAcronym(value);
                break;
            case 4:
                this.setAddress(value);
                break;
            case 5:
                this.setPhone(value);
                break;
            case 6:
                this.setEmail(value);
                break;
            case 7:
                this.setNumEmployees(value);
                break;
            case 8:
                this.setEconomicActivity(value);
                break;
            case 9:
                this.setArl(value);
                break;
            case 10:
                this.setCountry(value);
                break;
            case 11:
                this.setDepartment(value);
                break;
            case 12:
                this.setCity(value);
                break;
            case 13:
                this.setNameContact(value);
                break;
            case 14:
                this.setPhoneContact(value);
                break;
            case 15:
                this.setEmailContact(value);
                break;
            default:
                break;
        }
    }
    
    public boolean validAddCompany(){
        for (int i = 0; i < numFileds; i++) {
            if(!isNull(getValue(i))){
                return true;
            }
        }
        return getTags().size() > 0;
    }
    
    private boolean isNull(String value) {
        if (value == null) {
            return true;
        }
        return value.replaceAll(" ", "").isEmpty();
    }

}
