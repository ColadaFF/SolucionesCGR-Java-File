/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.ias.suraencuestas.solucionescgr.implementation;

import co.com.ias.suraencuestas.solucionescgr.masters.MasterCompany;
import co.com.ias.suraencuestas.solucionescgr.services.CompanyService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CompanyServicesImpl  implements CompanyService{
    
    MasterCompany masterCompany = new MasterCompany();

    @Override
    public Response downloadXLS() {
        try {
            HSSFWorkbook wb = masterCompany.getExcelCompany();
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            wb.write(outByteStream);
            Response.ResponseBuilder response = Response.ok(outByteStream.toByteArray());
            response.header("Expires:", "0");
            response.header("Content-Disposition", "attachment; filename=companies.xls; charset=UTF-8");
            response.header("Cache-Control", "no-store");
            response.header("Pragma", "no-cache");
            return response.status(Status.OK).build();
        } catch (IOException ex) {
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @Override
    public Response uploadXLS(MultipartFormDataInput multipartFormDataInput) {
        try {
            Map<String, List<InputPart>> uploadForm = multipartFormDataInput.getFormDataMap();
            List<InputPart> inputParts = uploadForm.get("file");
            if (inputParts.size() > 0) {
                InputStream file = inputParts.get(0).getBody(InputStream.class, null);
                POIFSFileSystem pOIFSFileSystem = new POIFSFileSystem(file);
                HSSFWorkbook wb = new HSSFWorkbook(pOIFSFileSystem);
                HSSFWorkbook wbEdit = masterCompany.readExcelCompany(wb);
                ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                wbEdit.write(outByteStream);
                Response.ResponseBuilder response = Response.ok(outByteStream.toByteArray());
                CacheControl cacheControl = new CacheControl();
                cacheControl.setNoCache(true);
                response.cacheControl(cacheControl);
                response.header("Expires:", "0");
                response.header("Content-Disposition", "attachment; filename=companies_report.xls; charset=UTF-8");
                response.header("Cache-Control", "no-store");
                response.header("Pragma", "no-cache");
                return response.status(Response.Status.OK).build();
            }
        } catch (IOException ex) {
            Logger.getLogger(CompanyServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
