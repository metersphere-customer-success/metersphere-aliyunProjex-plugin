package io.metersphere.platform.domain.models;


import lombok.Data;

@Data
public class GetWorkitemAttachmentCreatemetaResponseBody{
    //NameInMap("errorCode")
    private String errorCode;

    //NameInMap("errorMessage")
    private String errorMessage;

    //NameInMap("requestId")
    private String requestId;

    //NameInMap("success")
    private String success;

    //NameInMap("uploadInfo")
    private UploadInfo uploadInfo;

    @Data
    public static class UploadInfo {
        //NameInMap("accessid")
        private String accessid;

        //NameInMap("dir")
        private String dir;

        //NameInMap("host")
        private String host;

        //NameInMap("policy")
        private String policy;

        //NameInMap("signature")
        private String signature;


    }
}
