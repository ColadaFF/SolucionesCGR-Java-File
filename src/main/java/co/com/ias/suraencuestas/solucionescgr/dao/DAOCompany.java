/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ias.suraencuestas.solucionescgr.dao;

import co.com.ias.suraencuestas.solucionescgr.beans.BeanCompany;
import co.com.ias.suraencuestas.solucionescgr.beans.BeanKeyValue;
import co.com.ias.suraencuestas.solucionescgr.utils.Conexion;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author user-
 */
public class DAOCompany {

    Conexion conexion = new Conexion();

    public List<BeanCompany> saveCompanies(List<BeanCompany> listCompaniesSave) {

        List<BeanKeyValue> alrs = conexion.getARLsList();
        List<BeanKeyValue> countries = conexion.getCountriesList();
        List<BeanKeyValue> departments = conexion.getDepartmentsList();
        List<BeanKeyValue> cities = conexion.getCitiesList();
        List<BeanKeyValue> numEmployees = conexion.getNumEmployeesList();
        List<BeanKeyValue> ActivityEconomic = conexion.getActivityEconomicList();
        List<BeanCompany> listCompaniesResult = new ArrayList<BeanCompany>();

        for (BeanCompany beanCompany : listCompaniesSave) {
            boolean band = true;
            String arl = validDataForm(alrs, beanCompany.getArl());
            String numEmpl = validDataForm(numEmployees, beanCompany.getNumEmployees());
            String ecoActiv = validDataForm(ActivityEconomic, beanCompany.getEconomicActivity());
            String country = validDataForm(countries, beanCompany.getCountry());
            String department = "";
            String city = "";
            if (numEmpl == null) {
                beanCompany.setComment(beanCompany.getComment() + "El valor Número de empleados no es válido. ");
                band = false;
            }
            if (arl == null) {
                beanCompany.setComment(beanCompany.getComment() + "El valor ARL no es válido. ");
                band = false;
            }
            if (ecoActiv == null) {
                beanCompany.setComment(beanCompany.getComment() + "El valor Actividad económica no es válido. ");
                band = false;
            }
            if (country == null) {
                beanCompany.setComment(beanCompany.getComment() + "El valor País no es válido. ");
                band = false;
            } else {
                department = validDataForm(departments, beanCompany.getDepartment(), country);
                if (department == null) {
                    beanCompany.setComment(beanCompany.getComment() + "El valor Departamento no es válido, no coincide con el País. ");
                    band = false;
                } else {
                    city = validDataForm(cities, beanCompany.getCity(), department);
                    if (city == null) {
                        beanCompany.setComment(beanCompany.getComment() + "El valor Ciudad no es válido, no coincide con el Departamento. ");
                        band = false;
                    }
                }
            }
            if (band) {
                BeanCompany saveCompany = beanCompany;
                String numEm = saveCompany.getNumEmployees();
                String ecoAct =  saveCompany.getEconomicActivity();
                String coun = saveCompany.getCountry();
                String depart = saveCompany.getDepartment();
                String cit = saveCompany.getCity();
                String arlSelected = saveCompany.getArl();
                
                saveCompany.setNumEmployees(numEmpl);
                saveCompany.setEconomicActivity(ecoActiv);
                saveCompany.setCountry(country);
                saveCompany.setDepartment(department);
                saveCompany.setCity(city);
                saveCompany.setArl(arl);

                beanCompany.setComment(saveCompany(saveCompany));
                if(!"Guardado".equals(beanCompany.getComment())){
                    beanCompany.setNumEmployees(numEm);
                    beanCompany.setEconomicActivity(ecoAct);
                    beanCompany.setCountry(coun);
                    beanCompany.setDepartment(depart);
                    beanCompany.setCity(cit);
                    beanCompany.setArl(arlSelected);
                    listCompaniesResult.add(beanCompany);
                }
            } else {
                listCompaniesResult.add(beanCompany);
            }
        }
        return listCompaniesResult;
    }

    private String validDataForm(List<BeanKeyValue> listData, String value) {
        for (int i = 0; i < listData.size(); i++) {
            if (value.equals(listData.get(i).getValue())) {
                return listData.get(i).getKey();
            }
        }
        return null;
    }

    private String validDataForm(List<BeanKeyValue> listData, String value, String id) {
        for (int i = 0; i < listData.size(); i++) {
            if (value.equals(listData.get(i).getValue()) && id.equals(listData.get(i).getCode())) {
                return listData.get(i).getKey();
            }
        }
        return null;
    }

    private String saveCompany(BeanCompany objectCompany) {
        BasicDBObject saveCompany = new BasicDBObject();
        saveCompany.put("nit", objectCompany.getNit());
        saveCompany.put("type", objectCompany.getType());
        saveCompany.put("email", objectCompany.getEmail());
        saveCompany.put("phone", objectCompany.getPhone());
        saveCompany.put("address", objectCompany.getAddress());
        saveCompany.put("acronym", objectCompany.getAcronym());
        saveCompany.put("businessName", objectCompany.getBusinessName());
        saveCompany.put("numEmployees", new ObjectId(objectCompany.getNumEmployees()));
        saveCompany.put("economicActivity", new ObjectId(objectCompany.getEconomicActivity()));
        saveCompany.put("country", new ObjectId(objectCompany.getCountry()));
        saveCompany.put("department", new ObjectId(objectCompany.getDepartment()));
        saveCompany.put("city", new ObjectId(objectCompany.getCity()));
        saveCompany.put("phoneContact", objectCompany.getPhoneContact());
        saveCompany.put("nameContact", objectCompany.getNameContact());
        saveCompany.put("emailContact", objectCompany.getEmailContact());
        saveCompany.put("arl", objectCompany.getArl());
        if(objectCompany.getTags().size() > 0){
            saveCompany.put("tags", getTagsArray(objectCompany.getTags()));
        }
        return conexion.saveCompanies(saveCompany, objectCompany.getNit());
    }

    private BasicDBList getTagsArray(List<String> tagsCompanies) {
        BasicDBList tags = new BasicDBList();
        for (String tagsCompany : tagsCompanies) {
            BasicDBObject savetag = new BasicDBObject();
            savetag.put("value", tagsCompany);
            tags.add(savetag);
        }
        return tags;
    }
    
}
