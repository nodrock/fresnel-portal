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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author nodrock
 */
@Controller
public class ServicesController {
    
    @Autowired
    private ServiceManager serviceManager;
    
    @RequestMapping(value = "/services.htm", method = RequestMethod.GET)
    public String handleServicesIndex(Model model) {
        Collection<Service> services = serviceManager.findAllServices();
        model.addAttribute("services", services);
        
        return "services";
    }
}
