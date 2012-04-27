/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.controllers;

import cz.muni.fi.fresnelportal.manager.ServiceManager;
import cz.muni.fi.fresnelportal.model.Service;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author nodrock
 */
@Controller
public class ServicesController {
    
    @Autowired
    private ServiceManager serviceManager;
    
    @RequestMapping(value = "/services/services.htm", method = RequestMethod.GET)
    public String handleServicesIndex(Model model) {
        Collection<Service> services = serviceManager.findAllServices();
        model.addAttribute("services", services);
        
        return "/services/services";
    }
    
    @RequestMapping(value = "/services/editService.htm", method = RequestMethod.GET)
    public String handleServiceEdit(@RequestParam(value="id", required=false) Integer serviceId, Model model) {
        if(serviceId == null){
            model.addAttribute("service", new Service());
            model.addAttribute("mode", "create");
        }else{
            Service service = serviceManager.findServiceById(serviceId);
            if(service == null){
                model.addAttribute("service", new Service());
                model.addAttribute("mode", "create");
            }else{
                model.addAttribute("service", service);
                model.addAttribute("mode", "edit");
            }
        }
        
        return "/services/editService";
    }
    
    @RequestMapping(value = "/services/deleteService.htm", method = RequestMethod.GET)
    public String handleServiceDelete(@RequestParam("id") Integer serviceId, Model model) {
        if(serviceId == null){
            model.addAttribute("errors", new String[]{"No service id!"});
            return handleServicesIndex(model);
        }
        Service service = serviceManager.findServiceById(serviceId);
        if(service == null){
            model.addAttribute("errors", new String[]{"No service with this id exist!"});
            return handleServicesIndex(model);
        }
        
        serviceManager.deleteService(service);
     
        return "redirect:/services/services.htm";
    }
    
    @RequestMapping(value = "/services/saveService.htm", method = RequestMethod.POST)
    public String handleServiceSave(@ModelAttribute("service") Service service, Model model) {
        if(service == null){
            model.addAttribute("errors", new String[]{"No service!"});
            return handleServicesIndex(model);
        }
        if(service.getId() == null){
            serviceManager.createService(service);
        }else{
            serviceManager.updateUser(service);
        }  
        
        return "redirect:/services/services.htm";
    }
}
