package com.datn.beestyle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptPasswordEncoderTest {
    // Hàm mã hóa chuỗi bằng Bcrypt
    public static String encodePassword(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(rawPassword);
    }

    @Test
    public void testEncodePassword() {
        String rawPassword = "kim2003";
        System.out.println("Chuỗi ban đầu: " + rawPassword);
if(rawPassword.length()<=10 && rawPassword.length()>=5){
    String encodedPassword = encodePassword(rawPassword);
    System.out.println("Chuỗi đã mã hóa (Bcrypt): " + encodedPassword);

    assertNotNull(encodedPassword, "Mã hóa không thành công, chuỗi trả về là null");
    assertTrue(encodedPassword.startsWith("$2a$"), "Chuỗi mã hóa không đúng định dạng Bcrypt");

    // Kiểm tra chuỗi ban đầu khớp với mã hóa
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    boolean isMatch = encoder.matches(rawPassword, encodedPassword);
    System.out.println("Kết quả kiểm tra chuỗi khớp: " + isMatch);
    assertTrue(isMatch, "Chuỗi ban đầu không khớp với chuỗi đã mã hóa");
}else {
    System.out.println("Chuỗi không hợp lệ");
}

    }

}
