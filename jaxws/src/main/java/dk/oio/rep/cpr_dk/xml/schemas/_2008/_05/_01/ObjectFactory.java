
package dk.oio.rep.cpr_dk.xml.schemas._2008._05._01;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.oio.rep.cpr_dk.xml.schemas._2008._05._01 package. 
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

    private final static QName _ForeignAddressStructure_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/2008/05/01/", "ForeignAddressStructure");
    private final static QName _AddressCompleteGreenland_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/2008/05/01/", "AddressCompleteGreenland");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.oio.rep.cpr_dk.xml.schemas._2008._05._01
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ForeignAddressStructureType }
     * 
     */
    public ForeignAddressStructureType createForeignAddressStructureType() {
        return new ForeignAddressStructureType();
    }

    /**
     * Create an instance of {@link AddressCompleteGreenlandType }
     * 
     */
    public AddressCompleteGreenlandType createAddressCompleteGreenlandType() {
        return new AddressCompleteGreenlandType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ForeignAddressStructureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/2008/05/01/", name = "ForeignAddressStructure")
    public JAXBElement<ForeignAddressStructureType> createForeignAddressStructure(ForeignAddressStructureType value) {
        return new JAXBElement<ForeignAddressStructureType>(_ForeignAddressStructure_QNAME, ForeignAddressStructureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressCompleteGreenlandType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/2008/05/01/", name = "AddressCompleteGreenland")
    public JAXBElement<AddressCompleteGreenlandType> createAddressCompleteGreenland(AddressCompleteGreenlandType value) {
        return new JAXBElement<AddressCompleteGreenlandType>(_AddressCompleteGreenland_QNAME, AddressCompleteGreenlandType.class, null, value);
    }

}