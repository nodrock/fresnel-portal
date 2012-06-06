package cz.muni.fi.fresnelportal.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Stores information about transformation.
 * @author nodrock
 */
@XmlRootElement(name="transformation")
public class Transformation {
    private Integer id;
    private String name;
    private String filename;
    private String contentType;

    public Transformation() {
    }

    public Transformation(Integer id, String name, String filename, String contentType) {
        this.id = id;
        this.name = name;
        this.filename = filename;
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // change this in Spring 3.1.1 with @Valid annotation
    public boolean isValid(){
        return name != null && contentType != null && !name.equals("") && contentType.equals("");
    }
    
    @Override
    public String toString() {
        return "Transformation { id = " + id + ", name = " + name + ", filename = " + filename  + ", contentType = " + contentType + " }";
    }
}
