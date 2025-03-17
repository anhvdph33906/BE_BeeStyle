package com.datn.beestyle.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressResponse {
    Long id;
    String addressName;
    Integer cityCode;
    String city;
    Integer districtCode;
    String district;
    Integer communeCode;
    String commune;
    Boolean isDefault;
    Long customerId;


    public AddressResponse (Long id, String addressName, Integer cityCode, String city, Integer districtCode, String district, Integer communeCode,
                            String commune){
        this.id = id;
        this.addressName = addressName;
        this.cityCode = cityCode;
        this.city = city;
        this.districtCode = districtCode;
        this.district = district;
        this.communeCode = communeCode;
        this.commune = commune;
    }

}
