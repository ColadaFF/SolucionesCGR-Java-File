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
public class BeanEmployee {
    
    private String typeDocument;
    private String id;
    private String name;
    private String lastName;
    private String email;
    private List<BeanTags> tags = new ArrayList<BeanTags>();
    private String comment = "";
    
    private int numFileds = 5;

    public BeanEmployee(String typeDocument, String id, String name, String lastName, String email) {
        this.typeDocument = typeDocument;
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    public BeanEmployee() {
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<BeanTags> getTags() {
        return tags;
    }

    public void setTags(List<BeanTags> tags) {
        this.tags = tags;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "BeanEmployee{" + "typeDocument=" + typeDocument + ", id=" + id + ", name=" + name + ", lastName=" + lastName + ", email=" + email + ", tags=" + getTags(tags) + ", comment=" + comment + '}';
    }
    
    private String getTags(List<BeanTags> tags){
        String t = "";
        for (BeanTags tag : tags) {
            t = t + tag.toString() + ", ";
        }
        return t;
    }
    
    public String getValue(int field) {
        switch(field){
            case 1:
                return this.getTypeDocument();
            case 2:
                return this.getId();
            case 3:
                return this.getName();
            case 4:
                return this.getLastName();
            case 5:
                return this.getEmail();
            default:
                return "";
        }
    }
    
    public void setValue(int field, String value) {
        switch(field){
            case 1:
                this.setTypeDocument(value);
                break;
            case 2:
                this.setId(value);
                break;
            case 3:
                this.setName(value);
                break;
            case 4:
                this.setLastName(value);
                break;
            case 5:
                this.setEmail(value);
                break;
            default:
                break;
        }
    }
    
    public boolean validAddEmployee(){
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
