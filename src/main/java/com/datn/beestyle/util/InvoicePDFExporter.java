package com.datn.beestyle.util;

import com.datn.beestyle.dto.order.item.OrderItemResponse;
import com.datn.beestyle.entity.Address;
import com.datn.beestyle.entity.order.Order;
import com.datn.beestyle.entity.order.OrderItem;
import com.datn.beestyle.enums.DiscountType;
import com.datn.beestyle.enums.OrderStatus;
import com.datn.beestyle.repository.AddressRepository;
import com.datn.beestyle.repository.OrderItemRepository;
import com.datn.beestyle.repository.OrderRepository;
import com.datn.beestyle.repository.VoucherRepository;
import com.datn.beestyle.service.order.IOrderService;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@RequiredArgsConstructor
@Component
public class InvoicePDFExporter {

    private final OrderItemRepository orderItemRepository;
    private final IOrderService orderService;
    public void exportInvoice(Long orderId, OutputStream out) {
        try {
            Order order = orderService.getById(orderId);
            List<OrderItem> listOrderItem = orderItemRepository.findOrderItemsByOrderId(orderId);
            if(order.getOrderStatus() == OrderStatus.PENDING.getValue() ||
                    order.getOrderStatus() == OrderStatus.AWAITING_CONFIRMATION.getValue()
            ){
                return;
            }

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            PdfFont font = PdfFontFactory.createFont(
                    Objects.requireNonNull(
                            getClass().getResource("/NotoSans-Regular.ttf")).toString(),
                    PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED
            );

            // **1. Thêm tiêu đề "HÓA ĐƠN THANH TOÁN"**
            // Thông tin công ty
            document.add(new Paragraph("BEESTYLE")
                    .setFont(font).setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(24).setBold()
                    .setMarginTop(0) // Khoảng cách trên đoạn văn
                    .setMarginBottom(5)); // Khoảng cách dưới đoạn văn

            document.add(new Paragraph("SĐT: 0123456789")
                    .setFont(font).setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10)
                    .setMarginTop(0) // Khoảng cách trên đoạn văn
                    .setMarginBottom(5)); // Khoảng cách dưới đoạn văn

            document.add(new Paragraph("Địa chỉ: Phương Canh, Nam Từ Liêm, Hà Nội")
                    .setFont(font).setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10)
                    .setMarginTop(0) // Khoảng cách trên đoạn văn
                    .setMarginBottom(10)); // Khoảng cách dưới đoạn văn

            // Đường kẻ ngang
            LineSeparator lineSeparator = new LineSeparator(new SolidLine());
            lineSeparator.setWidth(500);
            document.add(lineSeparator);

