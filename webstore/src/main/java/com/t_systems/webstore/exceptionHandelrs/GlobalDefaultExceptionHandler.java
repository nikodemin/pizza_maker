package com.t_systems.webstore.exceptionHandelrs;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = {"com.t_systems.webstore.controller.controller",
        "com.t_systems.webstore.service"})
@Log4j2
public class GlobalDefaultExceptionHandler {
    /**
     * global exception handler
     * @param model model
     * @param e exception
     * @return internal page name
     * @throws Exception ex
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public String defaultExceptionHandler(Model model, Exception e) throws Exception{
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;
        log.error(e.getMessage(),e);
        model.addAttribute("text", "500 Internal server error");
        return "text";
    }
}
