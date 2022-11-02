package net.valeryvash.myawss3springrestapi.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface StorageService {

    boolean uploadFile(MultipartFile file, String fileName);

    byte[] getFile(String fileName);

    boolean deleteFile(String fileName);
}
