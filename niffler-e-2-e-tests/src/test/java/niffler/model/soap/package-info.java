@XmlSchema(
        namespace = "niffler-userdata",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(prefix = "gs", namespaceURI = "niffler-userdata")
        }
)

package niffler.model.soap;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;