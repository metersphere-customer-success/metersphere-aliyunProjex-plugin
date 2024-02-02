package io.metersphere.platform.domain.models;

import io.metersphere.platform.domain.models.ResponseBase;
import lombok.Data;

@Data
public class ListOrganizationMembersResponseBody extends ResponseBase {
    //"errorCode")
    private String errorCode;

    //"errorMessage")
    private String errorMessage;

    //"members")
    private java.util.List < Members> members;

    //"nextToken")
    private String nextToken;

    //"requestId")
    private String requestId;

    //"success")
    private Boolean success;

    //"totalCount")
    private Long totalCount;

    @Data
    public static class Identities extends ResponseBase {
        //"externUid")
        private String externUid;

        //"provider")
        private String provider;

    }
    @Data
    public static class Members extends ResponseBase {
        //"accountId")
        private String accountId;

        //"birthday")
        private Long birthday;

        //"deptLists")
        private java.util.List < String > deptLists;

        //"email")
        private String email;

        //"hiredDate")
        private Long hiredDate;

        //"identities")
        private Identities identities;

        //"joinTime")
        private Long joinTime;

        //"lastVisitTime")
        private Long lastVisitTime;

        //"mobile")
        private String mobile;

        //"organizationMemberName")
        private String organizationMemberName;

        //"organizationRoleId")
        private String organizationRoleId;

        //"organizationRoleName")
        private String organizationRoleName;

        //"state")
        private String state;

    }
}