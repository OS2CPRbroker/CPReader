
package dk.oio.rep.xkom_dk.xml.schemas._2006._01._06;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.oio.rep.xkom_dk.xml.schemas._2006._01._06 package. 
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

    private final static QName _AddressComplete_QNAME = new QName("http://rep.oio.dk/xkom.dk/xml/schemas/2006/01/06/", "AddressComplete");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.oio.rep.xkom_dk.xml.schemas._2006._01._06
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddressCompleteType }
     * 
     */
    public AddressCompleteType createAddressCompleteType() {
        return new AddressCompleteType();
    }

    /**
     * Create an instance of {@link AddressPostalType }
     * 
     */
    public AddressPostalType createAddressPostalType() {
        return new AddressPostalType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressCompleteType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/xkom.dk/xml/schemas/2006/01/06/", name = "AddressComplete")
    public JAXBElement<AddressCompleteType> createAddressComplete(AddressCompleteType value) {
        return new JAXBElement<AddressCompleteType>(_AddressComplete_QNAME, AddressCompleteType.class, null, value);
    }

}
