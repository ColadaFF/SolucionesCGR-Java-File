/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.ias.suraencuestas.solucionescgr.dao;


import co.com.ias.suraencuestas.solucionescgr.beans.BeanEmployee;
import co.com.ias.suraencuestas.solucionescgr.beans.BeanKeyValue;
import co.com.ias.suraencuestas.solucionescgr.beans.BeanTags;
import co.com.ias.suraencuestas.solucionescgr.utils.Conexion;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author user-
 */
public class DAOEmployee {
    Conexion conexion = new Conexion();

    public List<BeanEmployee> saveCompanies(List<BeanEmployee> listCompaniesSave, String idCompany) {

        List<BeanKeyValue> typeDocuments = conexion.getTypeDocumentsList();
        List<BeanEmployee> listEmployeesResult = new ArrayList<BeanEmployee>();

        for (BeanEmployee beanEmployee : listCompaniesSave) {
            boolean band = true;
            String typeDoc = validDataForm(typeDocuments, beanEmployee.getTypeDocument());
            if (typeDoc == null) {
                beanEmployee.setComment(beanEmployee.getComment() + "El valor Tipo de documento no es válido. ");
                band = false;
            }
            if (band) {
                BeanEmployee saveEmployee = beanEmployee;
                String typeID = beanEmployee.getTypeDocument();
                
                saveEmployee.setTypeDocument(typeDoc);

                beanEmployee.setComment(saveEmployee(saveEmployee, idCompany));
                if(!("Guardado".equals(beanEmployee.getComment()))){
                    beanEmployee.setTypeDocument(typeID);
                    listEmployeesResult.add(beanEmployee);
                }
            } else {
                listEmployeesResult.add(beanEmployee);
            }
        }
        return listEmployeesResult;
    }

    private String validDataForm(List<BeanKeyValue> listData, String value) {
        for (int i = 0; i < listData.size(); i++) {
            if (value.equals(listData.get(i).getValue())) {
                return listData.get(i).getKey();
            }
        }
        return null;
    }

    private String saveEmployee(BeanEmployee objectEmployee, String idCompany) {
        BasicDBObject saveEmployee = new BasicDBObject();
        BasicDBObject savePerson = new BasicDBObject();
        
        savePerson.put("documentType", new ObjectId(objectEmployee.getTypeDocument()));
        savePerson.put("id", objectEmployee.getId());
        savePerson.put("name", objectEmployee.getName());
        savePerson.put("lastName", objectEmployee.getLastName());
        savePerson.put("email", objectEmployee.getEmail());
        savePerson.put("createdAt", new Date());
        
        saveEmployee.put("company", new ObjectId(idCompany));
        saveEmployee.put("deleted", false);
        if(objectEmployee.getTags().size() > 0){
            saveEmployee.put("tags", getTagsArray(objectEmployee.getTags()));
        }
        
        return conexion.saveEmployees(saveEmployee, savePerson, objectEmployee.getId(), idCompany);
    }

    private BasicDBList getTagsArray(List<BeanTags> tagsEmployee) {
        BasicDBList tags = new BasicDBList();
        for (BeanTags tag : tagsEmployee) {
            BasicDBObject savetag = new BasicDBObject();
            savetag.put("value", tag.getValue());
            savetag.put("title", tag.getTitle());
            tags.add(savetag);
        }
        return tags;
    }
}
