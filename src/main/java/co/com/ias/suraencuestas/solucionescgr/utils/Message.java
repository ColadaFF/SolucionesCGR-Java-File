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
public class Message {
    
    public String getMessageCompany(int field){
        switch(field){
            case 1:
                return "NIT";
            case 2:
                return "Razón social";
            case 3:
                return "Abreviatura";
            case 4:
                return "Dirección de la sede principal";
            case 5:
                return "Teléfono";
            case 6:
                return "Correo electrónico";
            case 7:
                return "Número de empleados";
            case 8:
                return "Actividad Econónica";
            case 9:
                return "País";
            case 10:
                return "Departamento";
            case 11:
                return "Ciudad";
            case 12:
                return "Nombre del contacto";
            case 13:
                return "Teléfono del contacto";
            case 14:
                return "Correo electrónico del contacto";
            default:
                return "";
        }
    }
    
    public String getMessageEmployee(int field){
        switch(field){
            case 1:
                return "Tipo de documento";
            case 2:
                return "Número de identificación";
            case 3:
                return "Nombre(s)";
            case 4:
                return "Apellidos";
            case 5:
                return "Correo electrónico";
            default:
                return "";
        }
    }
    
}
