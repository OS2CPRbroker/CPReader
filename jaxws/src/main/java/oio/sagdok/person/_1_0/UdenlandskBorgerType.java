
package oio.sagdok.person._1_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import dk.oio.rep.ebxml.xml.schemas.dkcc._2003._02._13.CountryIdentificationCodeType;


/**
 * <p>Java class for UdenlandskBorgerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UdenlandskBorgerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PersonIdentifikator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2006/01/03/}PersonNationalityCode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/2007/01/02/}PersonCivilRegistrationReplacementIdentifier" minOccurs="0"/>
 *         &lt;element name="SprogKode" type="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/}CountryIdentificationCodeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="FoedselslandKode" type="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/}CountryIdentificationCodeType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UdenlandskBorgerType", propOrder = {
    "personIdentifikator",
    "personNationalityCode",
    "personCivilRegistrationReplacementIdentifier",
    "sprogKode",
    "foedselslandKode"
})
public class UdenlandskBorgerType {

    @XmlElement(name = "PersonIdentifikator")
    protected String personIdentifikator;
    @XmlElement(name = "PersonNationalityCode", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2006/01/03/")
    protected List<CountryIdentificationCodeType> personNationalityCode;
    @XmlElement(name = "PersonCivilRegistrationReplacementIdentifier", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/2007/01/02/")
    protected String personCivilRegistrationReplacementIdentifier;
    @XmlElement(name = "SprogKode")
    protected List<CountryIdentificationCodeType> sprogKode;
    @XmlElement(name = "FoedselslandKode")
    protected CountryIdentificationCodeType foedselslandKode;

    /**
     * Gets the value of the personIdentifikator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonIdentifikator() {
        return personIdentifikator;
    }

    /**
     * Sets the value of the personIdentifikator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonIdentifikator(String value) {
        this.personIdentifikator = value;
    }

    /**
     * Gets the value of the personNationalityCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the personNationalityCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersonNationalityCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CountryIdentificationCodeType }
     * 
     * 
     */
    public List<CountryIdentificationCodeType> getPersonNationalityCode() {
        if (personNationalityCode == null) {
            personNationalityCode = new ArrayList<CountryIdentificationCodeType>();
        }
        return this.personNationalityCode;
    }

    /**
     * Gets the value of the personCivilRegistrationReplacementIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonCivilRegistrationReplacementIdentifier() {
        return personCivilRegistrationReplacementIdentifier;
    }

    /**
     * Sets the value of the personCivilRegistrationReplacementIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonCivilRegistrationReplacementIdentifier(String value) {
        this.personCivilRegistrationReplacementIdentifier = value;
    }

    /**
     * Gets the value of the sprogKode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sprogKode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSprogKode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CountryIdentificationCodeType }
     * 
     * 
     */
    public List<CountryIdentificationCodeType> getSprogKode() {
        if (sprogKode == null) {
            sprogKode = new ArrayList<CountryIdentificationCodeType>();
        }
        return this.sprogKode;
    }

    /**
     * Gets the value of the foedselslandKode property.
     * 
     * @return
     *     possible object is
     *     {@link CountryIdentificationCodeType }
     *     
     */
    public CountryIdentificationCodeType getFoedselslandKode() {
        return foedselslandKode;
    }

    /**
     * Sets the value of the foedselslandKode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountryIdentificationCodeType }
     *     
     */
    public void setFoedselslandKode(CountryIdentificationCodeType value) {
        this.foedselslandKode = value;
    }

}
