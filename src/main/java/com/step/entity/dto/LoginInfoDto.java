package com.step.entity.dto;

import lombok.Data;

/**
 * Created by zhushubin  on 2019-08-29.
 * email:604580436@qq.com
 * 登录成功状态
 */
@Data
public class LoginInfoDto {
    private String entCode;
    private String tokenId;
    private String refreshTokenId;
}
