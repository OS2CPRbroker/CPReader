
package dk.oio.rep.ebxml.xml.schemas.dkcc._2005._05._19;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.oio.rep.ebxml.xml.schemas.dkcc._2005._05._19 package. 
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

    private final static QName _PostalAddressThirdLineText_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/", "PostalAddressThirdLineText");
    private final static QName _PostalAddressFirstLineText_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/", "PostalAddressFirstLineText");
    private final static QName _PostalAddressSecondLineText_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/", "PostalAddressSecondLineText");
    private final static QName _PostalAddressFifthLineText_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/", "PostalAddressFifthLineText");
    private final static QName _PostalAddressFourthLineText_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/", "PostalAddressFourthLineText");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.oio.rep.ebxml.xml.schemas.dkcc._2005._05._19
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/", name = "PostalAddressThirdLineText")
    public JAXBElement<String> createPostalAddressThirdLineText(String value) {
        return new JAXBElement<String>(_PostalAddressThirdLineText_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/", name = "PostalAddressFirstLineText")
    public JAXBElement<String> createPostalAddressFirstLineText(String value) {
        return new JAXBElement<String>(_PostalAddressFirstLineText_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/", name = "PostalAddressSecondLineText")
    public JAXBElement<String> createPostalAddressSecondLineText(String value) {
        return new JAXBElement<String>(_PostalAddressSecondLineText_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/", name = "PostalAddressFifthLineText")
    public JAXBElement<String> createPostalAddressFifthLineText(String value) {
        return new JAXBElement<String>(_PostalAddressFifthLineText_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/", name = "PostalAddressFourthLineText")
    public JAXBElement<String> createPostalAddressFourthLineText(String value) {
        return new JAXBElement<String>(_PostalAddressFourthLineText_QNAME, String.class, null, value);
    }

}
