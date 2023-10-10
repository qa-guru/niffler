package guru.qa.niffler.db.mapper;

import guru.qa.niffler.db.model.userdata.UserDataEntity;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import org.apache.commons.io.FileUtils;

public class UserdataHelper {

//  public String getEncodingPhoto(UserDataEntity userData) {
//    ClassLoader classLoader = getClass().getClassLoader();
//    byte[] fileContent = new byte[0];
//    try {
//      fileContent = FileUtils.readFileToByteArray(new File(classLoader
//          .getResource(userData.getPhoto())
//          .getFile()));
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
//    return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileContent);
//  }

}
