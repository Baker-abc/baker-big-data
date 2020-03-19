package com.baker.learning.bigdatakafka.model;

import lombok.*;

import java.io.Serializable;

/**
 * @description
 * @date 2020/3/7 10:53
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportRoomsMessage implements Serializable {

    public static final String CHANGE_EVENT = "change";

    private String messageType;
    private String designId;
    private String designJson;
    private String resultJson;
}
