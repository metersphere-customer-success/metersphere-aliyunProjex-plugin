package io.metersphere.platform.domain.models;

import lombok.Data;

@Data
public class CreateWorkitemV2Request {
    //Path
    //NameInMap("organizationId")
    //Validation(required = true)
    private String organizationId;

    //Body
    //NameInMap("assignedTo")
    //Validation(required = true)
    private String assignedTo;

    //Body
    //NameInMap("category")
    //Validation(required = true)
    private String category;

    //Body
    //NameInMap("description")
    private String description;

    //Body
    //NameInMap("fieldValueList")
    private java.util.List <FieldValueList> fieldValueList;

    //Body
    //NameInMap("parentIdentifier")
    private String parentIdentifier;

    //Body
    //NameInMap("participants")
    private java.util.List < String > participants;

    //Body
    //NameInMap("spaceIdentifier")
    //Validation(required = true)
    private String spaceIdentifier;

    //Body
    //NameInMap("sprintIdentifier")
    private String sprintIdentifier;

    //Body
    //NameInMap("subject")
    //Validation(required = true)
    private String subject;

    //Body
    //NameInMap("tags")
    private java.util.List < String > tags;

    //Body
    //NameInMap("trackers")
    private java.util.List < String > trackers;

    //Body
    //NameInMap("verifier")
    private String verifier;

    //Body
    //NameInMap("versions")
    private java.util.List < String > versions;

    //Body
    //NameInMap("workitemTypeIdentifier")
    //Validation(required = true)
    private String workitemTypeIdentifier;

//    private String descriptionFormat = "RICHTEXT";



    @Data
    public static class FieldValueList {
        //NameInMap("fieldIdentifier")
        private String fieldIdentifier;

        //NameInMap("value")
        private String value;

    }
}
