package cz.muni.fi.fresnelportal.model;

import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper object for proper JAXB handling of List.
 * @author nodrock
 */
@XmlRootElement(name="services")
public class ServiceList {
	private int count;
	private Collection<Service> services;

	public ServiceList() {}
	
	public ServiceList(Collection<Service> services) {
		this.services = services;
		this.count = services.size();
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@XmlElement(name="service")
	public Collection<Service> getServices() {
		return services;
	}
	public void setServices(Collection<Service> services) {
		this.services = services;
	}
}
