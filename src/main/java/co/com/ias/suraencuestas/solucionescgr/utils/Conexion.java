/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ias.suraencuestas.solucionescgr.utils;

import co.com.ias.suraencuestas.solucionescgr.beans.BeanKeyValue;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.WriteResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author user-
 */
public class Conexion {

    private final Constants constants = new Constants();
    private final MongoClient mongoClient = new MongoClient(constants.getHOST_DB(), constants.getPORT_DB());

    public String[] getActivityEconomic() {
        DBCollection collection = getCollection("masters");
        BasicDBObject searchQuery = new BasicDBObject();
        BasicDBObject sortQuery = new BasicDBObject();
        searchQuery.put("code", "economicActivities");
        sortQuery.put("value", 1);
        DBCursor cursor = collection.find(searchQuery).sort(sortQuery);
        return ListToArray(getListValues(cursor, "value"));
    }

    public List<BeanKeyValue> getActivityEconomicList() {
        List<BeanKeyValue> arrayValues = new ArrayList<BeanKeyValue>();
        DBCollection collection = getCollection("masters");
        BasicDBObject searchQuery = new BasicDBObject();
        BasicDBObject sortQuery = new BasicDBObject();
        searchQuery.put("code", "economicActivities");
        sortQuery.put("value", 1);
        DBCursor cursor = collection.find(searchQuery).sort(sortQuery);
        while (cursor.hasNext()) {
            DBObject data = cursor.next();
            arrayValues.add(new BeanKeyValue(data.get("value").toString(), data.get("_id").toString()));
        }
        return arrayValues;
    }

    public String[] getNumEmployees() {
        DBCollection collection = getCollection("masters");
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("code", "employeesNumber");
        return ListToArray(collection.distinct("value", searchQuery));
    }

    public List<BeanKeyValue> getNumEmployeesList() {
        List<BeanKeyValue> arrayValues = new ArrayList<BeanKeyValue>();
        DBCollection collection = getCollection("masters");
        BasicDBObject searchQuery = new BasicDBObject();
        BasicDBObject sortQuery = new BasicDBObject();
        searchQuery.put("code", "employeesNumber");
        sortQuery.put("value", 1);
        DBCursor cursor = collection.find(searchQuery).sort(sortQuery);
        while (cursor.hasNext()) {
            DBObject data = cursor.next();
            arrayValues.add(new BeanKeyValue(data.get("value").toString(), data.get("_id").toString()));
        }
        return arrayValues;
    }

    public String[] getTypeDocuments() {
        DBCollection collection = getCollection("masters");
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("code", "documentsTypes");
        return ListToArray(collection.distinct("value", searchQuery));
    }

    public List<BeanKeyValue> getTypeDocumentsList() {
        List<BeanKeyValue> arrayValues = new ArrayList<BeanKeyValue>();
        DBCollection collection = getCollection("masters");
        BasicDBObject searchQuery = new BasicDBObject();
        BasicDBObject sortQuery = new BasicDBObject();
        searchQuery.put("code", "documentsTypes");
        sortQuery.put("value", 1);
        DBCursor cursor = collection.find(searchQuery).sort(sortQuery);
        while (cursor.hasNext()) {
            DBObject data = cursor.next();
            arrayValues.add(new BeanKeyValue(data.get("value").toString(), data.get("_id").toString()));
        }
        return arrayValues;
    }

    public String[] getCountries() {
        DBCollection collection = getCollection("countries");
        return ListToArray(collection.distinct("name"));
    }

    public List<BeanKeyValue> getCountriesList() {
        List<BeanKeyValue> arrayValues = new ArrayList<BeanKeyValue>();
        DBCollection collection = getCollection("countries");
        BasicDBObject sortQuery = new BasicDBObject();
        sortQuery.put("name", 1);
        DBCursor cursor = collection.find().sort(sortQuery);
        while (cursor.hasNext()) {
            DBObject data = cursor.next();
            arrayValues.add(new BeanKeyValue(data.get("name").toString(), data.get("_id").toString(), data.get("code").toString()));
        }
        return arrayValues;
    }

    public String[] getDepartments() {
        DBCollection collection = getCollection("departments");
        return ListToArray(collection.distinct("name"));
    }

    public List<BeanKeyValue> getDepartmentsList() {
        List<BeanKeyValue> arrayValues = new ArrayList<BeanKeyValue>();
        DBCollection collection = getCollection("departments");
        BasicDBObject sortQuery = new BasicDBObject();
        sortQuery.put("name", 1);
        DBCursor cursor = collection.find().sort(sortQuery);
        while (cursor.hasNext()) {
            DBObject data = cursor.next();
            arrayValues.add(new BeanKeyValue(data.get("name").toString(), data.get("_id").toString(), data.get("country").toString()));
        }
        return arrayValues;
    }

    public String[] getCities() {
        DBCollection collection = getCollection("cities");
        return ListToArray(collection.distinct("name"));
    }

    public List<BeanKeyValue> getCitiesList() {
        List<BeanKeyValue> arrayValues = new ArrayList<BeanKeyValue>();
        DBCollection collection = getCollection("cities");
        BasicDBObject sortQuery = new BasicDBObject();
        sortQuery.put("name", 1);
        DBCursor cursor = collection.find().sort(sortQuery);
        while (cursor.hasNext()) {
            DBObject data = cursor.next();
            arrayValues.add(new BeanKeyValue(data.get("name").toString(), data.get("_id").toString(), data.get("department").toString()));
        }
        return arrayValues;
    }
    
