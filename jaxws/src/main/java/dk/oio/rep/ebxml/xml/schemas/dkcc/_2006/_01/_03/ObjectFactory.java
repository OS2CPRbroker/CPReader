
package dk.oio.rep.ebxml.xml.schemas.dkcc._2006._01._03;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import dk.oio.rep.ebxml.xml.schemas.dkcc._2003._02._13.CountryIdentificationCodeType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.oio.rep.ebxml.xml.schemas.dkcc._2006._01._03 package. 
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

    private final static QName _PersonNationalityCode_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2006/01/03/", "PersonNationalityCode");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.oio.rep.ebxml.xml.schemas.dkcc._2006._01._03
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountryIdentificationCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2006/01/03/", name = "PersonNationalityCode")
    public JAXBElement<CountryIdentificationCodeType> createPersonNationalityCode(CountryIdentificationCodeType value) {
        return new JAXBElement<CountryIdentificationCodeType>(_PersonNationalityCode_QNAME, CountryIdentificationCodeType.class, null, value);
    }

}
