package io.metersphere.platform.domain.models;

import lombok.Data;

@Data
public class ListProjectWorkitemTypesResponseBody{
    //meInMap("errorCode")
    private String errorCode;

    //meInMap("errorMessage")
    private String errorMessage;

    //meInMap("requestId")
    private String requestId;

    //meInMap("success")
    private Boolean success;

    //meInMap("workitemTypes")
    private java.util.List < WorkitemTypes> workitemTypes;

    @Data
    public static class WorkitemTypes {
        //meInMap("addUser")
        private String addUser;

        //meInMap("categoryIdentifier")
        private String categoryIdentifier;

        //meInMap("creator")
        private String creator;

        //meInMap("defaultType")
        private Boolean defaultType;

        //meInMap("description")
        private String description;

        //meInMap("enable")
        private Boolean enable;

        //meInMap("gmtAdd")
        private Long gmtAdd;

        //meInMap("gmtCreate")
        private Long gmtCreate;

        //meInMap("identifier")
        private String identifier;

        //meInMap("name")
        private String name;

        //meInMap("nameEn")
        private String nameEn;

        //meInMap("systemDefault")
        private Boolean systemDefault;


    }
}
