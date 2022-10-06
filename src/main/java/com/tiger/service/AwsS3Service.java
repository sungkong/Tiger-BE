package com.tiger.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tiger.exception.CustomException;
import com.tiger.exception.StatusCode;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * https://earth-95.tistory.com/117 참고
 */
@Service
@RequiredArgsConstructor
public class AwsS3Service {
    private static final int IMAGE_WIDTH = 800;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public List<String> uploadFile(List<MultipartFile> multipartFile) {
        List<String> fileUrlList = new ArrayList<>();

        multipartFile.forEach(file -> {
            try {
                String fileName = createFileName(file.getOriginalFilename());

                BufferedImage image = resizeImage(file, IMAGE_WIDTH);


                ObjectMetadata objectMetadata = new ObjectMetadata();
                String contentType = file.getContentType();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());

                ByteArrayInputStream inputStream = imageToInputStream(image, contentType, objectMetadata);

                amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                String url = amazonS3Client.getUrl(bucket, fileName).toString();

                fileUrlList.add(url);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return fileUrlList;
    }

    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        String fileExtension;
        try {
            fileExtension = fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
        if (!Arrays.asList(".jpg", ".png", ".jpeg", ".gif", ".bmp", ".jfif").contains(fileExtension.toLowerCase())) {
            throw new CustomException(StatusCode.BAD_IMAGE_INPUT);
        }
        return fileExtension;
    }

    // image resize
    private BufferedImage resizeImage(MultipartFile multipartFile, int targetWidth) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());

        if (bufferedImage == null) {
            throw new CustomException(StatusCode.BAD_IMAGE_INPUT);
        }

        if (bufferedImage.getWidth() < targetWidth) {
            return bufferedImage;
        }

        return Scalr.resize(bufferedImage, targetWidth);
    }

    // image to InputStream
    private ByteArrayInputStream imageToInputStream(BufferedImage bufferedImage, String contentType, ObjectMetadata objectMetadata) throws IOException {
        String fileExtension = contentType.split("/")[1];

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, fileExtension, byteArrayOutputStream);

        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(byteArrayOutputStream.size());

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }


}
