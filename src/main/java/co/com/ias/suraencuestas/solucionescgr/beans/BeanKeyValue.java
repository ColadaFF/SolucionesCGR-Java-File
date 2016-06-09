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
public class BeanKeyValue {
    
    private String value;
    private String key;
    private String code;

    public BeanKeyValue() {
    }

    public BeanKeyValue(String value, String key) {
        this.value = value;
        this.key = key;
    }

    public BeanKeyValue(String value, String key, String code) {
        this.value = value;
        this.key = key;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "BeanKeyValue{" + "value=" + value + ", key=" + key + ", code=" + code + '}';
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
}
