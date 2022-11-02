package net.valeryvash.myawss3springrestapi.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@Slf4j
public class FileUploadExceptionAdviceV1 {

    @ExceptionHandler(MultipartException.class)
    public void handleMultipartException(MultipartException e){
        log.error("Request isn't multipart request. Exception class message: {}",e.getMessage());

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Request isn't multipart request",
                e
        );
    }



}
