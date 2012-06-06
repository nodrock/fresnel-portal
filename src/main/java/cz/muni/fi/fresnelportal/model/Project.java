package cz.muni.fi.fresnelportal.model;

import fr.inria.jfresnel.Constants;
import fr.inria.jfresnel.FresnelDocument;
import fr.inria.jfresnel.jena.FresnelJenaParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Stores information about project.
 * @author nodrock
 */
@XmlRootElement(name="project")
public class Project {
    @Transient
    @XmlTransient
    private FresnelDocument fresnelDocument;
    private Integer id;
    private String uri;
    private String name;
    private String title;
    private String description;
    private String filename;

    public Project(Integer id, String uri, String name, String title, String description, String filename) {
        this.id = id;
        this.uri = uri;
        this.name = name;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return "Project {" + " id = " + id + ", uri = " + uri + ", name = " + name + ", title = " + title + ", description = " + description + ", filename = " + filename + '}';
    }
}
