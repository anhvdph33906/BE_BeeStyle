
package com.datn.beestyle.controller;

import com.datn.beestyle.repository.OrderRepository;
import com.datn.beestyle.repository.OrderItemRepository;
import com.datn.beestyle.util.InvoicePDFExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayOutputStream;

@RestController
@RequiredArgsConstructor
public class InvoiceController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InvoicePDFExporter invoicePDFExporter;
    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    /**
     * API để xuất hóa đơn PDF từ thông tin đơn hàng
     *
     * @param orderId ID của đơn hàng
     * @return ResponseEntity chứa file PDF
     */
    @GetMapping("/invoice/{orderId}")
    public ResponseEntity<byte[]> exportInvoice(@PathVariable Long orderId) {
        try {
            // Tạo OutputStream để chứa dữ liệu PDF
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            // Gọi phương thức để xuất hóa đơn PDF
            invoicePDFExporter.exportInvoice(orderId, byteArrayOutputStream);

            // Tạo header cho file trả về
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=invoice_" + orderId + ".pdf");

            // Trả về file PDF dưới dạng byte array
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(byteArrayOutputStream.toByteArray());

        } catch (Exception e) {
            // Xử lý lỗi nếu có
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/preview/{orderId}")
    public ResponseEntity<byte[]> previewInvoice(@PathVariable Long orderId) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            invoicePDFExporter.exportInvoice(orderId, outputStream);
            byte[] pdfBytes = outputStream.toByteArray();

            if (pdfBytes.length == 0) {
                logger.warn("Preview PDF content is empty.");
                return ResponseEntity.noContent().build();
            }

            // Tên file với ID đơn hàng và ngày tháng
            String fileName = "Invoice_" + orderId  + ".pdf";

            logger.info("Invoice preview generated successfully for order: {}", orderId);

            // Thiết lập các header cho response
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.add("Access-Control-Allow-Headers", "Content-Disposition");
//            headers.add("Access-Control-Allow-Origin", "*");  // Đảm bảo CORS nếu frontend và backend ở domain khác

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error generating invoice preview: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}