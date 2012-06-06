package cz.muni.fi.fresnelportal.model;

import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.validator.UrlValidator;

/**
 * Stores information about service.
 * @author nodrock
 */
@XmlRootElement(name="service")
public class Service {
    private Integer id;
    private String name;
    private String url;

    public Service(Integer id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public Service() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    // change this in Spring 3.1.1 with @Valid annotation
    public boolean isValid(){
        UrlValidator urlValidator = new UrlValidator();
        return name != null && url != null && !name.equals("") && urlValidator.isValid(url);
    }
    
    @Override
    public String toString() {
        return "Service {" + " id = " + id + ", name = " + name + ", url = " + url + '}';
    }
}
