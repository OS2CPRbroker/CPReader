
package dk.oio.rep.bbr_dk.xml.schemas._2006._09._30;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GeographicCoordinateTupleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GeographicCoordinateTupleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GeographicEastingMeasure" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="GeographicNorthingMeasure" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="GeographicHeightMeasure" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeographicCoordinateTupleType", propOrder = {
    "geographicEastingMeasure",
    "geographicNorthingMeasure",
    "geographicHeightMeasure"
})
public class GeographicCoordinateTupleType {

    @XmlElement(name = "GeographicEastingMeasure", required = true)
    protected BigDecimal geographicEastingMeasure;
    @XmlElement(name = "GeographicNorthingMeasure", required = true)
    protected BigDecimal geographicNorthingMeasure;
    @XmlElement(name = "GeographicHeightMeasure")
    protected BigDecimal geographicHeightMeasure;

    /**
     * Gets the value of the geographicEastingMeasure property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGeographicEastingMeasure() {
        return geographicEastingMeasure;
    }

    /**
     * Sets the value of the geographicEastingMeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGeographicEastingMeasure(BigDecimal value) {
        this.geographicEastingMeasure = value;
    }

    /**
     * Gets the value of the geographicNorthingMeasure property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGeographicNorthingMeasure() {
        return geographicNorthingMeasure;
    }

    /**
     * Sets the value of the geographicNorthingMeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGeographicNorthingMeasure(BigDecimal value) {
        this.geographicNorthingMeasure = value;
    }

    /**
     * Gets the value of the geographicHeightMeasure property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGeographicHeightMeasure() {
        return geographicHeightMeasure;
    }

    /**
     * Sets the value of the geographicHeightMeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGeographicHeightMeasure(BigDecimal value) {
        this.geographicHeightMeasure = value;
    }

}
