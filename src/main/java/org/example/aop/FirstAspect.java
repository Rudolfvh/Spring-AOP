package org.example.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import org.example.dto.UserCreateEditDto;
import org.example.http.exeption.ImageSizeException;
import org.springframework.stereotype.Component;

@Aspect // создаем аспект, в котором будем описывать
@Slf4j
@Component
public class FirstAspect {

    @Pointcut("execution(public * org.example.service.UserService.create(*))")
    public void isUserServiceCreateMethod() {
    }

    @Pointcut("execution(public * org.example.service.UserService.update(..))")
    public void isUserServiceUpdateMethod() {
    }

    @Pointcut("execution(public * org.example.http.controller.UserController.create(*))")
    public void isUserControllerCreateMethod() {
    }

    @Pointcut("execution(public * org.example.http.controller.UserController.update(..))")
    public void isUserControllerUpdateMethod() {
    }

    @Before("isUserControllerCreateMethod() && args(param)")
    public void checkImageSizeCreateMethod(Object param) {
        var userDto = (UserCreateEditDto) param;
        log.info("Size of load image {} bytes", userDto.getImage().getSize());
        if (userDto.getImage().getSize() > 1000) {
            throw new ImageSizeException();
        }
    }

    @Before("isUserControllerUpdateMethod()")
    public void checkImageSizeUpdateMethod(JoinPoint joinPoint) {
        var userDto = (UserCreateEditDto) joinPoint.getArgs()[1];
        log.info("Size of load image {} bytes", userDto.getImage().getSize());
        if (userDto.getImage().getSize() > 1000) {
            throw new ImageSizeException();
        }
    }

    @Before("isUserServiceCreateMethod() && args(param)")
    public void addLoggingCreateMethod(Object param) {
        log.info("invoke create method with param {}", param);
    }

    @Before(value = "isUserServiceUpdateMethod()")
    public void addLoggingUpdateMethod(JoinPoint joinPoint) {
        log.info("Invoke update method with param id {} and {}", joinPoint.getArgs()[0], joinPoint.getArgs()[1]);
    }

    @AfterReturning(value = "isUserServiceCreateMethod() && target(service)", returning = "result", argNames = "result,service")
    public void addLoggingAfterReturningCreateMethod(Object result,Object service) {
        log.info("After returning create method in class {}, with result {}", service, result);
    }

    @AfterReturning(value = "isUserServiceUpdateMethod() && target(service)", returning = "result", argNames = "result,service")
    public void addLoggingAfterReturningUpdateMethod(Object result,Object service) {
        log.info("After returning update method in class {}, with result {}", service, result);
    }

}
