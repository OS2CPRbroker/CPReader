
package oio.sagdok.person._1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import oio.sagdok._2_0.VirkningType;


/**
 * <p>Java class for RegisterOplysningType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegisterOplysningType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oio:sagdok:person:1.0.0}RegisterOplysningBaseType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:sagdok:2.0.0}Virkning" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisterOplysningType", propOrder = {
    "virkning"
})
public class RegisterOplysningType
    extends RegisterOplysningBaseType
{

    @XmlElement(name = "Virkning", namespace = "urn:oio:sagdok:2.0.0")
    protected VirkningType virkning;

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
