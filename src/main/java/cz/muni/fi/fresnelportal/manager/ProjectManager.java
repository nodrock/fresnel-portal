package cz.muni.fi.fresnelportal.manager;

import cz.muni.fi.fresnelportal.model.Project;
import java.io.File;
import java.util.Collection;

/**
 * Manager for projects.
 * @author nodrock
 */
public interface ProjectManager {
    public Project createProject(File file, String namespace);
    public boolean deleteProject(Project project);
    public Project findProjectById(int id);
    public Project findProjectByName(String name);
    public Collection<Project> findAllProjects();
}
