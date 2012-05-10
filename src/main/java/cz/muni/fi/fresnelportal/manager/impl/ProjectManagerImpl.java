/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.manager.impl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import cz.muni.fi.fresnelportal.FresnelPortalConstants;
import cz.muni.fi.fresnelportal.manager.ProjectManager;
import cz.muni.fi.fresnelportal.model.Project;
import fr.inria.jfresnel.Constants;
import fr.inria.jfresnel.FresnelDocument;
import fr.inria.jfresnel.jena.FresnelJenaParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 *
 * @author nodrock
 */
public class ProjectManagerImpl implements ProjectManager {
    private static final Logger logger = Logger.getLogger(ProjectManagerImpl.class.getName());
    
    private static final RowMapper<Project> PROJECT_MAPPER = new RowMapper<Project>() {
        @Override
        public Project mapRow(ResultSet rs, int i) throws SQLException {
            return new Project(rs.getInt("id"), rs.getString("uri"), rs.getString("name"), rs.getString("title"), rs.getString("description"), rs.getString("filename"));
        }
    };
        
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public void setDataSource(DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Project createProject(File file) {
        logger.log(Level.INFO, "Reading project informations from file: {0}", file.toString());
        Model model = ModelFactory.createDefaultModel();
        
        RDFReader rdfReader = model.getReader(Constants.N3);
        try {
            rdfReader.read(model, new FileInputStream(file), null);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex){
            logger.log(Level.SEVERE, null, ex);
            return null;
        }
        
        StmtIterator si = model.listStatements(null, model.createProperty(Constants.RDF_NAMESPACE_URI, Constants._type), model.getResource(FresnelPortalConstants._Dataset));
	Statement s;
        Resource project;
	if (si.hasNext()){
	    s = si.nextStatement();
            project = s.getSubject();
            
            String uri = project.getURI();
            String[] parts = uri.split("/");
            if(parts.length <= 1){
                return null;
            }
            String name = parts[parts.length-1];
            if(name.indexOf("#") != -1){
                return null;
            }
            
            Statement titleStmt = project.getProperty(model.createProperty(FresnelPortalConstants.DCTERMS_NAMESPACE_URI, FresnelPortalConstants._title));
            Statement descriptionStmt = project.getProperty(model.createProperty(FresnelPortalConstants.DCTERMS_NAMESPACE_URI, FresnelPortalConstants._description));
            if(titleStmt == null){
                return null;
            }
            String title = titleStmt.getLiteral().getLexicalForm();
            if(title == null){
                return null;
            }
            String description = null;
            if(descriptionStmt != null){
                description = descriptionStmt.getLiteral().getLexicalForm();
            }
            if(description == null){
                description = "";
            }
         
            return createProject(new Project(null, uri, name, title, description, file.getName()));            
	}
	si.close();
        model.close();
        
        return null;
    }
    
    private Project createProject(Project project){
        if (project == null) {
            throw new IllegalArgumentException("project");
        }
        logger.log(Level.INFO, "Creating project: {0}", project.toString());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("projects").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uri", project.getUri());
        parameters.put("name", project.getName());
        parameters.put("description", project.getDescription());
        parameters.put("filename", project.getFilename());

        Number id = insert.executeAndReturnKey(parameters);
        project.setId(id.intValue());
        
        return project;
    }

    @Override
    public boolean deleteProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("project");
        }
        logger.log(Level.INFO, "Removing project: {0}", project.toString());
        try{
            int update = jdbcTemplate.update("DELETE FROM projects WHERE id=?", project.getId());
            return update == 1;
        }catch(DataAccessException ex){
            logger.log(Level.SEVERE, "Database error: {0}", ex.toString() );
            return false;
        }
    }

    @Override
    public Project findProjectById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id");
        }
        logger.log(Level.INFO, "Finding project by id: {0}", id);
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM projects WHERE id=?", PROJECT_MAPPER, id);
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, "Database error: {0}", ex.toString() );
            return null;
        }
    }

    @Override
    public Collection<Project> findAllProjects() {
        logger.log(Level.INFO, "Finding all projects.");
        try {
            return jdbcTemplate.query("SELECT * FROM projects", PROJECT_MAPPER);
        }catch(DataAccessException ex){
            logger.log(Level.SEVERE, "Database error: {0}", ex.toString() );
            return null;
        }
    }

    @Override
    public Project findProjectByName(String name) {
        if (name == null ) {
            throw new IllegalArgumentException("name");
        }
        logger.log(Level.INFO, "Finding project by name: {0}", name);
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM projects WHERE name=?", PROJECT_MAPPER, name);
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, "Database error: {0}", ex.toString() );
            return null;
        }
    }
}
