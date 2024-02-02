package io.metersphere.platform.domain.models;

import lombok.Data;

@Data
public class ListSprintsResponseBody{
    //NameInMap("errorCode")
    private String errorCode;

    //NameInMap("errorMsg")
    private String errorMsg;

    //NameInMap("maxResults")
    private Long maxResults;

    //NameInMap("nextToken")
    private String nextToken;

    //NameInMap("requestId")
    private String requestId;

    //NameInMap("sprints")
    private java.util.List < Sprints> sprints;

    //NameInMap("success")
    private Boolean success;

    //NameInMap("totalCount")
    private Long totalCount;
    @Data
    public static class Sprints {
        //NameInMap("creator")
        private String creator;

        //NameInMap("description")
        private String description;

        //NameInMap("endDate")
        private Long endDate;

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

        //NameInMap("scope")
        private String scope;

        //NameInMap("spaceIdentifier")
        private String spaceIdentifier;

        //NameInMap("startDate")
        private Long startDate;

        //NameInMap("status")
        private String status;
    }
}
