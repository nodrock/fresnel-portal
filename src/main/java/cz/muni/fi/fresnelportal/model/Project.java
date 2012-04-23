/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.model;

import fr.inria.jfresnel.Constants;
import fr.inria.jfresnel.FresnelDocument;
import fr.inria.jfresnel.jena.FresnelJenaParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;

/**
 *
 * @author nodrock
 */
public class Project {
    private FresnelDocument fresnelDocument;
    private Integer id;
    private String uri;
    private String name;
    private String description;
    private String filename;

    public Project(Integer id, String uri, String name, String description, String filename) {
        this.id = id;
        this.uri = uri;
        this.name = name;
        this.description = description;
        this.filename = filename;
    }

    public Project() {
        
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public FresnelDocument getFresnelDocument(String projectsPath){
        if(fresnelDocument != null){
            return fresnelDocument;
        }
        File file = new File(projectsPath + File.separator + getFilename());
        
        FresnelDocument fd = null;
        FresnelJenaParser fjp = new FresnelJenaParser(null, null);
        try {
            fd = fjp.parse(new FileInputStream(file), Constants.N3_READER, "");
        } catch (FileNotFoundException ex) {
            return null;
        }
        
        fresnelDocument = fd;
        return fd;
    }
    
    @Override
    public String toString() {
        return "Project {" + " id = " + id + ", uri = " + uri + ", name = " + name + ", description = " + description + ", filename = " + filename + '}';
    }
}
