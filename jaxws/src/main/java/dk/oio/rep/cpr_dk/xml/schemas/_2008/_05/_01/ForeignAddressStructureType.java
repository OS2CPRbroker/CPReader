
package dk.oio.rep.cpr_dk.xml.schemas._2008._05._01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import dk.oio.rep.ebxml.xml.schemas.dkcc._2003._02._13.CountryIdentificationCodeType;


/**
 * <p>Java class for ForeignAddressStructureType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ForeignAddressStructureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/}PostalAddressFirstLineText" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/}PostalAddressSecondLineText" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/}PostalAddressThirdLineText" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/}PostalAddressFourthLineText" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/}PostalAddressFifthLineText" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/}CountryIdentificationCode" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/capevo.dk/xml/schemas/2007/08/01/}LocationDescriptionText" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ForeignAddressStructureType", propOrder = {
    "postalAddressFirstLineText",
    "postalAddressSecondLineText",
    "postalAddressThirdLineText",
    "postalAddressFourthLineText",
    "postalAddressFifthLineText",
    "countryIdentificationCode",
    "locationDescriptionText"
})
public class ForeignAddressStructureType {

    @XmlElement(name = "PostalAddressFirstLineText", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/")
    protected String postalAddressFirstLineText;
    @XmlElement(name = "PostalAddressSecondLineText", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/")
    protected String postalAddressSecondLineText;
    @XmlElement(name = "PostalAddressThirdLineText", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/")
    protected String postalAddressThirdLineText;
    @XmlElement(name = "PostalAddressFourthLineText", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/")
    protected String postalAddressFourthLineText;
    @XmlElement(name = "PostalAddressFifthLineText", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/19/")
    protected String postalAddressFifthLineText;
    @XmlElement(name = "CountryIdentificationCode", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/")
    protected CountryIdentificationCodeType countryIdentificationCode;
    @XmlElement(name = "LocationDescriptionText", namespace = "http://rep.oio.dk/capevo.dk/xml/schemas/2007/08/01/")
    protected String locationDescriptionText;

    /**
     * Gets the value of the postalAddressFirstLineText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalAddressFirstLineText() {
        return postalAddressFirstLineText;
    }

    /**
     * Sets the value of the postalAddressFirstLineText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalAddressFirstLineText(String value) {
        this.postalAddressFirstLineText = value;
    }

    /**
     * Gets the value of the postalAddressSecondLineText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalAddressSecondLineText() {
        return postalAddressSecondLineText;
    }

    /**
     * Sets the value of the postalAddressSecondLineText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalAddressSecondLineText(String value) {
        this.postalAddressSecondLineText = value;
    }

    /**
     * Gets the value of the postalAddressThirdLineText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalAddressThirdLineText() {
        return postalAddressThirdLineText;
    }

    /**
     * Sets the value of the postalAddressThirdLineText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalAddressThirdLineText(String value) {
        this.postalAddressThirdLineText = value;
    }

    /**
     * Gets the value of the postalAddressFourthLineText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalAddressFourthLineText() {
        return postalAddressFourthLineText;
    }

    /**
     * Sets the value of the postalAddressFourthLineText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalAddressFourthLineText(String value) {
        this.postalAddressFourthLineText = value;
    }

    /**
     * Gets the value of the postalAddressFifthLineText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalAddressFifthLineText() {
        return postalAddressFifthLineText;
    }

    /**
     * Sets the value of the postalAddressFifthLineText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalAddressFifthLineText(String value) {
        this.postalAddressFifthLineText = value;
    }

    /**
     * Gets the value of the countryIdentificationCode property.
     * 
     * @return
     *     possible object is
     *     {@link CountryIdentificationCodeType }
     *     
     */
    public CountryIdentificationCodeType getCountryIdentificationCode() {
        return countryIdentificationCode;
    }

    /**
     * Sets the value of the countryIdentificationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountryIdentificationCodeType }
     *     
     */
    public void setCountryIdentificationCode(CountryIdentificationCodeType value) {
        this.countryIdentificationCode = value;
    }

    /**
     * Gets the value of the locationDescriptionText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationDescriptionText() {
        return locationDescriptionText;
    }

    /**
     * Sets the value of the locationDescriptionText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationDescriptionText(String value) {
        this.locationDescriptionText = value;
    }

}
