/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.manager;

import cz.muni.fi.fresnelportal.model.Service;
import java.util.Collection;

/**
 *
 * @author nodrock
 */
public interface ServiceManager {
    public Service createService(Service service);
    public boolean deleteService(Service service);
    public Service findServiceById(int id);
    public Collection<Service> findAllServices();
}
