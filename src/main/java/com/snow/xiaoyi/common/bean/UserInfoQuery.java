/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: UserInfoQuery
 * Author:   萧毅
 * Date:     2019/7/24 15:10
 * Description:
 */
package com.snow.xiaoyi.common.bean;

import lombok.Data;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
public class UserInfoQuery {


    @NotNull(message = "账号不能为空")
    private String username;
    @NotNull(message = "权限不能为空")
    @Min(value = 1, message = "权限范围为[1-99]")
    @Max(value = 99, message = "权限范围为[1-99]")
    private Long roleId;






}