
package oio.sagdok.person._1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import dk.oio.rep.itst_dk.xml.schemas._2006._01._17.PersonNameStructureType;


/**
 * <p>Java class for NavnStrukturType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NavnStrukturType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/}PersonNameStructure" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:sagdok:2.0.0}KaldenavnTekst" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/itst.dk/xml/schemas/2005/02/22/}PersonNameForAddressingName" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:sagdok:2.0.0}NoteTekst" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NavnStrukturType", propOrder = {
    "personNameStructure",
    "kaldenavnTekst",
    "personNameForAddressingName",
    "noteTekst"
})
public class NavnStrukturType {

    @XmlElement(name = "PersonNameStructure", namespace = "http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/")
    protected PersonNameStructureType personNameStructure;
    @XmlElement(name = "KaldenavnTekst", namespace = "urn:oio:sagdok:2.0.0")
    protected String kaldenavnTekst;
    @XmlElement(name = "PersonNameForAddressingName", namespace = "http://rep.oio.dk/itst.dk/xml/schemas/2005/02/22/")
    protected String personNameForAddressingName;
    @XmlElement(name = "NoteTekst", namespace = "urn:oio:sagdok:2.0.0")
    protected String noteTekst;

    /**
     * Gets the value of the personNameStructure property.
     * 
     * @return
     *     possible object is
     *     {@link PersonNameStructureType }
     *     
     */
    public PersonNameStructureType getPersonNameStructure() {
        return personNameStructure;
    }

    /**
     * Sets the value of the personNameStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonNameStructureType }
     *     
     */
    public void setPersonNameStructure(PersonNameStructureType value) {
        this.personNameStructure = value;
    }

    /**
     * Gets the value of the kaldenavnTekst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKaldenavnTekst() {
        return kaldenavnTekst;
    }

    /**
     * Sets the value of the kaldenavnTekst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKaldenavnTekst(String value) {
        this.kaldenavnTekst = value;
    }

    /**
     * Gets the value of the personNameForAddressingName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonNameForAddressingName() {
        return personNameForAddressingName;
    }

    /**
     * Sets the value of the personNameForAddressingName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonNameForAddressingName(String value) {
        this.personNameForAddressingName = value;
    }

    /**
     * Gets the value of the noteTekst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoteTekst() {
        return noteTekst;
    }

    /**
     * Sets the value of the noteTekst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoteTekst(String value) {
        this.noteTekst = value;
    }

}
