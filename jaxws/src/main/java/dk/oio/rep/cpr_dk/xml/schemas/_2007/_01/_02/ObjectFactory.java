
package dk.oio.rep.cpr_dk.xml.schemas._2007._01._02;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.oio.rep.cpr_dk.xml.schemas._2007._01._02 package. 
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

    private final static QName _PersonCivilRegistrationReplacementIdentifier_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/2007/01/02/", "PersonCivilRegistrationReplacementIdentifier");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.oio.rep.cpr_dk.xml.schemas._2007._01._02
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/2007/01/02/", name = "PersonCivilRegistrationReplacementIdentifier")
    public JAXBElement<String> createPersonCivilRegistrationReplacementIdentifier(String value) {
        return new JAXBElement<String>(_PersonCivilRegistrationReplacementIdentifier_QNAME, String.class, null, value);
    }

}
