package ru.otus.hw.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.dto.error.ApiError;
import ru.otus.hw.exceptions.EntityNotFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(EntityNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ModelAndView handleEntityNotFoundException(EntityNotFoundException e) {
//        log.error("Not found:", e);
//        ModelAndView modelAndView = new ModelAndView("404");
//        modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
//        modelAndView.addObject("error", e.getMessage());
//        return modelAndView;
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException e) {
        var errorMessage = new StringBuilder();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            String field = ((FieldError) error).getField();
            errorMessage.append(field).append(" ").append(error.getDefaultMessage()).append(", ");
        }
        return new ResponseEntity<>(
                new ApiError(
                        HttpStatus.BAD_REQUEST.value(),
                        errorMessage.substring(0, errorMessage.toString().length() - 2)
                ),
                HttpStatus.BAD_REQUEST
        );
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ModelAndView handleException(Exception e) {
//        log.error("Error:", e);
//        ModelAndView modelAndView = new ModelAndView("error");
//        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        modelAndView.addObject("error", e.getMessage());
//        return modelAndView;
//    }

}
