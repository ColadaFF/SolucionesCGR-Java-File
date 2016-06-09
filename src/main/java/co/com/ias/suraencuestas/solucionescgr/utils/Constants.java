/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.ias.suraencuestas.solucionescgr.utils;

/**
 *
 * @author user-
 */
public class Constants {
    
//    Amazon    ->      52.2.239.144
//    IAS       ->      190.85.66.122
    private final int PORT_DB = 27017;
    private final String HOST_DB = "mongodb";
    private final String USER_DB = "";
    private final String PASS_DB = "";
    private final String NAME_DB = "NSS";
    private final String PASS_EXCEL = "1a$-01ia$";

    public int getPORT_DB() {
        return PORT_DB;
    }

    public String getHOST_DB() {
        return HOST_DB;
    }

    public String getUSER_DB() {
        return USER_DB;
    }

    public char[] getPASS_DB() {
        return PASS_DB.toCharArray();
    }

    public String getPASS_EXCEL() {
        return PASS_EXCEL;
    }

    public String getNAME_DB() {
        return NAME_DB;
    }
    
}
