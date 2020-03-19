package com.baker.learning.bigdatacommons;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @date 2019/9/20 15:40
 */

@Data
@ToString
public class CmdResultVO implements Serializable {
    private String taskTimeConsuming;
    private String result;
    private String errorResult;
}
