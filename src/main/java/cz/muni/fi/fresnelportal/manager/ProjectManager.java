/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.manager;

import cz.muni.fi.fresnelportal.model.Project;
import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author nodrock
 */
public interface ProjectManager {
    public Project createProject(File file, String namespace);
    public boolean deleteProject(Project project);
    public Project findProjectById(int id);
    public Project findProjectByName(String name);
    public Collection<Project> findAllProjects();
}
