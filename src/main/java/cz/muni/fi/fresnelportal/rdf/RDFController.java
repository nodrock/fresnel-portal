package cz.muni.fi.fresnelportal.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import cz.muni.fi.fresnelportal.manager.ProjectManager;
import cz.muni.fi.fresnelportal.model.Project;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for handling content negotiation and 303 See Other forwarding
 * and outputting documents in proper format.
 * @author nodrock
 */
@Controller
public class RDFController {
    @Autowired
    private ProjectManager projectManager;
    
    @RequestMapping(method = RequestMethod.GET, value = "/projects/{name}", headers = "Accept=text/html")
    public String getProjectSeeOthersHtml(@PathVariable(value="name") String name, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String projectsPath = session.getServletContext().getRealPath("/WEB-INF/projects/");
        Project project = projectManager.findProjectByName(name);

        File projectFile = new File(projectsPath + File.separatorChar + project.getFilename());
        if(projectFile.exists()){    
            response.setStatus(303);
            response.setHeader("Location", request.getRequestURI() + ".html");
            response.setHeader("Vary", "Accept");
        }
        
        return null;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/projects/{name}", headers = "Accept=application/rdf+n3")
    public String getProjectSeeOthersRDFN3(@PathVariable(value="name") String name, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String projectsPath = session.getServletContext().getRealPath("/WEB-INF/projects/");
        Project project = projectManager.findProjectByName(name);

        File projectFile = new File(projectsPath + File.separatorChar + project.getFilename());
        if(projectFile.exists()){    
            response.setStatus(303);
            response.setHeader("Location", request.getRequestURI() + ".n3");
            response.setHeader("Vary", "Accept");
        }
        
        return null;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/projects/{name}", headers = "Accept=application/rdf+xml")
    public String getProjectSeeOthersRDFXML(@PathVariable(value="name") String name, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String projectsPath = session.getServletContext().getRealPath("/WEB-INF/projects/");
        Project project = projectManager.findProjectByName(name);

        File projectFile = new File(projectsPath + File.separatorChar + project.getFilename());
        if(projectFile.exists()){    
            response.setStatus(303);
            response.setHeader("Location", request.getRequestURI() + ".xml");
            response.setHeader("Vary", "Accept");
        }
        
        return null;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/projects/{name}.html", headers = "Accept=text/html")
    public String getProjectHTML(@PathVariable(value="name") String name, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {       
        String projectsPath = session.getServletContext().getRealPath("/WEB-INF/projects/");
        Project project = projectManager.findProjectByName(name);

        File projectFile = new File(projectsPath + File.separatorChar + project.getFilename());


        try {
            response.setHeader("Content-Type", "text/html");
            InputStream is = new FileInputStream(projectFile);
            OutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(is, out);      
            is.close();
            out.close();
            response.getWriter().println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
            response.getWriter().println("<html><title>" + project.getTitle() + "</title><body></body><pre>");
            String doc = out.toString();
            response.getWriter().println(StringEscapeUtils.escapeHtml(doc));
            response.getWriter().println("</pre></body></html>");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/projects/{name}.n3", headers = "Accept=application/rdf+n3")
    public String getProjectRDFN3(@PathVariable(value="name") String name, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {       
        String projectsPath = session.getServletContext().getRealPath("/WEB-INF/projects/");
        Project project = projectManager.findProjectByName(name);

        File projectFile = new File(projectsPath + File.separatorChar + project.getFilename());


        try {
            response.setHeader("Content-Type", "application/rdf+n3");
            InputStream is = new FileInputStream(projectFile);
            OutputStream out = response.getOutputStream();
            IOUtils.copy(is, out);      
            is.close();
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/projects/{name}.xml", headers = "Accept=application/rdf+xml")
    public String getProjectRDFXML(@PathVariable(value="name") String name, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {       
        String projectsPath = session.getServletContext().getRealPath("/WEB-INF/projects/");
        Project project = projectManager.findProjectByName(name);

        File projectFile = new File(projectsPath + File.separatorChar + project.getFilename());
        
        Model model = ModelFactory.createDefaultModel();

        try {
            response.setHeader("Content-Type", "application/rdf+xml");
            InputStream is = new FileInputStream(projectFile);
            
            RDFReader parser = model.getReader("N3");
            parser.read(model, is, "");
            RDFWriter writer = model.getWriter("RDF/XML");
            writer.write(model, response.getOutputStream(), "");
                  
            is.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
