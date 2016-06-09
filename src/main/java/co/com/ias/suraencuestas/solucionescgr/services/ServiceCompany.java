/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ias.suraencuestas.solucionescgr.services;

import co.com.ias.suraencuestas.solucionescgr.masters.MasterCompany;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * REST Web Service
 *
 * @author user-
 */
@Path("/company")
public class ServiceCompany {

    @GET
    @Path("/getExcel")
    @Produces("application/vnd.ms-excel")
    public Response downloadXLS() {
        MasterCompany masterCompany = new MasterCompany();
        try {
            HSSFWorkbook wb = masterCompany.getExcelCompany();
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            wb.write(outByteStream);
            Response.ResponseBuilder response = Response.ok(outByteStream.toByteArray());
            CacheControl cacheControl = new CacheControl();
            cacheControl.setNoCache(true);
            response.cacheControl(cacheControl);
            response.header("Expires:", "0");
            response.header("Content-Disposition", "attachment; filename=companies.xls; charset=UTF-8");
            response.header("Cache-Control", "no-store");
            response.header("Pragma", "no-cache");
            return response.status(Response.Status.OK).build();

        } catch (IOException ex) {
            return null;
        }

    }

    @POST
    @Path("/massive")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/vnd.ms-excel")
    public Response downloadXLS(MultipartFormDataInput multipartFormDataInput) {
        MasterCompany masterCompany = new MasterCompany();
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
            Logger.getLogger(ServiceCompany.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
