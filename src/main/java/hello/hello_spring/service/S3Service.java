package hello.hello_spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {
    private final S3Client s3Client;

    @Value("${aws.bucket}")
    private String bucket;

    @Value("${aws.region}") // 지역 정보를 추가로 주입
    private String region;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    public String uploadImage(MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename(); // 고유한 파일 이름 생성

        // PutObjectRequest 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(image.getContentType())
                .build();

        // S3에 파일 업로드
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(image.getInputStream(), image.getSize()));

        return getPublicUrl(fileName);
    }

    private String getPublicUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
    }

    public void deleteImage(String fileUrl) {
        String fileName = extractFileNameFromUrl(fileUrl); // URL에서 파일명 추출

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    private String extractFileNameFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}
