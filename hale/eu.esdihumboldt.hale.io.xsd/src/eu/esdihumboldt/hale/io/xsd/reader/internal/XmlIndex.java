/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                 01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2011.
 */

package eu.esdihumboldt.hale.io.xsd.reader.internal;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.ws.commons.schema.XmlSchemaAttributeGroup;

import com.google.common.base.Preconditions;

import eu.esdihumboldt.hale.schema.model.TypeDefinition;
import eu.esdihumboldt.hale.schema.model.impl.DefaultSchema;
import eu.esdihumboldt.hale.schema.model.impl.DefaultTypeIndex;

/**
 * XML schema used during schema parsing, manages {@link XmlTypeDefinition}s
 * @author Simon Templer
 */
public class XmlIndex extends DefaultSchema {
	
	/**
	 * XML attribute definitions
	 */
	private final Map<QName, XmlAttribute> attributes = new HashMap<QName, XmlAttribute>();
	
	/**
	 * XML attribute group definitions
	 */
	private final Map<QName, XmlSchemaAttributeGroup> attributeGroups = new HashMap<QName, XmlSchemaAttributeGroup>();
	
	/**
	 * XML elements
	 */
	private final Map<QName, XmlElement> elements = new HashMap<QName, XmlElement>();
	
	/**
	 * @see DefaultSchema#DefaultSchema(String, URI)
	 */
	public XmlIndex(String namespace, URI location) {
		super(namespace, location);
	}

	/**
	 * Creates a new type definition if no type with the given name is found.
	 * 
	 * @see DefaultTypeIndex#getType(QName)
	 */
	@Override
	public XmlTypeDefinition getType(QName name) {
		XmlTypeDefinition type = (XmlTypeDefinition) super.getType(name);
		if (type == null) {
			type = new XmlTypeDefinition(name);
			
			TypeUtil.configureType(type);
			
			addType(type);
		}
		return type;
	}

	/**
	 * @see DefaultTypeIndex#addType(TypeDefinition)
	 */
	@Override
	public void addType(TypeDefinition type) {
		Preconditions.checkArgument(type instanceof XmlTypeDefinition,
				"Only XML type definitions may be added to the index");
		
		super.addType(type);
	}

	/**
	 * @return the attribute definitions
	 */
	public Map<QName, XmlAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * @return the attribute group definitions
	 */
	public Map<QName, XmlSchemaAttributeGroup> getAttributeGroups() {
		return attributeGroups;
	}

	/**
	 * @return the element definitions
	 */
	public Map<QName, XmlElement> getElements() {
		return elements;
	}

}
