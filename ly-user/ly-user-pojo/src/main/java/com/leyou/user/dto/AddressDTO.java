package com.leyou.user.dto;

import lombok.Data;

/**
 * @author Nie ZongXin
 * @date 2019/9/22 23:06
 */
@Data
public class AddressDTO {
    private Long id;
    private Long userId;
    private String addressee;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String street;
    private String zipCode;
    private Boolean isDefault;
}
