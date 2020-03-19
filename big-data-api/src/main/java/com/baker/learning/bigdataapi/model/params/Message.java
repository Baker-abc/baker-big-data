package com.baker.learning.bigdataapi.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @description: 保存message接口参数
 * @create: 2018-10-13 12:10
 * @Version: 1.0
 **/
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "保存message接口参数")
public class Message implements Serializable {

    @ApiModelProperty(value = "时间")
    private String time;
    @ApiModelProperty(value = "userName")
    private String userName;
    @ApiModelProperty(value = "designName")
    private String designName;
    @ApiModelProperty(value = "url")
    private String url;
    @ApiModelProperty(value = "method")
    private String method;
    @ApiModelProperty(value = "params")
    private String params;

}
