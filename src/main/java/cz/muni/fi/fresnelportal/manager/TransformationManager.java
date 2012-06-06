/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.manager;

import cz.muni.fi.fresnelportal.model.Transformation;
import java.util.Collection;

/**
 * Manager for transformations.
 * @author nodrock
 */
public interface TransformationManager {
    public Transformation createTransformation(Transformation transformation);
    public Transformation updateTransformation(Transformation transformation);
    public boolean deleteTransformation(Transformation transformation);
    public Transformation findTransformationById(int id);
    public Collection<Transformation> findAllTransformations();
}
