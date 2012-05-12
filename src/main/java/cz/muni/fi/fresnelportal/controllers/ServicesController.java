/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.controllers;

import cz.muni.fi.fresnelportal.data.Message;
import cz.muni.fi.fresnelportal.manager.ServiceManager;
import cz.muni.fi.fresnelportal.model.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.validator.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author nodrock
 */
@Controller
public class ServicesController {
    private static final Logger logger = Logger.getLogger(ServicesController.class.getName());
    
    @Autowired
    private ServiceManager serviceManager;
    
    private void prepareModel(Model model, HttpSession session){
        model.addAttribute("currentSection", "services_management");
        model.addAttribute("messages", session.getAttribute("messages"));
        session.removeAttribute("messages");
    }
    
    private void addMessage(HttpSession session, Message msg){
        List<Message> messages = (List<Message>) session.getAttribute("messages");
        if(messages == null){
            messages = new ArrayList<Message>();
            session.setAttribute("messages", messages);
        }
        messages.add(msg);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request, HttpSession session) {    
        addMessage(session, new Message(Message.ERROR, ex.getMessage()));
            
        return "redirect:/services/services.htm";
    }
    
    @RequestMapping(value = "/services/services.htm", method = RequestMethod.GET)
    public String handleServicesIndex(Model model, HttpSession session) {
        prepareModel(model, session);
        model.addAttribute("currentPage", "services");
        
        Collection<Service> services = serviceManager.findAllServices();
        model.addAttribute("services", services);
        
        return "/services/services";
    }
    
    @RequestMapping(value = "/services/editService.htm", method = RequestMethod.GET)
    public String handleServiceEdit(@RequestParam(value="id", required=false) Integer serviceId, Model model, HttpSession session) {
        prepareModel(model, session);
        
        if(serviceId == null){
            model.addAttribute("service", new Service());
            model.addAttribute("currentPage", "create_service");
        }else{
            Service service = serviceManager.findServiceById(serviceId);
            if(service == null){
                addMessage(session, new Message(Message.ERROR, "Service with this id NOT exist!"));
                return "redirect:/services/services.htm"; 
            }else{
                model.addAttribute("service", service);
                model.addAttribute("currentPage", "edit_service");
            }
        }
        
        return "/services/editService";
    }
    
    @RequestMapping(value = "/services/deleteService.htm", method = RequestMethod.GET)
    public String handleServiceDelete(@RequestParam("id") Integer serviceId, Model model, HttpSession session) {
        
        Service service = serviceManager.findServiceById(serviceId);
        if(service == null){
            addMessage(session, new Message(Message.ERROR, "No service with this id exist!"));
            return "redirect:/services/services.htm";      
        }
        
        serviceManager.deleteService(service);
     
        return "redirect:/services/services.htm";
    }
    
    @RequestMapping(value = "/services/saveService.htm", method = RequestMethod.POST)
    public String handleServiceSave(@ModelAttribute("service") Service service, Model model, HttpSession session) {        
        if(service == null){
            addMessage(session, new Message(Message.ERROR, "No service!"));
            return "redirect:/services/services.htm";    
        }
        
        boolean valid = true;
        if(service.getName().equals("") || service.getUrl().equals("")){
            addMessage(session, new Message(Message.ERROR, "All fields are required!"));
            valid = false; 
        }
        
        UrlValidator urlValidator = new UrlValidator();
        if(!urlValidator.isValid(service.getUrl())){
            addMessage(session, new Message(Message.ERROR, "Url is NOT valid!"));
            valid = false;             
        }
        
        if(!valid){
            prepareModel(model, session);
            if(service.getId() == null){             
                model.addAttribute("currentPage", "create_service");
            }else{
                model.addAttribute("currentPage", "edit_service");
            }
            model.addAttribute("service", service);
            return "/services/editService"; 
        }
        
        if(service.getId() == null){          
            serviceManager.createService(service);
        }else{
            serviceManager.updateUser(service);
        }  
        
        return "redirect:/services/services.htm";
    }
}
