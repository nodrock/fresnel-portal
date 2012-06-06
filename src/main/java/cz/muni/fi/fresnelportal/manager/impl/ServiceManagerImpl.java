package cz.muni.fi.fresnelportal.manager.impl;

import cz.muni.fi.fresnelportal.manager.ServiceManager;
import cz.muni.fi.fresnelportal.model.Service;
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
 * Implementation of ServiceManager using Spring JdbcTemplate.
 * @author nodrock
 */
public class ServiceManagerImpl implements ServiceManager {
    private static final Logger logger = Logger.getLogger(ServiceManagerImpl.class.getName());
    
    private static final RowMapper<Service> SERVICE_MAPPER = new RowMapper<Service>() {
        @Override
        public Service mapRow(ResultSet rs, int i) throws SQLException {
            return new Service(rs.getInt("id"), rs.getString("name"), rs.getString("url"));
        }
    };
        
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public void setDataSource(DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
    }
    
    @Override
    public Service createService(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("service");
        }
        logger.log(Level.INFO, "Creating service: {0}", service.toString());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("services").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", service.getName());
        parameters.put("url", service.getUrl());

        Number id = insert.executeAndReturnKey(parameters);
        service.setId(id.intValue());
        
        return service;
    }
    
    @Override
    public Service updateService(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("service");
        }
        logger.log(Level.INFO, "Updating user: {0}", service.toString());
        try{
            jdbcTemplate.update("UPDATE services SET name=?, url=? WHERE id=?", service.getName(), service.getUrl(), service.getId());
        }catch(DataAccessException ex){
            logger.log(Level.SEVERE, "Database error: {0}", ex.toString() );
            return null;
        }
        return service;
    }

    @Override
    public boolean deleteService(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("service");
        }
        logger.log(Level.INFO, "Removing service: {0}", service.toString());
        try{
            int update = jdbcTemplate.update("DELETE FROM services WHERE id=?", service.getId());
            return update == 1;
        }catch(DataAccessException ex){
            logger.log(Level.SEVERE, "Database error: {0}", ex.toString() );
            return false;
        }
    }

    @Override
    public Service findServiceById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id");
        }
        logger.log(Level.INFO, "Finding service by id: {0}", id);
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM services WHERE id=?", SERVICE_MAPPER, id);
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, "Database error: {0}", ex.toString() );
            return null;
        }
    }

    @Override
    public Collection<Service> findAllServices() {
        logger.log(Level.INFO, "Finding all services.");
        try {
            return jdbcTemplate.query("SELECT * FROM services", SERVICE_MAPPER);
        }catch(DataAccessException ex){
            logger.log(Level.SEVERE, "Database error: {0}", ex.toString() );
            return null;
        }
    }
    
}
