
package dk.oio.rep.ebxml.xml.schemas.dkcc._2003._02._13;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.oio.rep.ebxml.xml.schemas.dkcc._2003._02._13 package. 
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

    private final static QName _PersonSurnameName_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "PersonSurnameName");
    private final static QName _PersonMiddleName_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "PersonMiddleName");
    private final static QName _CountryIdentificationCode_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "CountryIdentificationCode");
    private final static QName _MailDeliverySublocationIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "MailDeliverySublocationIdentifier");
    private final static QName _PersonGivenName_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "PersonGivenName");
    private final static QName _FloorIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "FloorIdentifier");
    private final static QName _StreetBuildingIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "StreetBuildingIdentifier");
    private final static QName _SuiteIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "SuiteIdentifier");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.oio.rep.ebxml.xml.schemas.dkcc._2003._02._13
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CountryIdentificationCodeType }
     * 
     */
    public CountryIdentificationCodeType createCountryIdentificationCodeType() {
        return new CountryIdentificationCodeType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "PersonSurnameName")
    public JAXBElement<String> createPersonSurnameName(String value) {
        return new JAXBElement<String>(_PersonSurnameName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "PersonMiddleName")
    public JAXBElement<String> createPersonMiddleName(String value) {
        return new JAXBElement<String>(_PersonMiddleName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountryIdentificationCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "CountryIdentificationCode")
    public JAXBElement<CountryIdentificationCodeType> createCountryIdentificationCode(CountryIdentificationCodeType value) {
        return new JAXBElement<CountryIdentificationCodeType>(_CountryIdentificationCode_QNAME, CountryIdentificationCodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "MailDeliverySublocationIdentifier")
    public JAXBElement<String> createMailDeliverySublocationIdentifier(String value) {
        return new JAXBElement<String>(_MailDeliverySublocationIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "PersonGivenName")
    public JAXBElement<String> createPersonGivenName(String value) {
        return new JAXBElement<String>(_PersonGivenName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "FloorIdentifier")
    public JAXBElement<String> createFloorIdentifier(String value) {
        return new JAXBElement<String>(_FloorIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "StreetBuildingIdentifier")
    public JAXBElement<String> createStreetBuildingIdentifier(String value) {
        return new JAXBElement<String>(_StreetBuildingIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "SuiteIdentifier")
    public JAXBElement<String> createSuiteIdentifier(String value) {
        return new JAXBElement<String>(_SuiteIdentifier_QNAME, String.class, null, value);
    }

}