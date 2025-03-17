package com.datn.beestyle.controller;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/admin/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/send-mail")
    public ApiResponse<?> sendMail(@RequestParam String recipients, @RequestParam String subject,
                                   @RequestParam String content, @RequestParam(required = false) MultipartFile[] files){
        try {
            return new ApiResponse<>(HttpStatus.OK.value(), mailService.sendMail(recipients,subject,content,files));
        }catch (Exception e){
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/send-thanhYou-mail")
    public ApiResponse<?> sendThankYouEmail(@RequestParam Long id, @RequestParam(required = false) MultipartFile[] files){
        try {
            return new ApiResponse<>(HttpStatus.OK.value(), mailService.sendThankYouEmail(id,files));
        }catch (Exception e){
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/send-orderTrackingNumber")
    public ApiResponse<?> sendOrderTrackingNumber(@RequestParam String orderTrackingNumber,String recipient, String customerName ){
        try {
            return new ApiResponse<>(HttpStatus.OK.value(), mailService.sendOrderTrackingNumber(orderTrackingNumber,recipient,customerName));
        }catch (Exception e){
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

}
