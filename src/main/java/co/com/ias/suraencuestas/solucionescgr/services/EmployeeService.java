/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ias.suraencuestas.solucionescgr.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * REST Web Service
 *
 * @author user-
 */
@Path("employee_v2")
public interface EmployeeService {
    
    @GET
    @Path("/getExcel/{company}")
    @Produces("application/vnd.ms-excel")
    Response downloadXLS(@PathParam("company") String company);
    
    @POST
    @Path("/massive/{company}")
    @Consumes("multipart/form-data")
    @Produces("application/vnd.ms-excel")
    Response uploadXLS(@PathParam("company") String company, MultipartFormDataInput multipartFormDataInput);

}
