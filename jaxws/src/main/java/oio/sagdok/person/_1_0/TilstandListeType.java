
package oio.sagdok.person._1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import oio.sagdok._2_0.LokalUdvidelseType;


/**
 * <p>Java class for TilstandListeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TilstandListeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LivStatus" type="{urn:oio:sagdok:person:1.0.0}LivStatusType" minOccurs="0"/>
 *         &lt;element name="CivilStatus" type="{urn:oio:sagdok:person:1.0.0}CivilStatusType" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:sagdok:2.0.0}LokalUdvidelse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TilstandListeType", propOrder = {
    "livStatus",
    "civilStatus",
    "lokalUdvidelse"
})
public class TilstandListeType {

    @XmlElement(name = "LivStatus")
    protected LivStatusType livStatus;
    @XmlElement(name = "CivilStatus")
    protected CivilStatusType civilStatus;
    @XmlElement(name = "LokalUdvidelse", namespace = "urn:oio:sagdok:2.0.0")
    protected LokalUdvidelseType lokalUdvidelse;

    /**
     * Gets the value of the livStatus property.
     * 
     * @return
     *     possible object is
     *     {@link LivStatusType }
     *     
     */
    public LivStatusType getLivStatus() {
        return livStatus;
    }

    /**
     * Sets the value of the livStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link LivStatusType }
     *     
     */
    public void setLivStatus(LivStatusType value) {
        this.livStatus = value;
    }

    /**
     * Gets the value of the civilStatus property.
     * 
     * @return
     *     possible object is
     *     {@link CivilStatusType }
     *     
     */
    public CivilStatusType getCivilStatus() {
        return civilStatus;
    }

    /**
     * Sets the value of the civilStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link CivilStatusType }
     *     
     */
    public void setCivilStatus(CivilStatusType value) {
        this.civilStatus = value;
    }

    /**
     * Gets the value of the lokalUdvidelse property.
     * 
     * @return
     *     possible object is
     *     {@link LokalUdvidelseType }
     *     
     */
    public LokalUdvidelseType getLokalUdvidelse() {
        return lokalUdvidelse;
    }

    /**
     * Sets the value of the lokalUdvidelse property.
     * 
     * @param value
     *     allowed object is
     *     {@link LokalUdvidelseType }
     *     
     */
    public void setLokalUdvidelse(LokalUdvidelseType value) {
        this.lokalUdvidelse = value;
    }

}
