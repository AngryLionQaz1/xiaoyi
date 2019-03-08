/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: RangeSettings
 * Author:   萧毅
 * Date:     2019/3/8 15:47
 * Description:
 */
package com.snow.xiaoyi.common.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RangeSettings {

    private long start;
    private long end;
    private long contentLength;
    private long totalLength;
    private boolean range;


}