package guru.qa.niffler.service.converter;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialCreationOptions;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRequestOptions;
import org.springframework.security.web.webauthn.jackson.WebauthnJackson2Module;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Android should get property requireResidentKey and class PublicKeyCredentialCreationOptions does not provide it
 */
public class PublicKeyOptionsConverter extends MappingJackson2HttpMessageConverter {

  public PublicKeyOptionsConverter() {
    super(JsonMapper.builder().addModule(new WebauthnJackson2Module()).build());
  }

  @Override
  protected void writeInternal(Object object, @Nullable Type type, HttpOutputMessage outputMessage)
      throws IOException, HttpMessageNotWritableException {
    if (object instanceof PublicKeyCredentialCreationOptions) {
      ObjectNode root = defaultObjectMapper.valueToTree(object);
      ObjectNode sel = (ObjectNode) root.get("authenticatorSelection");
      sel.put("requireResidentKey", false);
      super.writeInternal(root, type, outputMessage);
    } else if (object instanceof PublicKeyCredentialRequestOptions) {
      ObjectNode root = defaultObjectMapper.valueToTree(object);
      root.remove("allowCredentials");
      root.remove("extensions");
      super.writeInternal(root, type, outputMessage);
    } else {
      super.writeInternal(object, type, outputMessage);
    }
  }
}
