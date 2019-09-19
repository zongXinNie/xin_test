package com.leyou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nie ZongXin
 * @date 2019/9/18 23:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long id;
    private String username;
    private String role;
}
