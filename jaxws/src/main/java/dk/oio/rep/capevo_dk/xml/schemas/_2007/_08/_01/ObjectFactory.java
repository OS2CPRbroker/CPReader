
package dk.oio.rep.capevo_dk.xml.schemas._2007._08._01;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.oio.rep.capevo_dk.xml.schemas._2007._08._01 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _LocationDescriptionText_QNAME = new QName("http://rep.oio.dk/capevo.dk/xml/schemas/2007/08/01/", "LocationDescriptionText");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.oio.rep.capevo_dk.xml.schemas._2007._08._01
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/capevo.dk/xml/schemas/2007/08/01/", name = "LocationDescriptionText")
    public JAXBElement<String> createLocationDescriptionText(String value) {
        return new JAXBElement<String>(_LocationDescriptionText_QNAME, String.class, null, value);
    }

}
