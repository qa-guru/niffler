package guru.qa.niffler.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class SmallPhoto {

  private static final Logger LOG = LoggerFactory.getLogger(SmallPhoto.class);

  private final int height;
  private final int width;
  private final double quality;
  @Nonnull
  private final String outputFormat;
  @Nullable
  private final String photo;

  public SmallPhoto(int height, int width, @Nullable String photo) {
    this(height, width, 1.0, "png", photo);
  }

  public SmallPhoto(int height, int width, @Nonnull String outputFormat, @Nullable String photo) {
    this(height, width, 1.0, outputFormat, photo);
  }

  public SmallPhoto(int height, int width, double quality, @Nonnull String outputFormat, @Nullable String photo) {
    this.height = height;
    this.width = width;
    this.quality = quality;
    this.outputFormat = outputFormat;
    this.photo = photo;
  }

  public @Nullable byte[] bytes() {
    if (photo != null) {
      try {
        String base64Image = photo.split(",")[1];

        try (ByteArrayInputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(base64Image));
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {

          Thumbnails.of(ImageIO.read(is))
              .height(height)
              .width(width)
              .outputQuality(quality)
              .outputFormat(outputFormat)
              .toOutputStream(os);

          return concatArrays(
              "data:image/png;base64,".getBytes(StandardCharsets.UTF_8),
              Base64.getEncoder().encode(os.toByteArray())
          );
        }
      } catch (Exception e) {
        LOG.error("### Error while resizing photo");
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  private @Nonnull byte[] concatArrays(@Nonnull byte[] first, @Nonnull byte[] second) {
    byte[] result = Arrays.copyOf(first, first.length + second.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }
}
