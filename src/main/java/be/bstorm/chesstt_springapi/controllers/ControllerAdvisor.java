package be.bstorm.chesstt_springapi.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;

@CrossOrigin("*")
@ControllerAdvice
@Slf4j
public class ControllerAdvisor {

    //Todo MailSendException
    //Todo UserException
    //Todo UserNotFoundException
    //Todo UserAlreadyExistException
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handlePrecondition(Exception e){
        log.error("Error",e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
