
package oio.sagdok.person._1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for KontaktKanalBaseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="KontaktKanalBaseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BegraensetAnvendelseTekst" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "KontaktKanalBaseType", propOrder = {
    "begraensetAnvendelseTekst",
    "noteTekst"
})
@XmlSeeAlso({
    KontaktKanalType.class
})
public class KontaktKanalBaseType {

    @XmlElement(name = "BegraensetAnvendelseTekst")
    protected String begraensetAnvendelseTekst;
    @XmlElement(name = "NoteTekst", namespace = "urn:oio:sagdok:2.0.0")
    protected String noteTekst;

    /**
     * Gets the value of the begraensetAnvendelseTekst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBegraensetAnvendelseTekst() {
        return begraensetAnvendelseTekst;
    }

    /**
     * Sets the value of the begraensetAnvendelseTekst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBegraensetAnvendelseTekst(String value) {
        this.begraensetAnvendelseTekst = value;
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
