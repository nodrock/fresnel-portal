package cz.muni.fi.fresnelportal.model;

import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper object for proper JAXB handling of List.
 * @author nodrock
 */
@XmlRootElement(name="projects")
public class ProjectList {
	private int count;
	private Collection<Project> projects;

	public ProjectList() {}
	
	public ProjectList(Collection<Project> projects) {
		this.projects = projects;
		this.count = projects.size();
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@XmlElement(name="project")
	public Collection<Project> getProjects() {
		return projects;
	}
	public void setProjects(Collection<Project> projects) {
		this.projects = projects;
	}
}
