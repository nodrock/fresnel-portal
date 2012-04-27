/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.controllers;

import cz.muni.fi.fresnelportal.manager.TransformationManager;
import cz.muni.fi.fresnelportal.model.Transformation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    
    @RequestMapping(value = "/transformations/transformations.htm", method = RequestMethod.GET)
    public String handleTransformationsIndex(Model model) {
        Collection<Transformation> transformations = transformationManager.findAllTransformations();
        model.addAttribute("transformations", transformations);
        
        return "/transformations/transformations";
    }
    
    @RequestMapping(value = "/transformations/editTransformation.htm", method = RequestMethod.GET)
    public String handleTransformationEdit(@RequestParam(value="id", required=false) Integer transformationId, Model model) {
        if(transformationId == null){
            model.addAttribute("service", new Transformation());
            model.addAttribute("mode", "create");
        }else{
            Transformation transformation = transformationManager.findTransformationById(transformationId);
            if(transformation == null){
                model.addAttribute("service", new Transformation());
                model.addAttribute("mode", "create");
            }else{
                model.addAttribute("service", transformation);
                model.addAttribute("mode", "edit");
            }
        }
        
        return "/transformations/editTransformation";
    }
    
    @RequestMapping(value = "/transformations/deleteTransformation.htm", method = RequestMethod.GET)
    public String handleServiceDelete(@RequestParam("id") Integer transformationId, Model model, HttpServletRequest request) {
        if(transformationId == null){
            model.addAttribute("errors", new String[]{"No transformation id!"});
            return handleTransformationsIndex(model);
        }
        Transformation transformation = transformationManager.findTransformationById(transformationId);
        if(transformation == null){
            model.addAttribute("errors", new String[]{"No transformation with this id exist!"});
            return handleTransformationsIndex(model);
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
    public String handleServiceSave(@ModelAttribute("service") Transformation transformation, 
                                    Model model, HttpServletRequest request, 
                                    @RequestParam(value="file", required=false) MultipartFile file) {
        String transformationsPath = request.getSession().getServletContext().getRealPath("/WEB-INF/transformations/");
        
        if(transformation == null){
            model.addAttribute("errors", new String[]{"No transformation!"});
            return handleTransformationsIndex(model);
        }
        if(transformation.getId() == null){
            if (!file.isEmpty()) {
                byte[] bytes;
                try {
                    bytes = file.getBytes();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                    model.addAttribute("errors", new String[]{"Problem with uploaded file!"});
                    return handleTransformationsIndex(model);
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
                    model.addAttribute("errors", new String[]{"Can't open output file on server!"});
                    return handleTransformationsIndex(model);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                    model.addAttribute("errors", new String[]{"Can't write to output file on server!"});
                    return handleTransformationsIndex(model);
                }


                // try to create Transformation
                Transformation trans = transformationManager.createTransformation(new Transformation(null, transformation.getName(), name, transformation.getContentType()));
                if (trans == null) {
                    saveFile.delete();
                    model.addAttribute("errors", new String[]{"Can't create transformation!"});
                    return handleTransformationsIndex(model);
                }
            }
            
        }else{
            transformationManager.updateTransformation(transformation);
        }  
        
        return "redirect:/transformations/transformations.htm";
    }
}
