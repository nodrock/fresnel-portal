package cz.muni.fi.fresnelportal.manager;

import cz.muni.fi.fresnelportal.model.Service;
import java.util.Collection;

/**
 * Manager for services.
 * @author nodrock
 */
public interface ServiceManager {
    public Service createService(Service service);
    public Service updateService(Service service);
    public boolean deleteService(Service service);
    public Service findServiceById(int id);
    public Collection<Service> findAllServices();
}
