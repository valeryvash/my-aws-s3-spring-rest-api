package net.valeryvash.myawss3springrestapi.controller;

import net.valeryvash.myawss3springrestapi.dto.FilePostResponseDTO;
import net.valeryvash.myawss3springrestapi.model.Event;
import net.valeryvash.myawss3springrestapi.model.EventType;
import net.valeryvash.myawss3springrestapi.model.File;
import net.valeryvash.myawss3springrestapi.model.User;
import net.valeryvash.myawss3springrestapi.service.FileService;
import net.valeryvash.myawss3springrestapi.service.StorageService;
import net.valeryvash.myawss3springrestapi.service.UserService;
import org.apache.coyote.Response;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/files/")
public class FileControllerV1 {

    private final FileService fileService;

    private final UserService userService;

    private final StorageService storageService;

    public FileControllerV1(FileService fileService, UserService userService, StorageService storageService) {
        this.fileService = fileService;
        this.userService = userService;
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<FilePostResponseDTO> uploadFile(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("fileName") String fileName) {
        String userName = getLoggedUser_UserName();

        User loggedUser = userService.findByUserName(userName);

        storageService.uploadFile(multipartFile, fileName);

        File file = new File();

        file.setFileName(fileName);
        file.getEvent().setFile(file);
        file.getEvent().setUser(loggedUser);
        loggedUser.getEvents().add(file.getEvent());

        file = fileService.addFile(file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(FilePostResponseDTO.fromFile(file));

    }

    @GetMapping("{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("fileName") String fileName) {
        byte[] array = storageService.getFile(fileName);

        if (array.length != 0) {
            ByteArrayResource byteArrayResource = new ByteArrayResource(array);

            String userName = getLoggedUser_UserName();

            User loggedUser = userService.findByUserName(userName);

            File downloadFile = new File();

            downloadFile.setFileName(fileName);

            downloadFile.getEvent().setEventType(EventType.DOWNLOADED);
            downloadFile.getEvent().setFile(downloadFile);
            downloadFile.getEvent().setUser(loggedUser);
            loggedUser.getEvents().add(downloadFile.getEvent());

            fileService.addFile(downloadFile);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentLength(byteArrayResource.contentLength())
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(byteArrayResource);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Requested file not found. File name: %s", fileName)
            );
        }

    }

    @DeleteMapping("{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable("fileName") String fileName) {

        boolean isDeleteSuccessfully = storageService.deleteFile(fileName);

        if (isDeleteSuccessfully) {

            String userName = getLoggedUser_UserName();

            User loggedUser = userService.findByUserName(userName);

            File downloadFile = new File();

            downloadFile.setFileName(fileName);

            downloadFile.getEvent().setEventType(EventType.DELETED);
            downloadFile.getEvent().setFile(downloadFile);
            downloadFile.getEvent().setUser(loggedUser);
            loggedUser.getEvents().add(downloadFile.getEvent());

            fileService.addFile(downloadFile);

            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(String.format("File successfully deleted. File name is %s",fileName));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(String.format("File with file name %s not found",fileName));
        }
    }

    private String getLoggedUser_UserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }

    @ExceptionHandler(RuntimeException.class)
    public void handleFileControllerRuntimeExceptions(RuntimeException e) {
        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                String.format("Error in FileController occurred. Message: %s ", e.getMessage()),
                e.getCause()
        );
    }
}
