
package dk.oio.rep.ebxml.xml.schemas.dkcc._2005._03._15;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.oio.rep.ebxml.xml.schemas.dkcc._2005._03._15 package. 
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

    private final static QName _DistrictSubdivisionIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", "DistrictSubdivisionIdentifier");
    private final static QName _StreetName_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", "StreetName");
    private final static QName _PostCodeIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", "PostCodeIdentifier");
    private final static QName _DistrictName_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", "DistrictName");
    private final static QName _BirthDate_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", "BirthDate");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.oio.rep.ebxml.xml.schemas.dkcc._2005._03._15
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", name = "DistrictSubdivisionIdentifier")
    public JAXBElement<String> createDistrictSubdivisionIdentifier(String value) {
        return new JAXBElement<String>(_DistrictSubdivisionIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", name = "StreetName")
    public JAXBElement<String> createStreetName(String value) {
        return new JAXBElement<String>(_StreetName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", name = "PostCodeIdentifier")
    public JAXBElement<String> createPostCodeIdentifier(String value) {
        return new JAXBElement<String>(_PostCodeIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", name = "DistrictName")
    public JAXBElement<String> createDistrictName(String value) {
        return new JAXBElement<String>(_DistrictName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", name = "BirthDate")
    public JAXBElement<XMLGregorianCalendar> createBirthDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_BirthDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

}
