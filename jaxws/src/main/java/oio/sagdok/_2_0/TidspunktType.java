
package oio.sagdok._2_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for TidspunktType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TidspunktType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="GraenseIndikator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *           &lt;element ref="{urn:oio:sagdok:1.0.0}TidsstempelDatoTid"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TidspunktType", propOrder = {
    "graenseIndikator",
    "tidsstempelDatoTid"
})
public class TidspunktType {

    @XmlElement(name = "GraenseIndikator")
    protected Boolean graenseIndikator;
    @XmlElement(name = "TidsstempelDatoTid", namespace = "urn:oio:sagdok:1.0.0")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar tidsstempelDatoTid;

    /**
     * Gets the value of the graenseIndikator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGraenseIndikator() {
        return graenseIndikator;
    }

    /**
     * Sets the value of the graenseIndikator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGraenseIndikator(Boolean value) {
        this.graenseIndikator = value;
    }

    /**
     * Gets the value of the tidsstempelDatoTid property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTidsstempelDatoTid() {
        return tidsstempelDatoTid;
    }

    /**
     * Sets the value of the tidsstempelDatoTid property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTidsstempelDatoTid(XMLGregorianCalendar value) {
        this.tidsstempelDatoTid = value;
    }

}
