package com.datn.beestyle.security.authentication;

import com.datn.beestyle.dto.customer.CustomerResponse;
import com.datn.beestyle.dto.staff.StaffResponse;
import com.datn.beestyle.entity.user.Customer;
import com.datn.beestyle.entity.user.Staff;
import com.datn.beestyle.enums.Gender;
import com.datn.beestyle.exception.InvalidDataException;
import com.datn.beestyle.repository.customer.CustomerRepository;
import com.datn.beestyle.repository.StaffRepository;
import com.datn.beestyle.security.UserDetailsServiceImpl;
import com.datn.beestyle.security.jwt.JwtService;
import com.datn.beestyle.security.request.ResetPasswordRequest;
import com.datn.beestyle.security.request.SignInRequest;
import com.datn.beestyle.security.response.TokenResponse;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.datn.beestyle.enums.TokenType.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
//    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;

    @Override
    public TokenResponse accessToken(SignInRequest request) {
        /**
         * Xác thực thông tin đăng nhập bằng cách tạo một đối tượng UsernamePasswordAuthenticationToken
         * từ email và mật khẩu.
         * Sau đó, sử dụng authenticationManager để xác thực thông tin này và trả về đối tượng Authentication
         */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // Đặt đối tượng Authentication vào SecurityContext để quản lý bảo mật cho session hiện tại.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Lấy thông tin chi tiết của người dùng từ đối tượng Authentication.
        UserDetails user = (UserDetails) authentication.getPrincipal();

        StaffResponse staffResponse = null;
        CustomerResponse customerResponse = null;
        if (user instanceof Staff) {
            Staff staff = (Staff) user;
            staffResponse = new StaffResponse();
            staffResponse.setId(staff.getId());
            staffResponse.setFullName(staff.getFullName());
            staffResponse.setAvatar(staff.getAvatar());
            staffResponse.setDateOfBirth(staff.getDateOfBirth());
            staffResponse.setGender(Gender.fromInteger(staff.getGender()));
            staffResponse.setEmail(staff.getEmail());
            staffResponse.setRole(staff.getRole().name());
        } else if (user instanceof Customer) {
            Customer customer = (Customer) user;
            customerResponse = new CustomerResponse();
            customerResponse.setId(customer.getId());
            customerResponse.setFullName(customer.getFullName());
            customerResponse.setDateOfBirth(customer.getDateOfBirth());
            customerResponse.setPhoneNumber(customer.getPhoneNumber());
            customerResponse.setGender(Gender.fromInteger(customer.getGender()));
            customerResponse.setEmail(customer.getEmail());
            customerResponse.setRole(customer.getRole().name());
        }

        if(!user.isEnabled()) throw new InvalidDataException("User not active.");

        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        // save token to db
//        tokenService.save(Token.builder()
//                .username(user.getUsername())
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(staffResponse == null ? customerResponse : staffResponse)
                .build();
    }

    @Override
    public TokenResponse refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(HttpHeaders.REFERER);
        if (StringUtils.isBlank(refreshToken)) throw new InvalidDataException("Token must be not blank.");

        // extract user from token
        final String username = jwtService.extractUsername(refreshToken, REFRESH_TOKEN);

        // check it into db
        var user = userDetailsService.loadUserByUsername(username);
        if (!jwtService.isValidToken(refreshToken, REFRESH_TOKEN, user)) {
            throw new InvalidDataException("Not allow access with this token");
        }

        Staff staffResponse = null;
        Customer customerResponse = null;
        if (user instanceof Staff) {
            Staff staff = (Staff) user;
            staffResponse = new Staff();
            staffResponse.setId(staff.getId());
            staffResponse.setFullName(staff.getFullName());
            staffResponse.setAvatar(staff.getAvatar());
            staffResponse.setDateOfBirth(staff.getDateOfBirth());
            staffResponse.setGender(staff.getGender());
            staffResponse.setEmail(staff.getEmail());
            staffResponse.setRole(staff.getRole());
        } else if (user instanceof Customer) {
            Customer customer = (Customer) user;
            customerResponse = new Customer();
            customerResponse.setId(customer.getId());
            customerResponse.setFullName(customer.getFullName());
            customerResponse.setDateOfBirth(customer.getDateOfBirth());
            customerResponse.setGender(customer.getGender());
            customerResponse.setEmail(customer.getEmail());
            customerResponse.setRole(customer.getRole());
        }

        // create new access token
        String accessToken = jwtService.generateAccessToken(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(staffResponse == null ? customerResponse : staffResponse)
                .build();
    }

    @Override
    public String removeToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(HttpHeaders.REFERER);
        if (StringUtils.isBlank(refreshToken)) throw new InvalidDataException("Token must be not blank.");

        // extract user from token
        final String username = jwtService.extractUsername(refreshToken, ACCESS_TOKEN);

//        // check token into db
//        Token currentToken = tokenService.getByUsername(username);

//        // delete token
//        tokenService.delete(currentToken);

        return "Deleted";
    }

    @Override
    public String forgotPassword(String email) {
        log.info("---------- forgotPassword ----------");

        // check email exists
        var user = userDetailsService.loadUserByUsername(email);

        // user is active or inactive
        if(!user.isEnabled()) throw new InvalidDataException("User not active");

        // generate reset token
        String resetToken = jwtService.generateResetToken(user);

        // save to db
//        tokenService.save(Token.builder().username(user.getUsername()).resetToken(resetToken).build());

        // send email confirm link
        String confirmLink = String.format(
                "curl --location 'http://localhost:80/auth/reset-password' \\\n" +
                "--header 'accept: */*' \\\n" +
                "--header 'Content-Type: application/json' \\\n" +
                "--data '%s'", resetToken);
        log.info("--> confirmLink: {}", confirmLink);

        return "Send";
    }

    @Override
    public String resetPassword(String resetKey) {
        log.info("---------- resetPassword ----------");

        // validate token
        var user = this.validateToken(resetKey);

//        // check token by username
//        tokenService.getByUsername(user.getUsername());

        if (!jwtService.isValidToken(resetKey, RESET_TOKEN, user)) {
            throw new InvalidDataException("Not allow access with this token");
        }
        return "Reset";
    }

    @Override
    public String changePassword(ResetPasswordRequest request) {
        log.info("---------- changePassword ----------");

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidDataException("Passwords do not match");
        }

        // get user by reset token
        var user = this.validateToken(request.getSecretKey());

        // update password
        if (user instanceof Staff) {
            Staff staff = (Staff) user;
            staff.setPassword(passwordEncoder.encode(request.getPassword()));
            staffRepository.save(staff);
        } else if (user instanceof Customer){
            Customer customer = (Customer) user;
            customer.setPassword(passwordEncoder.encode(request.getPassword()));
            customerRepository.save(customer);
        }

        return "Changed";
    }

    private UserDetails validateToken(String token) {
        // extract username to token
        final String username = jwtService.extractUsername(token, RESET_TOKEN);

        // validate user is active or not
        var user = userDetailsService.loadUserByUsername(username);
        if(!user.isEnabled()) throw new InvalidDataException("User not active");

        return user;
    }

}
