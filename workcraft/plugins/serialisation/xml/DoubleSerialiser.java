package org.workcraft.plugins.serialisation.xml;

import org.w3c.dom.Element;
import org.workcraft.framework.exceptions.SerialisationException;
import org.workcraft.framework.serialisation.xml.BasicXMLSerialiser;

public class DoubleSerialiser implements BasicXMLSerialiser{
	public String getClassName() {
		return double.class.getName();
	}

	public void serialise(Element element, Object object)
			throws SerialisationException {
		element.setAttribute("value", ((Double)object).toString());
	}
}
