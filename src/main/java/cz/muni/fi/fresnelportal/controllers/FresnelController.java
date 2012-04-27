/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.controllers;

import com.hp.hpl.jena.rdf.model.ModelFactory;
import cz.muni.fi.fresnelportal.manager.ProjectManager;
import cz.muni.fi.fresnelportal.manager.ServiceManager;
import cz.muni.fi.fresnelportal.model.Project;
import cz.muni.fi.fresnelportal.model.Service;
import cz.muni.fi.jfresnel.jena.semanticweb.SPARQLEndpointGraph;
import cz.muni.fi.jfresnel.jena.semanticweb.SPARQLJenaSemWebClientEvaluator;
import de.fuberlin.wiwiss.ng4j.semwebclient.SemanticWebClient;
import fr.inria.jfresnel.Format;
import fr.inria.jfresnel.FresnelDocument;
import fr.inria.jfresnel.Group;
import fr.inria.jfresnel.Lens;
import fr.inria.jfresnel.fsl.FSLHierarchyStore;
import fr.inria.jfresnel.fsl.FSLNSResolver;
import fr.inria.jfresnel.fsl.jena.FSLJenaEvaluator;
import fr.inria.jfresnel.fsl.jena.FSLJenaHierarchyStore;
import fr.inria.jfresnel.jena.JenaRenderer;
import fr.inria.jfresnel.sparql.SPARQLEvaluator;
import fr.inria.jfresnel.sparql.SPARQLNSResolver;
import fr.inria.jfresnel.sparql.jena.SPARQLJenaEvaluator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

/**
 *
 * @author nodrock
 */
@Controller
public class FresnelController {
    private static final Logger logger = Logger.getLogger(FresnelController.class.getName());
    
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ServiceManager serviceManager;
    
    @RequestMapping(value = "/index.htm", method = RequestMethod.GET)
    public String handleIndex(Model model) {
        Collection<Project> projects = projectManager.findAllProjects();
            
        model.addAttribute("projects", projects);
              
        return "index";
    }
    
