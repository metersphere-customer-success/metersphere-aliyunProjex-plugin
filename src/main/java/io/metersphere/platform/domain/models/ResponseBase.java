package io.metersphere.platform.domain.models;

import lombok.Data;

@Data
public class ResponseBase {
    private Long totalCount;
    private String nextToken;
    private Long maxResults;
    private String requestId;
    private String errorMsg;
    private String errorCode;
    private Boolean success;
}
