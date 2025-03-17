package com.datn.beestyle.dto.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StaffResponse {
    Long id;
    String fullName;
    LocalDate dateOfBirth;
    String username;
    String role;
    String gender;
    String phoneNumber;
    String email;
    String avatar;
    String address;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
