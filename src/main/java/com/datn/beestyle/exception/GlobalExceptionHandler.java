package com.datn.beestyle.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "404 Response",
                                    summary = "Handle exception when resource not found",
                                    value = """
                                            {
                                                "timestamp": "2023-10-19T06:07:35.321+00:00",
                                                "status": 404,
                                                "path": "/api/v1/...",
                                                "error": "Not Found",
                                                "message": "{data} not found"
                                            }
                                            """
                            ))})
    })
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        ErrorResponse errorResponse = this.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);

        log.error("Path: {} - Msg error: {}", errorResponse.getPath(), errorResponse.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "409 Response",
                                    summary = "Handle exception when input data is conflicted",
                                    value = """
                                                {
                                                    "timestamp": "2023-10-19T06:07:35.321+00:00",
                                                    "status": 409,
                                                    "path": "/api/v1/...",
                                                    "error": "Conflict",
                                                    "message": "{data} exists, Please try again!"
                                                }
                                            """
                            ))})
    })
    @ExceptionHandler({InvalidDataException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<ErrorResponse> handleInvalidDataException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = this.createErrorResponse(HttpStatus.CONFLICT, e.getMessage(), request);

        log.error("Path: {} - Msg error: {}", errorResponse.getPath(), errorResponse.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "500 Response",
                                    summary = "Handle exception when internal server error",
                                    value = """
                                             {
                                                "timestamp": "2023-10-19T06:35:52.333+00:00",
                                                "status": 500,
                                                "path": "/api/v1/...",
                                                "error": "Internal Server Error",
                                                "message": "Connection timeout, please try again"
                                             }
                                            """
                            ))})
    })
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = this.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request);

        log.info("Class exception: {}", e.getClass());
        log.error("Path: {} - Msg error: {}", errorResponse.getPath(), errorResponse.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Handle exception when the data invalid. (@RequestBody, @RequestParam, @PathVariable)",
                                    summary = "Handle Bad request",
                                    value = """
                                            {
                                               "timestamp": "2023-10-19T06:07:35.321+00:00",
                                               "status": 400,
                                               "path": "/api/v1/...",
                                               "error": "Invalid Payload",
                                               "message": {
                                                 "field": "must be not blank"
                                               }
                                             }
                                            """
                            ))})
    })
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(Exception e, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ErrorResponse errorResponse = this.createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);

        if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            errorResponse.setError("Invalid Payload");
            errorResponse.setMessage(errors);
        } else if (e instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) e).getConstraintViolations();

//             "message": {
//                      "updateMaterials.requestList[3].materialName": "K de trong",
//                      "updateMaterials.requestList[1].materialName": "K de trong"
//             }
//            for (ConstraintViolation<?> violation: violations) {
//                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
//            }
//            errorResponse.setMessage(errors);

//             "message": {
//                     "updateMaterials.requestList[3].materialName": "K de trong",
//                     "updateMaterials.requestList[1].materialName": "K de trong"
//             }
            List<String> errorList = violations.stream()
                    .map(err -> err.getPropertyPath() + ": " + err.getMessage()).toList();
            errorResponse.setMessage(errorList);
            errorResponse.setError("Invalid Payload");
        }

        log.error("Path: {} - Msg error: {}", errorResponse.getPath(), errorResponse.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private ErrorResponse createErrorResponse(HttpStatus httpStatus, String message, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setCode(httpStatus.value());
        errorResponse.setPath(path);
        errorResponse.setError(httpStatus.getReasonPhrase());
        errorResponse.setMessage(message);

        return errorResponse;
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "409 Response",
                                    summary = "Handle Duplicate Entry",
                                    value = """
                                        {
                                            "timestamp": "2023-10-19T06:35:52.333+00:00",
                                            "status": 409,
                                            "path": "/api/v1/admin/customer/create",
                                            "error": "Conflict",
                                            "message": "Dữ liệu đã tồn tại. Vui lòng kiểm tra lại."
                                        }
                                        """
                            ))})
    })
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e, WebRequest request) {
        ErrorResponse errorResponse;

        // Kiểm tra thông báo lỗi xem có liên quan đến ràng buộc unique (duplicate entry)
        if (e.getCause() != null && e.getCause().getMessage().contains("Duplicate entry")) {
            String message = "Dữ liệu đã tồn tại. Vui lòng kiểm tra lại.";
            errorResponse = this.createErrorResponse(HttpStatus.CONFLICT, message, request);
            errorResponse.setError("Conflict");
        } else {
            String message = "Lỗi vi phạm ràng buộc dữ liệu.";
            errorResponse = this.createErrorResponse(HttpStatus.BAD_REQUEST, message, request);
            errorResponse.setError("Bad Request");
        }

        log.error("Path: {} - Msg error: {}", errorResponse.getPath(), errorResponse.getMessage());
        return ResponseEntity.status(errorResponse.getCode()).body(errorResponse);

    }



}
