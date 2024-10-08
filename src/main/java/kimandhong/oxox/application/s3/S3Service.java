package kimandhong.oxox.application.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kimandhong.oxox.application.handler.error.CustomException;
import kimandhong.oxox.application.handler.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class S3Service {
  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String uploadFile(final MultipartFile file, final S3path path) {
    try {
      final String fileName = path.getValue() + file.getOriginalFilename();

      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(file.getContentType());
      metadata.setContentLength(file.getSize());

      PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
          .withCannedAcl(CannedAccessControlList.PublicRead);
      amazonS3.putObject(putObjectRequest);

      return amazonS3.getUrl(bucket, fileName).toString();
    } catch (IOException e) {
      throw new CustomException(ErrorCode.S3_UPLOAD_FAIL, e);
    }
  }

  public String changeFile(final String imageAddress, final MultipartFile file, S3path path) {
    try {
      if (imageAddress != null) {
        this.deleteFile(imageAddress);
      }
      final String fileName = path.getValue() + file.getOriginalFilename();

      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(file.getContentType());
      metadata.setContentLength(file.getSize());

      PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
          .withCannedAcl(CannedAccessControlList.PublicRead);
      amazonS3.putObject(putObjectRequest);

      return amazonS3.getUrl(bucket, fileName).toString();
    } catch (IOException e) {
      throw new CustomException(ErrorCode.S3_UPLOAD_FAIL, e);
    }
  }

  public void deleteFile(String imageAddress) {
    String key = getKeyFromImageAddress(imageAddress);
    try {
      amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
    } catch (Exception e) {
      throw new CustomException(ErrorCode.S3_DELETE_FAIL, e);
    }
  }

  private String getKeyFromImageAddress(final String imageAddress) {
    try {
      URL url = new URL(imageAddress);
      String decodingKey = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
      return decodingKey.substring(1); // 맨 앞의 '/' 제거
    } catch (MalformedURLException e) {
      throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, e);
    }
  }
}