            Paragraph titleHeader = new Paragraph("HÓA ĐƠN BÁN HÀNG")
                    .setFont(font)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20) // Giảm cỡ chữ tiêu đề
                    .setBold();
            document.add(titleHeader);

            // Lấy chiều rộng của trang và trừ đi phần lề (nếu có)
            float pageWidth = pdfDoc.getDefaultPageSize().getWidth();
            float marginRight = 60; // Ví dụ: lề phải 36
            float marginLeft = 36;  // Ví dụ: lề trái 36

            // Tính chiều rộng thực tế sử dụng được
            float usableWidth = pageWidth - marginLeft - marginRight;

            // Tạo Paragraph
            Paragraph paragraph = new Paragraph();
            paragraph.setFont(font).setFontSize(10);

            // Đặt TabStops
            paragraph.addTabStops(new TabStop(usableWidth, TabAlignment.RIGHT));

            // Thêm nội dung
            // Thêm nội dung bên trái (Tên khách hàng)
            String customerName = " Khách lẻ";
            if (order.getCustomer() != null &&
                order.getCustomer().getFullName() != null &&
                !order.getCustomer().getFullName().isEmpty()) {
                customerName = order.getCustomer().getFullName();
            }

            paragraph.add("Tên khách hàng: " + customerName);
            paragraph.add(new Tab());
            paragraph.add("Mã hóa đơn: " + order.getOrderTrackingNumber());
            paragraph.add("\n");
            if (order.getShippingAddress() != null) {
                // Chỉ sử dụng addressResponse nếu nó không phải null
                paragraph.add("Địa chỉ: " + order.getShippingAddress().getAddressName() + ","
                        + order.getShippingAddress().getDistrict() + ","
                        + order.getShippingAddress().getCity());
            } else {
                // Xử lý trường hợp không có addressResponse
                paragraph.add("Địa chỉ: ");
            }
          
            paragraph.add(new Tab());
            paragraph.add("Ngày tạo: " + order.getCreatedAt().format(AppUtils.formatterDateGlobal));
            paragraph.add("\n");
            String phoneNumber = (order.getPhoneNumber() == null || order.getPhoneNumber().isEmpty())
                    ? ""
                    : order.getPhoneNumber();
            paragraph.add("Số điện thoại: " + phoneNumber);

            // Thêm vào tài liệu
            document.add(paragraph);
            // **3. Dòng nội dung đơn hàng**
            double totalAmount = 0;
            int totalQuantity = 0; // Biến tổng số lượng

            // **Hiển thị dòng "Nội dung đơn hàng" trước bảng**
            document.add(new Paragraph("Nội dung đơn hàng: ")
                    .setFont(font).setTextAlignment(TextAlignment.LEFT).setBold().setFontSize(14));

            // **5. Bảng chi tiết sản phẩm**
            float[] columnWidths = {0.5f, 2f, 1.5f, 1f, 1.5f}; // Chia độ rộng cột: Cột "Tên sản phẩm" chiếm nhiều không gian hơn
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100));  // Đặt tổng chiều rộng bảng bằng 100% chiều rộng trang

            // Header
            table.addHeaderCell("STT").setFont(font).setTextAlignment(TextAlignment.CENTER).setFontSize(10);
            table.addHeaderCell("Tên sản phẩm").setFont(font).setTextAlignment(TextAlignment.CENTER).setFontSize(10);
            table.addHeaderCell("Đơn giá").setFont(font).setTextAlignment(TextAlignment.CENTER).setFontSize(10);
            table.addHeaderCell("Số lượng").setFont(font).setTextAlignment(TextAlignment.CENTER).setFontSize(10);
            table.addHeaderCell("Thành tiền").setFont(font).setTextAlignment(TextAlignment.CENTER).setFontSize(10);

            Locale vietnamLocale = new Locale("vi", "VN");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(vietnamLocale);

            int index = 1;

            for (OrderItem orderItem : listOrderItem) {
                // Thêm thông tin sản phẩm vào bảng
                table.addCell(String.valueOf(index++)) // Thêm số thứ tự
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(10);
                String productName = orderItem.getProductVariant().getProduct().getProductName();
                String productColor = orderItem.getProductVariant().getColor().getColorName();
                String productSize = orderItem.getProductVariant().getSize().getSizeName();

                // Tạo chuỗi hiển thị
                String displayText = String.format("%s / %s - %s", productName, productSize, productColor);
                table.addCell(displayText).setFont(font).setFontSize(10);

                // Sử dụng giá giảm nếu có, nếu không dùng giá bán
                BigDecimal salePrice = (orderItem.getDiscountedPrice() != null && orderItem.getDiscountedPrice().compareTo(BigDecimal.ZERO) > 0)
                        ? orderItem.getDiscountedPrice()
                        : orderItem.getSalePrice();

                table.addCell(currencyFormatter.format(salePrice)).setFont(font).setFontSize(10);
                table.addCell(String.valueOf(orderItem.getQuantity())).setFont(font).setFontSize(10);

                BigDecimal quantity = BigDecimal.valueOf(orderItem.getQuantity());  // Số lượng dưới dạng BigDecimal

                // Tính tổng tiền cho một sản phẩm
                BigDecimal itemTotal = salePrice.multiply(quantity);
                table.addCell(currencyFormatter.format(itemTotal)).setFont(font).setFontSize(10);

                // Cập nhật tổng số tiền và số lượng
                totalAmount += itemTotal.doubleValue();  // Convert BigDecimal to double for totalAmount
                totalQuantity += orderItem.getQuantity();
            }
            // Thêm bảng vào tài liệu
            document.add(table);
            // **Tổng số lượng**
            document.add(new Paragraph("Tổng số lượng sản phẩm: " + totalQuantity).setFont(font).setBold().setTextAlignment(TextAlignment.LEFT).setFontSize(10));

            // Tính giảm giá
            BigDecimal totalAmountBigDecimal = BigDecimal.valueOf(totalAmount); // Tổng tiền hàng
            BigDecimal discount = totalAmountBigDecimal.subtract(order.getTotalAmount()).subtract(order.getShippingFee());

            // Thêm thông tin vào tài liệu
            document.add(new Paragraph("Tổng tiền hàng: " + currencyFormatter.format(totalAmount))
                    .setFont(font).setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
            document.add(new Paragraph("Giảm giá: " + currencyFormatter.format(discount))
                    .setFont(font).setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
            document.add(new Paragraph("Phí ship: " + currencyFormatter.format(order.getShippingFee()))
                    .setFont(font).setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
            // Tổng thanh toán
            Paragraph total = new Paragraph("Tổng thanh toán: " + currencyFormatter.format(order.getTotalAmount()))
                    .setFont(font).setTextAlignment(TextAlignment.RIGHT).setFontSize(12).setBold();
            document.add(total);


            // **9. Lời cảm ơn**
            document.add(new Paragraph("Cảm ơn quý khách đã mua hàng!").setFont(font).setTextAlignment(TextAlignment.CENTER).setFontSize(10));

            document.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