    public String[] getARLs() {
        DBCollection collection = getCollection("masters");
        BasicDBObject searchQuery = new BasicDBObject();
        BasicDBObject sortQuery = new BasicDBObject();
        searchQuery.put("code", "arl");
        sortQuery.put("value", 1);
        DBCursor cursor = collection.find(searchQuery).sort(sortQuery);
        return ListToArray(getListValues(cursor, "value"));
    }

    public List<BeanKeyValue> getARLsList() {
        List<BeanKeyValue> arrayValues = new ArrayList<BeanKeyValue>();
        DBCollection collection = getCollection("masters");
        BasicDBObject searchQuery = new BasicDBObject();
        BasicDBObject sortQuery = new BasicDBObject();
        searchQuery.put("code", "arl");
        sortQuery.put("value", 1);
        DBCursor cursor = collection.find(searchQuery).sort(sortQuery);
        while (cursor.hasNext()) {
            DBObject data = cursor.next();
            arrayValues.add(new BeanKeyValue(data.get("value").toString(), data.get("_id").toString()));
        }
        return arrayValues;
    }

    public String getNameCompany(String idCompany) throws Exception {
        DBCollection collection = getCollection("companies");
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", new ObjectId(idCompany));
        DBObject company = collection.findOne(searchQuery);
        if (company != null) {
            return company.get("businessName") + " (NIT: " + company.get("nit") + ")";
        } else {
            throw new Exception("La empresa no existe");
        }
    }

    public String[] getTagsFormEmployee(String idCompany) throws Exception {
        DBCollection collection = getCollection("companies");
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", new ObjectId(idCompany));
        DBObject company = collection.findOne(searchQuery);
        if (company != null) {
            Object tagsCompany = company.get("tags");
            if (tagsCompany != null) {
                BasicDBList tags = (BasicDBList) tagsCompany;
                BasicDBObject[] tagsArr = tags.toArray(new BasicDBObject[0]);
                String[] tagsForm = new String[tagsArr.length];
                for (int i = 0; i < tagsArr.length; i++) {
                    BasicDBObject tagArr = tagsArr[i];
                    tagsForm[i] = tagArr.getString("value");
                }
                return tagsForm;
            }
        } else {
            throw new Exception("La empresa no existe");
        }
        return new String[]{};
    }

    private List<String> getListValues(DBCursor cursor, String value) {
        List<String> list = new ArrayList<String>();
        while (cursor.hasNext()) {
            DBObject data = cursor.next();
            list.add(data.get(value).toString());
        }
        return list;
    }

    private String[] ListToArray(List<String> list) {
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i) + "";
        }
        Arrays.sort(array);
        return array;
    }

    public String saveCompanies(BasicDBObject saveCompany, String nitCompany) {
        DBCollection collection = getCollection("companies");
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("nit", nitCompany);
        DBObject company = collection.findOne(searchQuery);
        if (company == null) {
            WriteResult result = collection.insert(saveCompany);
            if (result.getN() == 0) {
                return "Guardado";
            } else {
                return "Al parecer hubo un error al guardar el registro";
            }
        } else {
            return "Ya existe una empresa con ese NIT";
        }
    }

    public String saveEmployees(BasicDBObject saveEmployee, BasicDBObject savePerson, String idPerson, String idCompany) {
        DBCollection collectionPerson = getCollection("persons");
        BasicDBObject searchQueryPerson = new BasicDBObject();
        searchQueryPerson.put("id", idPerson);
        DBObject person = collectionPerson.findOne(searchQueryPerson);
        if (person != null) {
            return saveEmpleadoWithPerson(person, saveEmployee, idCompany);
        } else {
            WriteResult result = collectionPerson.insert(savePerson);
            if (result.getN() == 0) {
                DBObject personSave = collectionPerson.findOne(searchQueryPerson);
                return saveEmpleadoWithPerson(personSave, saveEmployee, idCompany);
            } else {
                return "Al parecer hubo un error al guardar el registro de la persona";
            }
        }
    }

    private String saveEmpleadoWithPerson(DBObject person, BasicDBObject saveEmployee, String idCompany) {
        DBCollection collectionEmployee = getCollection("employees");
        BasicDBObject searchQueryEmployee = new BasicDBObject();
        searchQueryEmployee.put("person", person.get("_id"));
        searchQueryEmployee.put("company", new ObjectId(idCompany));
        DBObject employee = collectionEmployee.findOne(searchQueryEmployee);
        if (employee != null) {
            return "Esta persona ya se encuentra registrada en esta empresa";
        } else {
            saveEmployee.put("person", person.get("_id"));
            WriteResult result = collectionEmployee.insert(saveEmployee);
            if (result.getN() == 0) {
                return "Guardado";
            } else {
                return "Al parecer hubo un error al guardar el registro del empleado";
            }
        }
    }

    public void closeConnectionMongo() {
        mongoClient.close();
    }

    private DBCollection getCollection(String collection) {
        MongoCredential credential = MongoCredential.createCredential(constants.getUSER_DB(),
                                                                      constants.getNAME_DB(),
                                                                      constants.getPASS_DB());
//        mongoClient = new MongoClient(constants.getHOST_DB(), constants.getPORT_DB());
//        mongoClient = new MongoClient(new ServerAddress(constants.getHOST_DB(), constants.getPORT_DB()), Arrays.asList(credential));
        DB db = mongoClient.getDB(constants.getNAME_DB());
        return db.getCollection(collection);
    }

}
