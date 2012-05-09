/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.rest;

import cz.muni.fi.fresnelportal.manager.ProjectManager;
import cz.muni.fi.fresnelportal.manager.ServiceManager;
import cz.muni.fi.fresnelportal.manager.TransformationManager;
import cz.muni.fi.fresnelportal.model.Project;
import cz.muni.fi.fresnelportal.model.ProjectList;
import cz.muni.fi.fresnelportal.model.Service;
import cz.muni.fi.fresnelportal.model.ServiceList;
import cz.muni.fi.fresnelportal.model.Transformation;
import cz.muni.fi.fresnelportal.model.TransformationList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author nodrock
 */
@Controller
public class RESTController {

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
}
