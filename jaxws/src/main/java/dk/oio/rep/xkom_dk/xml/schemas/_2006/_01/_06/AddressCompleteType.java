
package dk.oio.rep.xkom_dk.xml.schemas._2006._01._06;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import dk.oio.rep.xkom_dk.xml.schemas._2005._03._15.AddressAccessType;


/**
 * <p>Java class for AddressCompleteType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressCompleteType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/}AddressAccess" minOccurs="0"/>
 *         &lt;element name="AddressPostal" type="{http://rep.oio.dk/xkom.dk/xml/schemas/2006/01/06/}AddressPostalType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressCompleteType", propOrder = {
    "addressAccess",
    "addressPostal"
})
public class AddressCompleteType {

    @XmlElement(name = "AddressAccess", namespace = "http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/")
    protected AddressAccessType addressAccess;
    @XmlElement(name = "AddressPostal")
    protected AddressPostalType addressPostal;

    /**
     * Gets the value of the addressAccess property.
     * 
     * @return
     *     possible object is
     *     {@link AddressAccessType }
     *     
     */
    public AddressAccessType getAddressAccess() {
        return addressAccess;
    }

    /**
     * Sets the value of the addressAccess property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressAccessType }
     *     
     */
    public void setAddressAccess(AddressAccessType value) {
        this.addressAccess = value;
    }

    /**
     * Gets the value of the addressPostal property.
     * 
     * @return
     *     possible object is
     *     {@link AddressPostalType }
     *     
     */
    public AddressPostalType getAddressPostal() {
        return addressPostal;
    }

    /**
     * Sets the value of the addressPostal property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressPostalType }
     *     
     */
    public void setAddressPostal(AddressPostalType value) {
        this.addressPostal = value;
    }

}
