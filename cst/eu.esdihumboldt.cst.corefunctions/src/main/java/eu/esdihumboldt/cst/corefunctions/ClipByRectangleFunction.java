/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                  01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to this website:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to : http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2010.
 *
 * Componet     : cst
 * 	 
 * Classname    : eu.esdihumboldt.cst.transformer/ClipByRectangleFunction.java 
 * 
 * Author       : Josef Bezdek
 * 
 * Created on   : Dec, 2009
 *
 */

package eu.esdihumboldt.cst.corefunctions;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.geotools.feature.AttributeImpl;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.PropertyImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.PropertyDescriptor;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import eu.esdihumboldt.cst.align.ICell;
import eu.esdihumboldt.cst.align.ext.IParameter;
import eu.esdihumboldt.cst.transformer.AbstractCstFunction;
import eu.esdihumboldt.goml.align.Cell;
import eu.esdihumboldt.goml.omwg.Property;
import eu.esdihumboldt.goml.rdf.About;

public class ClipByRectangleFunction extends AbstractCstFunction{

	public static final String YMAX = "YMAX";
	public static final String YMIN = "YMIN";
	public static final String XMAX = "XMAX";
	public static final String XMIN = "XMIN";	
	
	private double YmaxCoord = Double.POSITIVE_INFINITY;
	private double YminCoord = Double.NEGATIVE_INFINITY;
	private double XmaxCoord = Double.POSITIVE_INFINITY;
	private double XminCoord = Double.NEGATIVE_INFINITY;
	private Envelope clipEnvelope;
	private Geometry clipGeometry;
	
	public void setYmaxCoord (double yMax){
		YmaxCoord = yMax;
	}
	public void setYminCoord (double yMin){
		YminCoord = yMin;
	}
	public void setXmaxCoord (double xMax){
		XmaxCoord = xMax;
	}
	public void setXminCoord (double xMin){
		XminCoord = xMin;
	}
	
	public void buildClip (double xMax, double xMin, double yMax, double yMin){
		GeometryFactory gf = new GeometryFactory();
		Coordinate[] coords = new Coordinate[5];
		coords[0] = new Coordinate(xMin, yMin);
		coords[1] = new Coordinate(xMax, yMin);
		coords[2] = new Coordinate(xMax, yMax);
		coords[3] = new Coordinate(xMin, yMax);
		coords[4] = new Coordinate(xMin, yMin);			
		clipGeometry = gf.createLinearRing(coords);
		clipEnvelope = clipGeometry.getEnvelopeInternal();
	}
	
	
	public FeatureCollection<? extends FeatureType, ? extends Feature> transform(
			FeatureCollection<? extends FeatureType, ? extends Feature> fc) {
			return null;
	}

	public Feature transform(Feature source, Feature target) {
		Collection<org.opengis.feature.Property> c = new HashSet<org.opengis.feature.Property>();
		PropertyDescriptor pd = target.getDefaultGeometryProperty().getDescriptor();
		
		Geometry geom = (Geometry)source.getDefaultGeometryProperty().getValue();
		if (clipEnvelope.intersects(geom.getEnvelopeInternal())){
			Object newGeometry = clipGeometry.intersection(geom);
			PropertyImpl p = new AttributeImpl(newGeometry, (AttributeDescriptor) pd, null);	
			c.add(p);
			target.setValue(c);
			return target;
		}
		return null;
	}

	public boolean configure(ICell cell) {
		for (IParameter ip : cell.getEntity1().getTransformation().getParameters()) {
			if (ip.getName().equals(ClipByRectangleFunction.XMAX)) {
				this.setXmaxCoord(Double.parseDouble(ip.getValue()));
			}
			else{
				if (ip.getName().equals(ClipByRectangleFunction.XMIN)) {
					this.setXminCoord(Double.parseDouble(ip.getValue()));
				}	
				else{
					if (ip.getName().equals(ClipByRectangleFunction.YMAX)) {
						this.setYmaxCoord(Double.parseDouble(ip.getValue()));
					}
					else{
						if (ip.getName().equals(ClipByRectangleFunction.YMIN)) {
							this.setYminCoord(Double.parseDouble(ip.getValue()));
						}	
					}
				}
			}
		}
		buildClip(XmaxCoord, XminCoord, YmaxCoord, YminCoord);
		return true;
	}
	@Override
	protected void setParametersTypes(Map<String, Class<?>> parametersTypes) {
		parametersTypes.put(ClipByRectangleFunction.XMAX, Double.class);
		parametersTypes.put(ClipByRectangleFunction.XMIN, Double.class);
		parametersTypes.put(ClipByRectangleFunction.YMAX, Double.class);
		parametersTypes.put(ClipByRectangleFunction.YMIN, Double.class);	
	}
	
	public Cell getParameters() {
		Cell parameterCell = new Cell();
		Property entity1 = new Property(new About(""));
		Property entity2 = new Property(new About(""));
	
		parameterCell.setEntity1(entity1);
		parameterCell.setEntity2(entity2);
		return parameterCell;
	}
	
}
