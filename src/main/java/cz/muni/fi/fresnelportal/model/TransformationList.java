package cz.muni.fi.fresnelportal.model;

import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nodrock
 */
@XmlRootElement(name="transformations")
public class TransformationList {
	private int count;
	private Collection<Transformation> transformations;

	public TransformationList() {}
	
	public TransformationList(Collection<Transformation> transformations) {
		this.transformations = transformations;
		this.count = transformations.size();
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@XmlElement(name="transformation")
	public Collection<Transformation> getTransformations() {
		return transformations;
	}
	public void setTransformations(Collection<Transformation> transformations) {
		this.transformations = transformations;
	}
}
