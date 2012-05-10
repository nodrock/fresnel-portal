/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.rest;

import cz.muni.fi.fresnelportal.controllers.FresnelController;
import cz.muni.fi.fresnelportal.manager.ProjectManager;
import cz.muni.fi.fresnelportal.manager.ServiceManager;
import cz.muni.fi.fresnelportal.manager.TransformationManager;
import cz.muni.fi.fresnelportal.model.Project;
import cz.muni.fi.fresnelportal.model.ProjectList;
import cz.muni.fi.fresnelportal.model.Service;
import cz.muni.fi.fresnelportal.model.ServiceList;
import cz.muni.fi.fresnelportal.model.Transformation;
import cz.muni.fi.fresnelportal.model.TransformationList;
import cz.muni.fi.fresnelportal.utils.FresnelPortalUtils;
import cz.muni.fi.fresnelportal.utils.HttpUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nodrock
 */
@Controller
public class RESTController {
    private static final Logger logger = Logger.getLogger(RESTController.class.getName());
    
    @Autowired
    private ServiceManager serviceManager;
    
    @Autowired
    private TransformationManager transformationManager;
    
    @Autowired
    private ProjectManager projectManager;
    
    @RequestMapping(method = RequestMethod.GET, value = "/services", headers = "Accept=application/json,application/xml")
    @ResponseBody 
    public ServiceList getServices() {
        Collection<Service> services = serviceManager.findAllServices();
        return new ServiceList(services);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/service/{id}", headers = "Accept=application/json,application/xml")
    @ResponseBody 
    public Service getService(@PathVariable("id") int id) {
        Service service = serviceManager.findServiceById(id);
        return service;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/transformations", headers = "Accept=application/json,application/xml")
    @ResponseBody 
    public TransformationList getTransformations() {
        Collection<Transformation> transformations = transformationManager.findAllTransformations();
        return new TransformationList(transformations);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/transformation/{id}", headers = "Accept=application/json,application/xml")
    @ResponseBody 
    public Transformation getTransformation(@PathVariable("id") int id) {
        Transformation transformation = transformationManager.findTransformationById(id);
        return transformation;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/projects", headers = "Accept=application/json,application/xml")
    @ResponseBody 
    public ProjectList getProjects() {
        Collection<Project> projects = projectManager.findAllProjects();
        return new ProjectList(projects);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/project/{id}", headers = "Accept=application/json,application/xml")
    @ResponseBody 
    public Project getProject(@PathVariable("id") int id) {
        Project project = projectManager.findProjectById(id);
        return project;
    }
    
    @RequestMapping(value = "/upload", method = RequestMethod.POST, headers = "Accept=application/json,application/xml")
    @ResponseBody
    public Project handleProjectUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model model, HttpSession session) {  
        Project project = null;
        
        if(file.isEmpty()){
            return null;
        }
        String projectsPath = request.getSession().getServletContext().getRealPath("/WEB-INF/projects/");
        
        if (!file.isEmpty()) {
            // gets name of original file
            File f = new File(file.getOriginalFilename());
            String name = f.getName();

            // create new name in projects folder
            File saveFile = new File(projectsPath + File.separatorChar + name);
            int i = 0;
            while (saveFile.exists()) {
                i++;
                saveFile = new File(projectsPath + File.separatorChar + i + f.getName());
                name = i + f.getName();
            }

            String projectsNamespace = HttpUtils.getBaseUrl(request) + "/rdf/projects/";
            
            // writes file to projects folder
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(saveFile);
                FresnelPortalUtils.replaceNamespaces(projectsNamespace, file.getInputStream(), fos);
                fos.close();
            } catch (FileNotFoundException ex) {
                logger.log(Level.SEVERE, null, ex);
                
                return null;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
                
                return null;
            }


            // try to create Project
            project = projectManager.createProject(saveFile, projectsNamespace);
            if (project == null) {
                saveFile.delete();
                
                return null;
            }
       }
        
       return project;  
    }
}
