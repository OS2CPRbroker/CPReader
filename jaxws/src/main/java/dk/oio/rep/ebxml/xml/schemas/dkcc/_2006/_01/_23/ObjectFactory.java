
package dk.oio.rep.ebxml.xml.schemas.dkcc._2006._01._23;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.oio.rep.ebxml.xml.schemas.dkcc._2006._01._23 package. 
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

    private final static QName _PersonGenderCode_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2006/01/23/", "PersonGenderCode");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.oio.rep.ebxml.xml.schemas.dkcc._2006._01._23
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonGenderCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2006/01/23/", name = "PersonGenderCode")
    public JAXBElement<PersonGenderCodeType> createPersonGenderCode(PersonGenderCodeType value) {
        return new JAXBElement<PersonGenderCodeType>(_PersonGenderCode_QNAME, PersonGenderCodeType.class, null, value);
    }

}
