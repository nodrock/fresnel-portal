/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.controllers;

import cz.muni.fi.fresnelportal.data.Message;
import cz.muni.fi.fresnelportal.manager.TransformationManager;
import cz.muni.fi.fresnelportal.model.Transformation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nodrock
 */
@Controller
public class TransformationsController {
    private static final Logger logger = Logger.getLogger(TransformationsController.class.getName());
    
    @Autowired
    private TransformationManager transformationManager;
    
    private void prepareModel(Model model, HttpSession session){
        model.addAttribute("currentSection", "transformations_management");
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
            
        return "redirect:/transformations/transformations.htm";
    }
    
    @RequestMapping(value = "/transformations/transformations.htm", method = RequestMethod.GET)
    public String handleTransformationsIndex(Model model, HttpSession session) {
        prepareModel(model, session);
        model.addAttribute("currentPage", "transformations");
        
        Collection<Transformation> transformations = transformationManager.findAllTransformations();
        model.addAttribute("transformations", transformations);
        
        return "/transformations/transformations";
    }
    
    @RequestMapping(value = "/transformations/editTransformation.htm", method = RequestMethod.GET)
    public String handleTransformationEdit(@RequestParam(value="id", required=false) Integer transformationId, Model model, HttpSession session) {
        prepareModel(model, session);
            
        if(transformationId == null){
            model.addAttribute("transformation", new Transformation());
            model.addAttribute("currentPage", "create_transformation");
        }else{
            Transformation transformation = transformationManager.findTransformationById(transformationId);
            if(transformation == null){
                addMessage(session, new Message(Message.ERROR, "Transformation with this id NOT exist!"));
                return "redirect:/transformations/transformations.htm"; 
            }else{
                model.addAttribute("transformation", transformation);
                model.addAttribute("currentPage", "edit_transformation");
            }
        }
        
        return "/transformations/editTransformation";
    }
    
    @RequestMapping(value = "/transformations/deleteTransformation.htm", method = RequestMethod.GET)
    public String handleTransformationDelete(@RequestParam("id") Integer transformationId, Model model, HttpServletRequest request, HttpSession session) {
        
        Transformation transformation = transformationManager.findTransformationById(transformationId);
        if(transformation == null){
            addMessage(session, new Message(Message.ERROR, "No transformation with this id exist!"));
            return "redirect:/transformations/transformations.htm";   
        }
        
        String transformationsPath = request.getSession().getServletContext().getRealPath("/WEB-INF/transformations/");
        File file = new File(transformationsPath + File.separatorChar + transformation.getFilename());
        file.delete();
        if(!file.exists()){
            transformationManager.deleteTransformation(transformation);
        }
     
        return "redirect:/transformations/transformations.htm";
    }
    
    @RequestMapping(value = "/transformations/saveTransformation.htm", method = RequestMethod.POST)
    public String handleTransformationSave(@ModelAttribute("transformation") Transformation transformation, 
                                    Model model, HttpServletRequest request, HttpSession session, 
                                    @RequestParam(value="file", required=false) MultipartFile file) {
        String transformationsPath = request.getSession().getServletContext().getRealPath("/WEB-INF/transformations/");
        
        if(transformation == null){
            addMessage(session, new Message(Message.ERROR, "No transformation!"));
            return "redirect:/transformations/transformations.htm";   
        }
        
        boolean valid = true;
        if(transformation.getName().equals("") || transformation.getContentType().equals("")){
            addMessage(session, new Message(Message.ERROR, "All fields are required!"));
            valid = false; 
        }
        
        if(file.isEmpty()){
            addMessage(session, new Message(Message.ERROR, "No file to upload!"));
            valid = false; 
        }
        
        if(!valid){
            prepareModel(model, session);
            if(transformation.getId() == null){             
                model.addAttribute("currentPage", "create_transformation");
            }else{
                model.addAttribute("currentPage", "edit_transformation");
            }
            model.addAttribute("transformation", transformation);
            return "/transformations/editTransformation"; 
        }
        
        if(transformation.getId() == null){
            byte[] bytes;
            try {
                bytes = file.getBytes();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
                addMessage(session, new Message(Message.ERROR, "Problem with uploaded file!"));
                return "redirect:/transformations/transformations.htm";   
            }
            // gets name of original file
            File f = new File(file.getOriginalFilename());
            String name = f.getName();

            // create new name in projects folder
            File saveFile = new File(transformationsPath + File.separatorChar + name);
            int i = 0;
            while(saveFile.exists()){
                i++;
                saveFile = new File(transformationsPath + File.separatorChar + i + f.getName());
                name = i + f.getName();
            }

            // writes file to projects folder
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(saveFile);
                fos.write(bytes);
                fos.close();
            } catch (FileNotFoundException ex) {
                logger.log(Level.SEVERE, null, ex);
                addMessage(session, new Message(Message.ERROR, "Can't open output file on server!"));
                return "redirect:/transformations/transformations.htm";   
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
                addMessage(session, new Message(Message.ERROR, "Can't write to output file on server!"));
                return "redirect:/transformations/transformations.htm";   
            }

            // try to create Transformation
            Transformation trans = transformationManager.createTransformation(new Transformation(null, transformation.getName(), name, transformation.getContentType()));
            if (trans == null) {
                saveFile.delete();
                addMessage(session, new Message(Message.ERROR, "Can't create transformation!"));
                return "redirect:/transformations/transformations.htm";  
            }
            
        }else{
            transformationManager.updateTransformation(transformation);
        }  
        
        return "redirect:/transformations/transformations.htm";
    }
}
