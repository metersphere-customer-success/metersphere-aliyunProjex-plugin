package io.metersphere.platform.domain.models;

import lombok.Data;

@Data
public class ListWorkitemsResponseBody{
    //nMap("errorCode")
    private String errorCode;

    //nMap("errorMsg")
    private String errorMsg;

    //nMap("maxResults")
    private Long maxResults;

    //nMap("nextToken")
    private String nextToken;

    //nMap("requestId")
    private String requestId;

    //nMap("success")
    private Boolean success;

    //nMap("totalCount")
    private Long totalCount;

    //nMap("workitems")
    private java.util.List <Workitems> workitems;

    @Data
    public static class Workitems {
        //nMap("assignedTo")
        private String assignedTo;

        //nMap("categoryIdentifier")
        private String categoryIdentifier;

        //nMap("creator")
        private String creator;

        //nMap("document")
        private String document;

        //nMap("gmtCreate")
        private Long gmtCreate;

        //nMap("gmtModified")
        private Long gmtModified;

        //nMap("identifier")
        private String identifier;

        //nMap("logicalStatus")
        private String logicalStatus;

        //nMap("modifier")
        private String modifier;

        //nMap("parentIdentifier")
        private String parentIdentifier;

        //nMap("serialNumber")
        private String serialNumber;

        //nMap("spaceIdentifier")
        private String spaceIdentifier;

        //nMap("spaceName")
        private String spaceName;

        //nMap("spaceType")
        private String spaceType;

        //nMap("sprintIdentifier")
        private String sprintIdentifier;

        //nMap("status")
        private String status;

        //nMap("statusIdentifier")
        private String statusIdentifier;

        //nMap("statusStageIdentifier")
        private String statusStageIdentifier;

        //nMap("subject")
        private String subject;

        //nMap("updateStatusAt")
        private Long updateStatusAt;

        //nMap("workitemTypeIdentifier")
        private String workitemTypeIdentifier;
    }
}
