
package oio.sagdok.person._1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import dk.oio.rep.cpr_dk.xml.schemas._2008._05._01.AddressCompleteGreenlandType;


/**
 * <p>Java class for GroenlandAdresseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GroenlandAdresseType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oio:sagdok:person:1.0.0}AdresseBaseType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/2008/05/01/}AddressCompleteGreenland" minOccurs="0"/>
 *         &lt;element name="SpecielVejkodeIndikator" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GroenlandAdresseType", propOrder = {
    "addressCompleteGreenland",
    "specielVejkodeIndikator"
})
public class GroenlandAdresseType
    extends AdresseBaseType
{

    @XmlElement(name = "AddressCompleteGreenland", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/2008/05/01/")
    protected AddressCompleteGreenlandType addressCompleteGreenland;
    @XmlElement(name = "SpecielVejkodeIndikator")
    protected Boolean specielVejkodeIndikator;

    /**
     * Gets the value of the addressCompleteGreenland property.
     * 
     * @return
     *     possible object is
     *     {@link AddressCompleteGreenlandType }
     *     
     */
    public AddressCompleteGreenlandType getAddressCompleteGreenland() {
        return addressCompleteGreenland;
    }

    /**
     * Sets the value of the addressCompleteGreenland property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressCompleteGreenlandType }
     *     
     */
    public void setAddressCompleteGreenland(AddressCompleteGreenlandType value) {
        this.addressCompleteGreenland = value;
    }

    /**
     * Gets the value of the specielVejkodeIndikator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSpecielVejkodeIndikator() {
        return specielVejkodeIndikator;
    }

    /**
     * Sets the value of the specielVejkodeIndikator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSpecielVejkodeIndikator(Boolean value) {
        this.specielVejkodeIndikator = value;
    }

}
