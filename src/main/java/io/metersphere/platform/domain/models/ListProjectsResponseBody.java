package io.metersphere.platform.domain.models;

import lombok.Data;

@Data
public class ListProjectsResponseBody extends ResponseBase {
    private String errorCode;

    private String errorMsg;

    private Long maxResults;

    private String nextToken;

    private java.util.List < Projects> projects;

    private String requestId;

    private Boolean success;

    private Long totalCount;

    @Data
    public static class Projects extends ResponseBase {
        private String categoryIdentifier;

        private String creator;

        private String customCode;

        private Long deleteTime;

        private String description;

        private Long gmtCreate;

        private String icon;

        private String identifier;

        private String logicalStatus;

        private String name;

        private String scope;

        private String statusStageIdentifier;

        private String typeIdentifier;

    }
}