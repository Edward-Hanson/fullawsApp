package com.hanson.cloudbasedproject.cloudbasedproject.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public String handleMaxSizeException(MaxUploadSizeExceededException ex,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Upload failed: File size exceeds 2MB limit.");
        return "redirect:" + request.getHeader("Referer");
    }

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Oops! An error occurred 😒🤷‍♂️🤦‍♂️");
        return "redirect:" + request.getHeader("Referer");
    }
}