package net.valeryvash.myawss3springrestapi;


import net.valeryvash.myawss3springrestapi.model.Role;
import net.valeryvash.myawss3springrestapi.repository.RoleRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MyAwsS3SpringRestApiApplicationTests {


    @Autowired
    RoleRepo roleRepo;
    @Autowired
    S3Client s3Client;

    @Test
    void contextLoads() {
        String bucketName = "my-first-backet";

        List<ObjectIdentifier> keys = new ArrayList<>();

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();
        ListObjectsV2Iterable responses = s3Client.listObjectsV2Paginator(request);

        for (ListObjectsV2Response page : responses) {
            page.contents().forEach(
                    s3Object -> {
                        keys.add(ObjectIdentifier.builder()
                                        .key(s3Object.key())
                                .build());
                        System.out.println(s3Object.key());
                    }
            );
        }

        Delete delete = Delete.builder()
                .objects(keys)
                .build();

        DeleteObjectsRequest deleteObjectRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(delete)
                .build();

        s3Client.deleteObjects(deleteObjectRequest);


    }

    @Test
    void amazonS3ClientTest() throws IOException {
        File file = new File("src/main/resources/application.yml");

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket("my-first-backet")
                .key(file.getName())
                .build();

        PutObjectResponse response = s3Client.putObject(request, RequestBody.fromFile(file));

        if (response.sdkHttpResponse().isSuccessful()) {
            System.out.println("File successfully added");
        } else {
            System.err.println("File upload error");
        }

        System.out.println(response.eTag());

        System.out.println(response.toString());

        System.out.println(response.versionId());

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("my-first-backet")
                .key("application.yml")
                .build();

        ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getObjectRequest);

        File outputFile = new File("src/main/resources/2application.yml2");

        new FileOutputStream(outputFile).write(responseBytes.asByteArray());

        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                .sourceBucket("my-first-backet")
                .sourceKey(file.getName())
                .destinationBucket("my-first-backet")
                .destinationKey(file.getName()+"1")
                .build();

        CopyObjectResponse copyObjectResponse = s3Client.copyObject(copyObjectRequest);

        if (copyObjectResponse.sdkHttpResponse().isSuccessful()) {
            System.out.println("Copy operation is successfully");
        } else {
            System.out.println("Copy operation went wrong");
        }

        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                .bucket("my-first-backet")
                .build();

        ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjectsRequest);
        List<S3Object> s3Objects = listObjectsResponse.contents();

        System.out.println("Listing start");
        s3Objects.forEach(object -> System.out.println(object.key()));
        System.out.println("Listing end");

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket("my-first-backet")
                .key(file.getName())
                .build();

        DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(deleteObjectRequest);

        System.out.println(deleteObjectResponse.sdkHttpResponse().isSuccessful());

    }

    @Test
    void repoTest() {

        Role role;

        role = roleRepo.findByRoleName("ROLE_USER");

        System.out.println(role.getId());
        System.out.println(role.getRoleName());
        System.out.println(role.getCreated());

        role = roleRepo.findByRoleName("USER");

        assertNull(role);

        role = roleRepo.findByRoleName(null);

        assertNull(role);

        List<Role> list = roleRepo.findByUsers_UserName("1230");

        assertNotNull(list);

        list = roleRepo.findByUsers_UserName("55555555");
        assertTrue(list.isEmpty());

        list = roleRepo.findByUsers_UserName(null);
        assertTrue(list.isEmpty());

    }


}
