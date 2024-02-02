package io.metersphere.platform.domain.models;

import lombok.Data;

@Data
public class ListWorkItemWorkFlowStatusResponseBody{
    //NameInMap("errorCode")
    private String errorCode;

    //NameInMap("errorMessage")
    private String errorMessage;

    //NameInMap("requestId")
    private String requestId;

    //NameInMap("statuses")
    private java.util.List < Statuses> statuses;

    //NameInMap("success")
    private Boolean success;
    @Data
    public static class Statuses{
        //NameInMap("creator")
        private String creator;

        //NameInMap("description")
        private String description;

        //NameInMap("gmtCreate")
        private Long gmtCreate;

        //NameInMap("gmtModified")
        private Long gmtModified;

        //NameInMap("identifier")
        private String identifier;

        //NameInMap("modifier")
        private String modifier;

        //NameInMap("name")
        private String name;

        //NameInMap("resourceType")
        private String resourceType;

        //NameInMap("source")
        private String source;

        //NameInMap("workflowStageIdentifier")
        private String workflowStageIdentifier;

        //NameInMap("workflowStageName")
        private String workflowStageName;

    }
}
