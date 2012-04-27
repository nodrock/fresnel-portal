/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.manager.impl;

import cz.muni.fi.fresnelportal.manager.TransformationManager;
import cz.muni.fi.fresnelportal.model.Transformation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
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
public class TransformationManagerImpl implements TransformationManager {
    private static final Logger logger = Logger.getLogger(TransformationManagerImpl.class.getName());
    
    private static final RowMapper<Transformation> TRANSFORMATION_MAPPER = new RowMapper<Transformation>() {
        @Override
        public Transformation mapRow(ResultSet rs, int i) throws SQLException {
            return new Transformation(rs.getInt("id"), rs.getString("name"), rs.getString("filename"), rs.getString("contentType"));
        }
    };
        
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public void setDataSource(DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
    }
    
    @Override
    public Transformation createTransformation(Transformation transformation) {
        if (transformation == null) {
            throw new IllegalArgumentException("transformation");
        }
        logger.log(Level.INFO, "Creating transformation: {0}", transformation.toString());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("transformations").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", transformation.getName());
        parameters.put("filename", transformation.getFilename());
        parameters.put("contentType", transformation.getContentType());

        Number id = insert.executeAndReturnKey(parameters);
        transformation.setId(id.intValue());
        
        return transformation;
    }

    @Override
    public Transformation updateTransformation(Transformation transformation) {
        if (transformation == null) {
            throw new IllegalArgumentException("transformation");
        }
        logger.log(Level.INFO, "Updating transformation: {0}", transformation.toString());
        try{
            jdbcTemplate.update("UPDATE transformations SET name=?, contentType=? WHERE id=?", transformation.getName(), transformation.getContentType(), transformation.getId());
        }catch(DataAccessException ex){
            logger.log(Level.SEVERE, "Database error: {0}", ex.toString() );
            return null;
        }
        return transformation;
    }

    @Override
    public boolean deleteTransformation(Transformation transformation) {
        if (transformation == null) {
            throw new IllegalArgumentException("transformation");
        }
        logger.log(Level.INFO, "Removing transformation: {0}", transformation.toString());
        try{
            int update = jdbcTemplate.update("DELETE FROM transformations WHERE id=?", transformation.getId());
            return update == 1;
        }catch(DataAccessException ex){
            logger.log(Level.SEVERE, "Database error: {0}", ex.toString() );
            return false;
        }
    }

    @Override
    public Transformation findTransformationById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id");
        }
        logger.log(Level.INFO, "Finding transformation by id: {0}", id);
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM transformations WHERE id=?", TRANSFORMATION_MAPPER, id);
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, "Database error: {0}", ex.toString() );
            return null;
        }
    }

    @Override
    public Collection<Transformation> findAllTransformations() {
        logger.log(Level.INFO, "Finding all transformations.");
        try {
            return jdbcTemplate.query("SELECT * FROM transformations", TRANSFORMATION_MAPPER);
        }catch(DataAccessException ex){
            logger.log(Level.SEVERE, "Database error: {0}", ex.toString() );
            return null;
        }
    }
    
}
