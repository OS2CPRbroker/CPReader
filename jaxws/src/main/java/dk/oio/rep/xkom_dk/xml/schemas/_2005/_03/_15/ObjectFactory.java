
package dk.oio.rep.xkom_dk.xml.schemas._2005._03._15;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.oio.rep.xkom_dk.xml.schemas._2005._03._15 package. 
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

    private final static QName _EmailAddressIdentifier_QNAME = new QName("http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/", "EmailAddressIdentifier");
    private final static QName _AddressAccess_QNAME = new QName("http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/", "AddressAccess");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.oio.rep.xkom_dk.xml.schemas._2005._03._15
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddressAccessType }
     * 
     */
    public AddressAccessType createAddressAccessType() {
        return new AddressAccessType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/", name = "EmailAddressIdentifier")
    public JAXBElement<String> createEmailAddressIdentifier(String value) {
        return new JAXBElement<String>(_EmailAddressIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressAccessType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/", name = "AddressAccess")
    public JAXBElement<AddressAccessType> createAddressAccess(AddressAccessType value) {
        return new JAXBElement<AddressAccessType>(_AddressAccess_QNAME, AddressAccessType.class, null, value);
    }

}
