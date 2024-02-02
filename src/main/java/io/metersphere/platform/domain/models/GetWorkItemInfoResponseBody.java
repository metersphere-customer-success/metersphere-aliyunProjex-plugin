package io.metersphere.platform.domain.models;

import lombok.Data;

@Data
public class GetWorkItemInfoResponseBody{
    //NameInMap("errorCode")
    private String errorCode;

    //NameInMap("errorMessage")
    private String errorMessage;

    //NameInMap("requestId")
    private String requestId;

    //NameInMap("success")
    private Boolean success;

    //NameInMap("workitem")
    private Workitem workitem;

    @Data
    public static class ValueList {
        //NameInMap("displayValue")
        private String displayValue;

        //NameInMap("identifier")
        private String identifier;

        //NameInMap("level")
        private Long level;

        //NameInMap("value")
        private String value;

        //NameInMap("valueEn")
        private String valueEn;
    }
    @Data
    public static class CustomFields {
        //NameInMap("fieldClassName")
        private String fieldClassName;

        //NameInMap("fieldFormat")
        private String fieldFormat;

        //NameInMap("fieldIdentifier")
        private String fieldIdentifier;

        //NameInMap("level")
        private Long level;

        //NameInMap("objectValue")
        private String objectValue;

        //NameInMap("position")
        private Long position;

        //NameInMap("value")
        private String value;

        //NameInMap("valueList")
        private java.util.List<ValueList> valueList;

        //NameInMap("workitemIdentifier")
        private String workitemIdentifier;
    }
    @Data
    public static class Workitem {
        //NameInMap("assignedTo")
        private String assignedTo;

        //NameInMap("categoryIdentifier")
        private String categoryIdentifier;

        //NameInMap("creator")
        private String creator;

        //NameInMap("customFields")
        private java.util.List<CustomFields> customFields;

        //NameInMap("document")
        private String document;

        //NameInMap("gmtCreate")
        private Long gmtCreate;

        //NameInMap("gmtModified")
        private Long gmtModified;

        //NameInMap("identifier")
        private String identifier;

        //NameInMap("logicalStatus")
        private String logicalStatus;

        //NameInMap("modifier")
        private String modifier;

        //NameInMap("parentIdentifier")
        private String parentIdentifier;

        //NameInMap("participant")
        private java.util.List<String> participant;

        //NameInMap("serialNumber")
        private String serialNumber;

        //NameInMap("spaceIdentifier")
        private String spaceIdentifier;

        //NameInMap("spaceName")
        private String spaceName;

        //NameInMap("spaceType")
        private String spaceType;

        //NameInMap("sprint")
        private java.util.List<String> sprint;

        //NameInMap("status")
        private String status;

        //NameInMap("statusIdentifier")
        private String statusIdentifier;

        //NameInMap("statusStageIdentifier")
        private String statusStageIdentifier;

        //NameInMap("subject")
        private String subject;

        //NameInMap("tag")
        private java.util.List<String> tag;

        //NameInMap("tracker")
        private java.util.List<String> tracker;

        //NameInMap("updateStatusAt")
        private Long updateStatusAt;

        //NameInMap("verifier")
        private java.util.List<String> verifier;

        //NameInMap("workitemTypeIdentifier")
        private String workitemTypeIdentifier;

    }
}
