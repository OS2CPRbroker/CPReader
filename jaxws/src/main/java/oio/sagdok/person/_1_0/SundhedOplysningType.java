
package oio.sagdok.person._1_0;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import oio.sagdok._2_0.VirkningType;


/**
 * <p>Java class for SundhedOplysningType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SundhedOplysningType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PraktiserendeLaegeNavn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PraktiserendeLaegeYderNummerIdentifikator" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="SygesikringsgruppeKode" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:sagdok:2.0.0}Virkning" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SundhedOplysningType", propOrder = {
    "praktiserendeLaegeNavn",
    "praktiserendeLaegeYderNummerIdentifikator",
    "sygesikringsgruppeKode",
    "virkning"
})
public class SundhedOplysningType {

    @XmlElement(name = "PraktiserendeLaegeNavn")
    protected String praktiserendeLaegeNavn;
    @XmlElement(name = "PraktiserendeLaegeYderNummerIdentifikator")
    protected BigInteger praktiserendeLaegeYderNummerIdentifikator;
    @XmlElement(name = "SygesikringsgruppeKode")
    protected BigInteger sygesikringsgruppeKode;
    @XmlElement(name = "Virkning", namespace = "urn:oio:sagdok:2.0.0")
    protected VirkningType virkning;

    /**
     * Gets the value of the praktiserendeLaegeNavn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPraktiserendeLaegeNavn() {
        return praktiserendeLaegeNavn;
    }

    /**
     * Sets the value of the praktiserendeLaegeNavn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPraktiserendeLaegeNavn(String value) {
        this.praktiserendeLaegeNavn = value;
    }

    /**
     * Gets the value of the praktiserendeLaegeYderNummerIdentifikator property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPraktiserendeLaegeYderNummerIdentifikator() {
        return praktiserendeLaegeYderNummerIdentifikator;
    }

    /**
     * Sets the value of the praktiserendeLaegeYderNummerIdentifikator property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPraktiserendeLaegeYderNummerIdentifikator(BigInteger value) {
        this.praktiserendeLaegeYderNummerIdentifikator = value;
    }

    /**
     * Gets the value of the sygesikringsgruppeKode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSygesikringsgruppeKode() {
        return sygesikringsgruppeKode;
    }

    /**
     * Sets the value of the sygesikringsgruppeKode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSygesikringsgruppeKode(BigInteger value) {
        this.sygesikringsgruppeKode = value;
    }

    /**
     * Gets the value of the virkning property.
     * 
     * @return
     *     possible object is
     *     {@link VirkningType }
     *     
     */
    public VirkningType getVirkning() {
        return virkning;
    }

    /**
     * Sets the value of the virkning property.
     * 
     * @param value
     *     allowed object is
     *     {@link VirkningType }
     *     
     */
    public void setVirkning(VirkningType value) {
        this.virkning = value;
    }

}
