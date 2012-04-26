/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.controllers;

import cz.muni.fi.fresnelportal.manager.ProjectManager;
import cz.muni.fi.fresnelportal.model.Project;
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
public class FileUploadController {
    private static final Logger logger = Logger.getLogger(FileUploadController.class.getName());
    
    @Autowired
    private ProjectManager projectManager;
    
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
            
//            InputStream inputStream = file.getInputStream();
//            ServletOutputStream outputStream = response.getOutputStream();
//            byte[] buf = new byte[8192];
//            while (true) {
//              int length = inputStream.read(buf);
//              if (length < 0)
//                break;
//              outputStream.write(buf, 0, length);
//            }
            // store the bytes somewhere
           //model.addAttribute("data", projectsPath + File.pathSeparator + name);
           
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
        
        return "fresnelDocument";
    }
    
    @RequestMapping(value = "/render.htm", method = RequestMethod.POST)
    public String handleFresnelDocument(@RequestParam("selectedGroup") String selectedGroup,
                                        @RequestParam("selectedService") String selectedService, 
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
        
        SemanticWebClient semWebClient = new SemanticWebClient();

        com.hp.hpl.jena.rdf.model.Model semWebModel = semWebClient.asJenaModel("default");        
        
        FSLNSResolver nsr = new FSLNSResolver();
        nsr.addPrefixBinding("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        nsr.addPrefixBinding("gn", "http://www.geonames.org/ontology#");
        nsr.addPrefixBinding("wgs84_pos", "http://www.w3.org/2003/01/geo/wgs84_pos#");
        nsr.addPrefixBinding("foaf", "http://xmlns.com/foaf/0.1/");
        nsr.addPrefixBinding("owl", "http://www.w3.org/2002/07/owl#");
        nsr.addPrefixBinding("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        nsr.addPrefixBinding("fresnel", "http://www.w3.org/2004/09/fresnel#");
        nsr.addPrefixBinding("foafsample", "http://www.fi.muni.cz/fresnel-editor#");
        nsr.addPrefixBinding("dbpedia", "http://dbpedia.org/resource/");
        nsr.addPrefixBinding("yago", "http://dbpedia.org/class/yago/");
        
        SPARQLNSResolver snsr = new SPARQLNSResolver();
        snsr.addPrefixBinding("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        snsr.addPrefixBinding("gn", "http://www.geonames.org/ontology#");
        snsr.addPrefixBinding("wgs84_pos", "http://www.w3.org/2003/01/geo/wgs84_pos#");
        snsr.addPrefixBinding("foaf", "http://xmlns.com/foaf/0.1/");
        snsr.addPrefixBinding("owl", "http://www.w3.org/2002/07/owl#");
        snsr.addPrefixBinding("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        snsr.addPrefixBinding("fresnel", "http://www.w3.org/2004/09/fresnel#");
        snsr.addPrefixBinding("foafsample", "http://www.fi.muni.cz/fresnel-editor#");
        snsr.addPrefixBinding("dbpedia", "http://dbpedia.org/resource/");
        snsr.addPrefixBinding("yago", "http://dbpedia.org/class/yago/");
          
        FSLHierarchyStore fhs = new FSLJenaHierarchyStore();
        
        FSLJenaEvaluator fje = new FSLJenaEvaluator(nsr, fhs);
        fje.setModel(semWebModel);
        
        SPARQLJenaSemWebClientEvaluator sje = new SPARQLJenaSemWebClientEvaluator(snsr);
        sje.setSemanticWebClient(semWebClient);
        
        
        JenaRenderer renderer = new JenaRenderer();
        Document document;
        try {
            document = renderer.render(fd, fje, sje, lensURIs.toArray(new String[0]));
        } catch (ParserConfigurationException ex) {
            logger.log(Level.SEVERE, null, ex);
            model.addAttribute("errors", new String[]{"Problem with parsing configuration!"});
            return handleIndex(model);
        }
        
        DOMSource source = new DOMSource(document);

        TransformerFactory tFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = tFactory.newTransformer(new StreamSource(transformationFile));
            
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
        
        semWebClient.close();
        
        return null;
    }
}
