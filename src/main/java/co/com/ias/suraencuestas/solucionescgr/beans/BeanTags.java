/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.ias.suraencuestas.solucionescgr.beans;

/**
 *
 * @author user-
 */
public class BeanTags {
    
    private String value;
    private String title;

    public BeanTags() {
    }

    public BeanTags(String value, String title) {
        this.value = value;
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "{" + "value=" + value + ", title=" + title + '}';
    }
    
}
