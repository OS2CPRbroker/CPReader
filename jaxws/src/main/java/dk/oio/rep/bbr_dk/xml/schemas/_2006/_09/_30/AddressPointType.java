
package dk.oio.rep.bbr_dk.xml.schemas._2006._09._30;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AddressPointType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressPointType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddressPointIdentifier" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="GeographicPointLocation" type="{http://rep.oio.dk/bbr.dk/xml/schemas/2006/09/30/}GeographicPointLocationType" minOccurs="0"/>
 *         &lt;element name="AddressPointStatusStructure" type="{http://rep.oio.dk/bbr.dk/xml/schemas/2006/09/30/}AddressPointStatusStructureType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressPointType", propOrder = {
    "addressPointIdentifier",
    "geographicPointLocation",
    "addressPointStatusStructure"
})
public class AddressPointType {

    @XmlElement(name = "AddressPointIdentifier")
    @XmlSchemaType(name = "anyURI")
    protected String addressPointIdentifier;
    @XmlElement(name = "GeographicPointLocation")
    protected GeographicPointLocationType geographicPointLocation;
    @XmlElement(name = "AddressPointStatusStructure")
    protected AddressPointStatusStructureType addressPointStatusStructure;

    /**
     * Gets the value of the addressPointIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressPointIdentifier() {
        return addressPointIdentifier;
    }

    /**
     * Sets the value of the addressPointIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressPointIdentifier(String value) {
        this.addressPointIdentifier = value;
    }

    /**
     * Gets the value of the geographicPointLocation property.
     * 
     * @return
     *     possible object is
     *     {@link GeographicPointLocationType }
     *     
     */
    public GeographicPointLocationType getGeographicPointLocation() {
        return geographicPointLocation;
    }

    /**
     * Sets the value of the geographicPointLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeographicPointLocationType }
     *     
     */
    public void setGeographicPointLocation(GeographicPointLocationType value) {
        this.geographicPointLocation = value;
    }

    /**
     * Gets the value of the addressPointStatusStructure property.
     * 
     * @return
     *     possible object is
     *     {@link AddressPointStatusStructureType }
     *     
     */
    public AddressPointStatusStructureType getAddressPointStatusStructure() {
        return addressPointStatusStructure;
    }

    /**
     * Sets the value of the addressPointStatusStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressPointStatusStructureType }
     *     
     */
    public void setAddressPointStatusStructure(AddressPointStatusStructureType value) {
        this.addressPointStatusStructure = value;
    }

}
