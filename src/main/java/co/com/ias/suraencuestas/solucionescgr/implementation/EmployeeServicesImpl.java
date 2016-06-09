/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.ias.suraencuestas.solucionescgr.implementation;

import co.com.ias.suraencuestas.solucionescgr.masters.MasterEmployee;
import co.com.ias.suraencuestas.solucionescgr.services.EmployeeService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 *
 * @author user-
 */
public class EmployeeServicesImpl implements EmployeeService{
    
    MasterEmployee masterEmployee = new MasterEmployee();

    @Override
    public Response downloadXLS(String company) {
        try {
            String valid = masterEmployee.getValidIdCompany(company);           
            if(valid == null){
                HSSFWorkbook wb = masterEmployee.getExcelEmployee(company);
                ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                wb.write(outByteStream);
                Response.ResponseBuilder response = Response.ok(outByteStream.toByteArray());
                response.header("Expires:", "0");
                response.header("Content-Disposition", "attachment; filename=employees.xls; charset=UTF-8");
                response.header("Cache-Control", "no-store");
                response.header("Pragma", "no-cache");
                return response.status(Status.OK).build();
            }
        } catch (IOException ex) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }

    @Override
    public Response uploadXLS(String company, MultipartFormDataInput multipartFormDataInput) {
        
        try {
            Map<String, List<InputPart>> uploadForm = multipartFormDataInput.getFormDataMap();
            List<InputPart> inputParts = uploadForm.get("file");
            if (inputParts.size() > 0) {
                InputStream file = inputParts.get(0).getBody(InputStream.class, null);
                POIFSFileSystem pOIFSFileSystem = new POIFSFileSystem(file);
                HSSFWorkbook wb = new HSSFWorkbook(pOIFSFileSystem);
                String valid = masterEmployee.getIdCompany(wb, company);
                if(valid == null){
                    HSSFWorkbook wbEdit = masterEmployee.readExcelEmployee(wb, company);
                    ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                    wbEdit.write(outByteStream);
                    Response.ResponseBuilder response = Response.ok(outByteStream.toByteArray());
                    CacheControl cacheControl = new CacheControl();
                    cacheControl.setNoCache(true);
                    response.cacheControl(cacheControl);
                    response.header("Expires:", "0");
                    response.header("Content-Disposition", "attachment; filename=employees_report.xls; charset=UTF-8");
                    response.header("Cache-Control", "no-store");
                    response.header("Pragma", "no-cache");
                    return response.status(Response.Status.OK).build();
                }
            }
        } catch (IOException ex) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }

}