    @RequestMapping(value = "/upload.htm", method = RequestMethod.POST)
    public String handleFormUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model model) {
        if(file.isEmpty()){
            model.addAttribute("errors", new String[]{"No file to upload!"});
            return "index";
        }
        String projectsPath = request.getSession().getServletContext().getRealPath("/WEB-INF/projects/");
        
        if (!file.isEmpty()) {
            byte[] bytes;
            try {
                bytes = file.getBytes();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
                model.addAttribute("errors", new String[]{"Problem with uploaded file!"});
                return handleIndex(model);
            }
            // gets name of original file
            File f = new File(file.getOriginalFilename());
            String name = f.getName();
            
            // create new name in projects folder
            File saveFile = new File(projectsPath + File.separatorChar + name);
            
            // writes file to projects folder
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(saveFile);
                fos.write(bytes);
                fos.close();
            } catch (FileNotFoundException ex) {
                logger.log(Level.SEVERE, null, ex);
                model.addAttribute("errors", new String[]{"Can't open output file on server!"});
                return handleIndex(model);
            } catch (IOException ex){
                logger.log(Level.SEVERE, null, ex);
                model.addAttribute("errors", new String[]{"Can't write to output file on server!"});
                return handleIndex(model);
            }
            
               
            // try to create Project
            Project project = projectManager.createProject(saveFile);
            if(project == null){
                saveFile.delete();
                model.addAttribute("errors", new String[]{"File does NOT contain valid Fresnel project!"});
                return handleIndex(model);
            }
       }
        
       return "redirect:index.htm";      
    }
    
    @RequestMapping(value = "/fresnelDocument.htm", method = RequestMethod.GET)
    public String handleFresnelDocument(@RequestParam("id") Integer id, Model model, HttpServletRequest request, HttpSession session) {
        if(id == null){
            model.addAttribute("errors", new String[]{"No id specified!"});
            return handleIndex(model);
        }
        Project project = projectManager.findProjectById(id);
        if(project == null){
            model.addAttribute("errors", new String[]{"No document with this id!"});
            return handleIndex(model);
        }
        
        String projectsPath = request.getSession().getServletContext().getRealPath("/WEB-INF/projects/");
        FresnelDocument fresnelDocument = project.getFresnelDocument(projectsPath);
        if(fresnelDocument == null){
            model.addAttribute("errors", new String[]{"No fresnel data!"});
            return handleIndex(model);
        }
        
        session.setAttribute("fresnelDocument", fresnelDocument);
        
        Lens[] lenses = fresnelDocument.getLenses();
        Format[] formats = fresnelDocument.getFormats();
        Group[] groups = fresnelDocument.getGroups();
        
        model.addAttribute("project", project);
        model.addAttribute("lenses", lenses);
        model.addAttribute("formats", formats);
        model.addAttribute("groups", groups);  
        
        Collection<Service> services = serviceManager.findAllServices();
        model.addAttribute("services", services); 
        
        return "fresnelDocument";
    }
     
    @RequestMapping(value = "/render.htm", method = RequestMethod.POST)
    public String handleFresnelDocument(@RequestParam("selectedGroup") String selectedGroup,
                                        @RequestParam("selectedService") Integer selectedService, 
                                        @RequestParam("selectedTransformation") Integer selectedTransformation,
                                        Model model, HttpSession session,
                                        HttpServletRequest request, HttpServletResponse response) {
        String transformationsPath = request.getSession().getServletContext().getRealPath("/WEB-INF/transformations/");
        File transformationFile = new File(transformationsPath + File.separator + "fresnel-to-xhtml-default.xsl");
        
        FresnelDocument fd;
        Object attribute = session.getAttribute("fresnelDocument");
        if(attribute == null){
            model.addAttribute("errors", new String[]{"No document in session!"});
            return handleIndex(model);
        }
        fd = (FresnelDocument) attribute;
        Group group = fd.getGroup(selectedGroup);
        List<String> lensURIs = new ArrayList<String>();
        for(Lens lens : group.getLenses()){
            lensURIs.add(lens.getURI());
        }
            
        if(selectedService == null){
            model.addAttribute("errors", new String[]{"No service selected!"});
            return handleIndex(model);
        }
        
        JenaRenderer renderer = new JenaRenderer();
        Document document;
        FSLNSResolver nsr = new FSLNSResolver();      
        SPARQLNSResolver snsr = new SPARQLNSResolver();    
        for(String prefix : fd.getPrefixes().keySet()){
            nsr.addPrefixBinding(prefix, fd.getPrefixes().get(prefix));
            snsr.addPrefixBinding(prefix, fd.getPrefixes().get(prefix));
        }
        FSLHierarchyStore fhs = new FSLJenaHierarchyStore();
        
        if(selectedService == 0){
            // Semantic Web Client
            SemanticWebClient semWebClient = new SemanticWebClient();

            com.hp.hpl.jena.rdf.model.Model semWebModel = semWebClient.asJenaModel("default");        
            
            FSLJenaEvaluator fje = new FSLJenaEvaluator(nsr, fhs);
            fje.setModel(semWebModel);

            SPARQLJenaSemWebClientEvaluator sje = new SPARQLJenaSemWebClientEvaluator(snsr);
            sje.setSemanticWebClient(semWebClient);

            try {
                document = renderer.render(fd, fje, sje, lensURIs.toArray(new String[0]));
            } catch (ParserConfigurationException ex) {
                logger.log(Level.SEVERE, null, ex);
                model.addAttribute("errors", new String[]{"Problem with parsing configuration!"});
                return handleIndex(model);
            }
            
            semWebClient.close();
        }else{
            Service service = serviceManager.findServiceById(selectedService);
            if(service == null){
                model.addAttribute("errors", new String[]{"No service with this id!"});
                return handleIndex(model);
            }
                      
            SPARQLEndpointGraph seg = new SPARQLEndpointGraph(service.getUrl());
            com.hp.hpl.jena.rdf.model.Model sparqlEndpointModel = ModelFactory.createModelForGraph(seg);
            
            FSLJenaEvaluator fje = new FSLJenaEvaluator(nsr, fhs);
            fje.setModel(sparqlEndpointModel);
            SPARQLJenaEvaluator sje = new SPARQLJenaEvaluator(snsr);
            sje.setModel(sparqlEndpointModel);
            
            try {
                document = renderer.render(fd, fje, sje, lensURIs.toArray(new String[0]));
            } catch (ParserConfigurationException ex) {
                logger.log(Level.SEVERE, null, ex);
                model.addAttribute("errors", new String[]{"Problem with parsing configuration!"});
                return handleIndex(model);
            }
        }
        
        DOMSource source = new DOMSource(document);

        TransformerFactory tFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = null;
            if(selectedTransformation == 0){
                transformer = tFactory.newTransformer();
                response.setContentType("text/xml;charset=UTF-8");
            }else{
                //TODO:
                transformer = tFactory.newTransformer(new StreamSource(transformationFile));
            }
            
            transformer.transform(source, new StreamResult(response.getOutputStream())); 
        } catch (TransformerConfigurationException ex) {
            logger.log(Level.SEVERE, null, ex);
            model.addAttribute("errors", new String[]{"Problem with transforming document!"});
            return handleIndex(model);
        } catch (TransformerException ex){
            logger.log(Level.SEVERE, null, ex);
            model.addAttribute("errors", new String[]{"Problem with transforming document!"});
            return handleIndex(model);
        } catch (IOException ex){
            logger.log(Level.SEVERE, null, ex);
            model.addAttribute("errors", new String[]{"Problem with writing transformed document to output!"});
            return handleIndex(model);
        }     
        
        return null;
    }
}
