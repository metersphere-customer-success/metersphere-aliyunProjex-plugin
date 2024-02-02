package io.metersphere.platform.domain.models;

import lombok.Data;

@Data
public class ListWorkItemAllFieldsResponseBody{
    //NameInMap("errorCode")
    private String errorCode;

    //NameInMap("errorMsg")
    private String errorMsg;

    //NameInMap("fields")
    private java.util.List <Fields> fields;

    //NameInMap("requestId")
    private String requestId;

    //NameInMap("success")
    private Boolean success;

    @Data
    public static class Options {
        //NameInMap("displayValue")
        private String displayValue;

        //NameInMap("fieldIdentifier")
        private String fieldIdentifier;

        //NameInMap("identifier")
        private String identifier;

        //NameInMap("level")
        private Long level;

        //NameInMap("position")
        private Long position;

        //NameInMap("value")
        private String value;

        //NameInMap("valueEn")
        private String valueEn;
    }
        @Data
        public static class Fields {
            //NameInMap("className")
            private String className;

            //NameInMap("creator")
            private String creator;

            //NameInMap("defaultValue")
            private String defaultValue;

            //NameInMap("description")
            private String description;

            //NameInMap("format")
            private String format;

            //NameInMap("gmtCreate")
            private Long gmtCreate;

            //NameInMap("gmtModified")
            private Long gmtModified;

            //NameInMap("identifier")
            private String identifier;

            //NameInMap("isRequired")
            private Boolean isRequired;

            //NameInMap("isShowWhenCreate")
            private Boolean isShowWhenCreate;

            //NameInMap("isSystemRequired")
            private Boolean isSystemRequired;

            //NameInMap("linkWithService")
            private String linkWithService;

            //NameInMap("modifier")
            private String modifier;

            //NameInMap("name")
            private String name;

            //NameInMap("options")
            private java.util.List<Options> options;

            //NameInMap("resourceType")
            private String resourceType;

            //NameInMap("type")
            private String type;
        }

}
