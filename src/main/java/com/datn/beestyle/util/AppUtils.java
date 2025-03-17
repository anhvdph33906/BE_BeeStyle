package com.datn.beestyle.util;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class AppUtils {

    public static final int FREE_SHIPPING_THRESHOLD = 500000;
    private static final String PRODUCT_CODE_PREFIX = "SP";
    private static final String ORDER_CODE_PREFIX = "HD";
    public static final int MAX_CATEGORY_LEVEL = 3;
    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITE_SPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGES_DASHES = Pattern.compile("(^-|-$)");
    private static final DateTimeFormatter formatterDateOrderTrackingNumber = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    public static final DateTimeFormatter formatterDateGlobal = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

    public static String toSlug(String input) {
        // Thay thế khoảng trắng bằng dấu gạch ngang
        String noWhiteSpace = WHITE_SPACE.matcher(input).replaceAll("-");

        // Thay ký tự đĐ
        String str = noWhiteSpace.replaceAll("[đĐ]", "d");

        // Bỏ dấu tiếng Việt bằng cách chuẩn hóa
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);

        // Loại bỏ tất cả các ký tự không phải là chữ Latin hoặc dấu gạch ngang
        String slug = NON_LATIN.matcher(normalized).replaceAll("");

        // Loại bỏ dấu gạch ngang thừa ở đầu hoặc cuối chuỗi
        slug = EDGES_DASHES.matcher(slug).replaceAll("");

        // Chuyển đổi thành chữ thường
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static List<Integer> handleStringIdsToIntegerIdList(String StringIds) {
        List<Integer> integerIdList = null;
        String[] idsStr = StringIds != null ? StringIds.split(",") : null;
        if (idsStr != null) {
            integerIdList = new ArrayList<>();
            for (String strId : idsStr) {
                int id;
                try {
                    id = Integer.parseInt(strId);
                } catch (Exception e) {
                    continue;
                }
                integerIdList.add(id);
            }
        }
        return integerIdList;
    }

    public static String generateProductCode(Long id) {
        return String.format("%s%06d", PRODUCT_CODE_PREFIX, id);
    }

    public static String generateOrderTrackingNumber() {
        LocalDateTime now = LocalDateTime.now();
        String formattedOrderTrackingNumber = now.format(formatterDateOrderTrackingNumber);
        return String.format("%s%s", ORDER_CODE_PREFIX, formattedOrderTrackingNumber);
    }

//    public static void main(String[] args) {
//        System.out.println(toSlug("Đây là một ví dụ về Slug!"));
//    }
}
