package io.metersphere.platform.impl;

import com.alibaba.fastjson2.JSONObject;
import io.metersphere.base.domain.IssuesWithBLOBs;
import io.metersphere.platform.api.AbstractPlatform;
import io.metersphere.platform.client.AliyunProjexClient;
import io.metersphere.platform.constants.CustomFieldType;
import io.metersphere.platform.domain.*;
import io.metersphere.platform.domain.AliyunProjexConfig;
import io.metersphere.platform.domain.models.*;
import io.metersphere.platform.utils.BeanUtils;
import io.metersphere.plugin.exception.MSPluginException;
import io.metersphere.plugin.utils.JSON;
import io.metersphere.plugin.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AliyunProjexPlatform extends AbstractPlatform {
    public static AliyunProjexConfig config = null;
    public static AliyunProjexProjectConfig aliyunProjexProjectConfig = null;
    public static List<ListOrganizationMembersResponseBody.Members> members = null;
    private static AliyunProjexClient asyncClient;
    private static AliyunProjexUserConfig aliyunProjexUserConfig = null;
    private static String suffix = "suffix";
    public AliyunProjexPlatform(PlatformRequest request) {
        System.out.println("构造Projex实例对象，构造信息"+JSON.toJSONString(request));
        super.key = AliyunProjexPlatformMetaInfo.KEY;
        super.request = request;
        setConfig();
        if(StringUtils.isNotBlank(request.getUserPlatformInfo()))aliyunProjexUserConfig = JSON.parseObject(request.getUserPlatformInfo(), AliyunProjexUserConfig.class);
        asyncClient = new AliyunProjexClient(this);
    }

    public void setConfig() {
        config = getIntegrationConfig(AliyunProjexConfig.class);
    }

    private static void setUserConfig(String userConfig){
        config = JSON.parseObject(userConfig, AliyunProjexConfig.class);
    }

    @Override
    public List<DemandDTO> getDemands(String projectConfigStr) {
        aliyunProjexProjectConfig = getProjectConfig(projectConfigStr);
        List<ListWorkitemsResponseBody.Workitems> listWorkitems = asyncClient.listWorkitems("Req");
        List<DemandDTO> tempList = new ArrayList<>();
        boolean isStoryTypeId = false;
        if(aliyunProjexProjectConfig.getAliyunStoryTypeId() != null) isStoryTypeId = true;
        for(ListWorkitemsResponseBody.Workitems item : listWorkitems){
            if(isStoryTypeId) {
                if(!aliyunProjexProjectConfig.getAliyunStoryTypeId().equals(item.getWorkitemTypeIdentifier())) continue;
            }
            DemandDTO demandDTO = new DemandDTO();
            demandDTO.setId(item.getIdentifier());
            demandDTO.setName(item.getSubject());
            demandDTO.setPlatform("云效 Projex");
            tempList.add(demandDTO);
        }
        return tempList;
    }

    private void updateUserAKSK(){
        config.setAccessKeyID(aliyunProjexUserConfig.getAccessKeyID());
        config.setAccessKeySecret(aliyunProjexUserConfig.getAccessKeySecret());
    }

    private String rmSuffix(String accountId){
        //纯数字无法显示匹配，加个后缀
        if(StringUtils.isEmpty(accountId)) return null;
        return accountId.replace(suffix,"");
    }
    public String loadDescImage(String desc){
//        document -> aa![image.png](/resource/md/get?fileName=fb7c038d.png)
        Map<String,Object> map = loadDescImageAndInput(desc);
        String input = map.get("input")+"";
        map.remove("input");
        for(String key: map.keySet()){
            input = input.replace(key,loadHtmlImage((File)map.get(key)));
        }
        return input;
    }

    public static String encodeBase64File(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes); // 读取到 byte 里面
        fileInputStream.close();
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }

    public String loadHtmlImage(File file){
        String img = "";
        try {
            img = " <img src=\"data:image/png;base64,"+encodeBase64File(file)+"\"/>";
        }catch (Exception e){
            e.printStackTrace();
        }
        return img;
    }

    public Map<String,Object> loadDescImageAndInput(String input) {
        List<File> files = new ArrayList();
        Map<String,Object> map = new HashMap<>();
        Pattern pattern = Pattern.compile("(\\!\\[.*?\\]\\((.*?)\\))");
        if (StringUtils.isBlank(input)) {
            return map;
        } else {
            Matcher matcher = pattern.matcher(input);

            while(matcher.find()) {
                try {
                    String path = matcher.group(2);
                    if (!path.contains("/resource/md/get/url") && !path.contains("/resource/md/get/path")) {
                        String os = System.getProperty("os.name").toLowerCase();
                        String name;
                        if (path.contains("/resource/md/get/")) {
                            File file = null;
                            if(os.indexOf("windows") != -1){
                                name = path.substring(path.indexOf("/resource/md/get/") + 18);
                                String filePath = "C:\\opt\\metersphere\\data\\image\\markdown\\"+name;
                                file = new File(filePath);
                            }else{
                                 name = path.substring(path.indexOf("/resource/md/get/") + 17);
                                 file = new File("/opt/metersphere/data/image/markdown/" + name);
                            }
                            files.add(file);
                            String uuid = UUID.randomUUID().toString();
                            input = input.replace(matcher.group(0), uuid);
                            map.put(uuid,file);
                        } else if (path.contains("/resource/md/get")) {
                            File file = null;
                            if(os.indexOf("windows") != -1){
                                name = path.substring(path.indexOf("/resource/md/get/") + 27);
                                String filePath = "C:\\opt\\metersphere\\data\\image\\markdown\\"+ URLDecoder.decode(name, StandardCharsets.UTF_8.name());
                                file = new File(filePath);
                            }else{
                                name = path.substring(path.indexOf("/resource/md/get") + 26);
                                file = new File("/opt/metersphere/data/image/markdown/" + URLDecoder.decode(name, StandardCharsets.UTF_8.name()));
                            }
                            files.add(file);
                            String uuid = UUID.randomUUID().toString();
                            input = input.replace(matcher.group(0), uuid);
                            map.put(uuid,file);
                        } else {
                            //网络图片
                            java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
                            String byte64Str = encoder.encodeToString(asyncClient.openFile(path));
                            byte64Str = " <img src=\"data:image/png;base64,"+byte64Str+"\"/>";
                            input = input.replace(matcher.group(0), byte64Str);
                        }
                    }

                } catch (Exception var7) {
                    LogUtil.error(var7.getMessage(), var7);
                }
            }
            map.put("input",input);
            return map;
        }
    }

    public Map<String,Object> loadHtmlImageAndInput(String input) {
//        List<File> files = new ArrayList();
        Map<String,Object> map = new HashMap<>();
        Pattern pattern = Pattern.compile("(\\!\\[.*?\\]\\((.*?)\\))");
        if (StringUtils.isBlank(input)) {
            return map;
        } else {
            Matcher matcher = pattern.matcher(input);

            while(matcher.find()) {
                try {
                    String nameT = matcher.group(0);
                    String pathT = matcher.group(1);
                    String path = matcher.group(2);
                    asyncClient.downloadImage(path);
//                    if (!path.contains("/resource/md/get/url") && !path.contains("/resource/md/get/path")) {
//                        String os = System.getProperty("os.name").toLowerCase();
//                        String name;
//                        if (path.contains("/resource/md/get/")) {
//                            File file = null;
//                            if(os.indexOf("windows") != -1){
//                                name = path.substring(path.indexOf("/resource/md/get/") + 18);
//                                String filePath = "C:\\opt\\metersphere\\data\\image\\markdown\\"+name;
//                                file = new File(filePath);
//                            }else{
//                                name = path.substring(path.indexOf("/resource/md/get/") + 17);
//                                file = new File("/opt/metersphere/data/image/markdown/" + name);
//                            }
//                            files.add(file);
//                            String uuid = UUID.randomUUID().toString();
//                            input = input.replace(matcher.group(0), uuid);
//                            map.put(uuid,file);
//                        } else if (path.contains("/resource/md/get")) {
//                            File file = null;
//                            if(os.indexOf("windows") != -1){
//                                name = path.substring(path.indexOf("/resource/md/get/") + 27);
//                                String filePath = "C:\\opt\\metersphere\\data\\image\\markdown\\"+ URLDecoder.decode(name, StandardCharsets.UTF_8.name());
//                                file = new File(filePath);
//                            }else{
//                                name = path.substring(path.indexOf("/resource/md/get") + 26);
//                                file = new File("/opt/metersphere/data/image/markdown/" + URLDecoder.decode(name, StandardCharsets.UTF_8.name()));
//                            }
//                            files.add(file);
//                            String uuid = UUID.randomUUID().toString();
//                            input = input.replace(matcher.group(0), uuid);
//                            map.put(uuid,file);
//                        } else {
//                            //网络图片
//                            java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
//                            String byte64Str = encoder.encodeToString(asyncClient.openFile(path));
//                            byte64Str = " <img src=\"data:image/png;base64,"+byte64Str+"\"/>";
//                            input = input.replace(matcher.group(0), byte64Str);
//                        }
//                    }
//
                } catch (Exception var7) {
                    LogUtil.error(var7.getMessage(), var7);
                }
            }
            map.put("input",input);
            return map;
        }
    }

    private CreateWorkitemV2Request getCreateWorkitemV2RequestByCustomFieIdList(List<PlatformCustomFieldItemDTO> customFieldItemDTOS){
        CreateWorkitemV2Request createWorkitemV2Request = new CreateWorkitemV2Request();
        createWorkitemV2Request.setOrganizationId(config.getOrganizationId());
        HashMap<String,String> map = new HashMap<>();
        customFieldItemDTOS.forEach(item -> {
            map.put(item.getCustomData(),item.getValue()+"");
        });
        List<String> list = new ArrayList<>();
        // 先添加系统字段，并把系统字段添加到集合里面去
        createWorkitemV2Request.setSubject(map.get("subject"));
        list.add("subject");
        String description = map.get("document");
        if(!StringUtils.isBlank(description)){
            String desc = loadDescImage(description);
            createWorkitemV2Request.setDescription(desc);
        }
        list.add("document");
        createWorkitemV2Request.setAssignedTo(rmSuffix(map.get("assignedTo")));
        list.add("assignedTo");
        createWorkitemV2Request.setSpaceIdentifier(aliyunProjexProjectConfig.getProjectIdentifier());
        createWorkitemV2Request.setCategory("Bug");
        createWorkitemV2Request.setWorkitemTypeIdentifier(aliyunProjexProjectConfig.getAliyunIssueTypeId());
        createWorkitemV2Request.setSprintIdentifier(map.get("sprint"));
        list.add("sprint");
        // 还有问题
        createWorkitemV2Request.setTrackers(JSON.parseArray(rmSuffix(map.get("workitem.tracker")), String.class));
        list.add("workitem.tracker");
        createWorkitemV2Request.setParticipants(JSON.parseArray(rmSuffix(map.get("ak.issue.member")), String.class));
        list.add("ak.issue.member");
        createWorkitemV2Request.setVerifier(rmSuffix(map.get("workitem.verifier")));
        list.add("workitem.verifier");

        List<CreateWorkitemV2Request.FieldValueList> fieldValueLists = new ArrayList<>();
        map.forEach((key, value) -> {
            // 先筛选出空值的 和系统字段和获取提交名称不一致的
            if(StringUtils.isNotBlank(value)){
                PlatformCustomFieldItemDTO p = null;
                for(PlatformCustomFieldItemDTO item:customFieldItemDTOS){
                    if(StringUtils.equals(item.getCustomData(), key)){
                        p = item;
                        break;
                    }
                }

                //如果不是多选下拉框字段，值不能为null或”“,允许值是[]
                if(!StringUtils.equalsIgnoreCase("multipleSelect", p.getType())){
                    if(StringUtils.isNotBlank(value)){
                        // 再筛选出系统字段
                        if(!list.contains(key)){
                            CreateWorkitemV2Request.FieldValueList fieldValueList = new CreateWorkitemV2Request.FieldValueList();
                            fieldValueList.setFieldIdentifier(key);
                            fieldValueList.setValue(value);
                            fieldValueLists.add(fieldValueList);
                        }
                    }
                }else{
                    // 如果是多选下拉框字段，值不能为[]
                    if(!StringUtils.equals("[]",value)){
                        // 再筛选出系统字段
                        if(!list.contains(key)){
                            CreateWorkitemV2Request.FieldValueList fieldValueList = new CreateWorkitemV2Request.FieldValueList();
                            fieldValueList.setFieldIdentifier(key);
                            fieldValueList.setValue(value);
                            fieldValueLists.add(fieldValueList);
                        }
                    }
                }
            }
        });
        createWorkitemV2Request.setFieldValueList(fieldValueLists);
        return createWorkitemV2Request;
    }

    @Override
    public IssuesWithBLOBs addIssue(PlatformIssuesUpdateRequest issuesRequest) {
        updateUserAKSK();
        issuesRequest.getProjectConfig();
        issuesRequest.getUserPlatformUserConfig();
        CreateWorkitemV2Request createWorkitemV2Request = getCreateWorkitemV2RequestByCustomFieIdList(issuesRequest.getCustomFieldList());
        String workitemIdentifier = asyncClient.createWorkitemV2(createWorkitemV2Request);
        issuesRequest.setPlatformId(workitemIdentifier);
        return issuesRequest;
    }

    @Override
    public IssuesWithBLOBs updateIssue(PlatformIssuesUpdateRequest request) {
        updateUserAKSK();
        List<PlatformCustomFieldItemDTO> template = getThirdPartCustomField(request.getProjectConfig());
        List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOS = updateFieldValue(request.getPlatformId(), template);
        List<PlatformCustomFieldItemDTO> platformCustomFieldItemDtOList = request.getCustomFieldList();

        Map<String,PlatformCustomFieldItemDTO> aly = new HashMap<>();
        platformCustomFieldItemDTOS.forEach(item -> aly.put(item.getCustomData(),item));

        Map<String,PlatformCustomFieldItemDTO> ms = new HashMap<>();
        platformCustomFieldItemDtOList.forEach(item -> ms.put(item.getCustomData(),item));

        for(Map.Entry<String, PlatformCustomFieldItemDTO> entry : ms.entrySet()){
            PlatformCustomFieldItemDTO msValue = entry.getValue();
            PlatformCustomFieldItemDTO alyValue = aly.get(entry.getKey());
            UpdateWorkItemRequest updateWorkItemRequest = new UpdateWorkItemRequest();
            updateWorkItemRequest.setIdentifier(request.getPlatformId());

            //统一赋值空字符串
            if(msValue.getValue() == null)msValue.setValue("");
            if(alyValue.getValue() == null)alyValue.setValue("");

            if(msValue == null && alyValue != null) {
                //ms有这个字段，aly没有这个字段 todo
            } else if (msValue != null && alyValue == null) {
                //ms没有这个字段，aly有这个字段 todo
            }
//            else if(msValue.getValue() == null && alyValue.getValue() != null){
//                //ms修改值为null todo
//            } else if(msValue.getValue() != null && alyValue.getValue() == null){
//                //ms修改空值为有值 todo
//            }
            else if(!StringUtils.equals(msValue.getValue()+"" , alyValue.getValue()+"")){

                if(StringUtils.equalsAny(msValue.getCustomData(), "101587","101586","space","workitemType")){
                    MSPluginException.throwException("无法修改归属项目、者工作项类型、工时");
                }
                if(StringUtils.equals(msValue.getCustomData(), "status")){
                    msValue.setValue(rmSuffix(msValue.getValue()+""));
                    if(StringUtils.equals(msValue.getValue()+"" , alyValue.getValue()+"")) continue;
                }
                if(StringUtils.equals(msValue.getCustomData(), "document")){
                    //先把图片替换为空字符串，不对比图片
//                    String descMS = replaceImage(msValue.getValue()+"");
//                    String descALY = replaceImage(alyValue.getValue()+"");
//                    if(StringUtils.equals(descMS, descALY)){
//                        continue;
//                    }else{
                        String input = msValue.getValue()+"";
                        input = loadDescImage(input);
                        updateWorkItemRequest.setPropertyValue(input);
//                    }
//                }else{
                }
                //ms修改旧值为新值 todo
                updateWorkItemRequest.setPropertyValue(msValue.getValue()+"");
                updateWorkItemRequest.setPropertyKey(msValue.getCustomData());
                updateWorkItemRequest.setFieldType(msValue.getCustomData());
                if(StringUtils.equalsAny(msValue.getCustomData(),"workitem.verifier","workitem.tracker","ak.issue.member","assignedTo")){
                    if(StringUtils.equals(rmSuffix(msValue.getValue()+""), alyValue.getValue()+"")){
                        continue;
                    }
                    if(StringUtils.isNotBlank(msValue.getValue()+"") && !StringUtils.equals("[]",msValue.getValue()+"")){
                        String arrayAccount = rmSuffix(msValue.getValue()+"");
                        // 去掉中括号
//                        arrayAccount = arrayAccount.substring(1,arrayAccount.length()-1);
                        arrayAccount = arrayAccount.replace("[","");
                        arrayAccount = arrayAccount.replace("]","");
                        //去掉所有双引号
                        arrayAccount = arrayAccount.replace("\"","");
                        updateWorkItemRequest.setPropertyValue(arrayAccount);
                        updateWorkItemRequest.setFieldType("user");
                    }

                    if(StringUtils.equals("[]",msValue.getValue()+"")){
                        updateWorkItemRequest.setPropertyValue("");
                    }
                }

                if(!StringUtils.equalsAny(updateWorkItemRequest.getFieldType(),
                        "user",
                        "workitem.verifier",
                        "workitem.tracker",
                        "ak.issue.member",
                        "assignedTo",
                        "status",
                        "document",
                        "subject"
                        )){
                    updateWorkItemRequest.setFieldType("customField");
                }

                asyncClient.updateWorkItem(updateWorkItemRequest);
            }//修改预计工时，完成工时，修改计划开始时间，计划完成时间有问题
        }
        return request;
    }

    private String replaceImage(String input) {
        Pattern pattern = Pattern.compile("(\\!\\[.*?\\]\\((.*?)\\))");
        if (StringUtils.isBlank(input)) {
            return input;
        } else {
            Matcher matcher = pattern.matcher(input);

            while(matcher.find()) {
                try {
//                    String path = matcher.group(2);
//                    if (!path.contains("/resource/md/get/url") && !path.contains("/resource/md/get/path")) {
//                        String os = System.getProperty("os.name").toLowerCase();
//                        String name;
//                        if (path.contains("/resource/md/get/")) {
//                            if(os.indexOf("windows") != -1){
//                                name = path.substring(path.indexOf("/resource/md/get/") + 18);
//                            }else{
//                                name = path.substring(path.indexOf("/resource/md/get/") + 17);
//                            }
//                            input = input.replace(matcher.group(0), "");
//                        } else if (path.contains("/resource/md/get")) {
//                            File file = null;
//                            if(os.indexOf("windows") != -1){
//                                name = path.substring(path.indexOf("/resource/md/get/") + 27);
//                                String filePath = "C:\\opt\\metersphere\\data\\image\\markdown\\"+ URLDecoder.decode(name, StandardCharsets.UTF_8.name());
//                                file = new File(filePath);
//                            }else{
//                                name = path.substring(path.indexOf("/resource/md/get") + 26);
//                                file = new File("/opt/metersphere/data/image/markdown/" + URLDecoder.decode(name, StandardCharsets.UTF_8.name()));
//                            }
//                            files.add(file);
//                            input = input.replace(matcher.group(0), "");
//                        } else {
//                            //网络图片
//                            java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
//                            String byte64Str = encoder.encodeToString(asyncClient.openFile(path));
//                            byte64Str = " <img src=\"data:image/png;base64,"+byte64Str+"\"/>";
//                            input = input.replace(matcher.group(0), byte64Str);
//                        }
//                    }
                    input = input.replace(matcher.group(0), "");
                } catch (Exception var7) {
                    LogUtil.error(var7.getMessage(), var7);
                }
            }
            return input;
        }
    }

    @Override
    public void deleteIssue(String id) {
        updateUserAKSK();
        asyncClient.deleteWorkitem(id);
    }

    @Override
    public void validateIntegrationConfig(){
        asyncClient.getProjectList();
    }


    @Override
    public void validateProjectConfig(String projectConfigStr) {
        AliyunProjexProjectConfig projectConfig = getProjectConfig(projectConfigStr);
        List<ListProjectsResponseBody.Projects> projectList = asyncClient.getProjectList();
        for(ListProjectsResponseBody.Projects item : projectList){
            if(StringUtils.equals(item.getIdentifier(), projectConfig.getProjectIdentifier())){
                return;
            }
        }
        throw new RuntimeException("项目不存在");
    }

    public AliyunProjexProjectConfig getProjectConfig(String configStr) {
        if (StringUtils.isBlank(configStr)) {
            MSPluginException.throwException("请在项目中添加项目配置！");
        }
        AliyunProjexProjectConfig projectConfig = JSON.parseObject(configStr, AliyunProjexProjectConfig.class);
        return projectConfig;
    }


    @Override
    public void validateUserConfig(String userConfig) {
        setUserConfig(userConfig);
//        asyncClient.getProjectList();
    }


    @Override
    public boolean isAttachmentUploadSupport() {
        return false;
    }

    @Override
    public SyncIssuesResult syncIssues(SyncIssuesRequest request) {
        return null;
    }

    @Override
    public List<PlatformCustomFieldItemDTO> getThirdPartCustomField(String projectConfig) {
        aliyunProjexProjectConfig = getProjectConfig(projectConfig);
        ListWorkItemAllFieldsResponseBody listWorkItemAllFieldsResponseBody = asyncClient.listWorkItemAllFields();
        List<ListWorkItemAllFieldsResponseBody.Fields> fieldsListTemp = listWorkItemAllFieldsResponseBody.getFields();
        List<ListWorkItemAllFieldsResponseBody.Fields> fieldsList = new ArrayList<>();
        fieldsListTemp.forEach(item ->{
            if(item.getClassName() != null && item.getIsShowWhenCreate() != null) {
                fieldsList.add(item);
            }
        });
        List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOS = fieldsList.stream().map(item ->
            getMSFileld(item)
        ).collect(Collectors.toList());
        addTitleDescription(platformCustomFieldItemDTOS);
        Character filedKey = 'A';
        for(PlatformCustomFieldItemDTO item : platformCustomFieldItemDTOS){
            item.setId(item.getCustomData());
            item.setKey(String.valueOf(filedKey++));
        }
        return platformCustomFieldItemDTOS;
    }

    private void addTitleDescription(List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOS){
        PlatformCustomFieldItemDTO title = new PlatformCustomFieldItemDTO();
        title.setName("标题");
        title.setType(CustomFieldType.INPUT.getValue());
        title.setCustomData("subject");
        title.setRequired(true);
        platformCustomFieldItemDTOS.add(0, title);

        PlatformCustomFieldItemDTO description = new PlatformCustomFieldItemDTO();
        description.setName("描述");
        description.setType(CustomFieldType.RICH_TEXT.getValue());
        description.setCustomData("document");
        description.setDefaultValue(aliyunProjexProjectConfig.getProjectxDesc());
        platformCustomFieldItemDTOS.add(description);
    }

    private List<ListOrganizationMembersResponseBody.Members> listOrganizationMembers(){
//        if(members != null){
//            return members;
//        }
        members = asyncClient.listOrganizationMembers();
        members.forEach(item -> {
            item.setAccountId(item.getAccountId()+suffix);
        });
        return members;
    }

    private PlatformCustomFieldItemDTO getMSFileld(ListWorkItemAllFieldsResponseBody.Fields item){
        PlatformCustomFieldItemDTO platformCustomFieldItemDTO = new PlatformCustomFieldItemDTO();
        platformCustomFieldItemDTO.setRequired(item.getIsRequired());
        platformCustomFieldItemDTO.setName(item.getName());
        platformCustomFieldItemDTO.setCustomData(item.getIdentifier());
        platformCustomFieldItemDTO.setDefaultValue(item.getDefaultValue());
        switch (item.getFormat()){
            case "list":
                switch (item.getIdentifier()){
                    case "workitemType":
                        platformCustomFieldItemDTO.setOptions(getOptionsStr(asyncClient.listProjectWorkitemTypes("Bug")));
                        platformCustomFieldItemDTO.setDefaultValue(aliyunProjexProjectConfig.getAliyunIssueTypeId());
                        break;
                    case "status":
                        List<ListWorkItemWorkFlowStatusResponseBody.Statuses> statusList = asyncClient.listWorkItemWorkFlowStatus();
                        statusList.forEach(status -> {
                            status.setIdentifier(status.getIdentifier() + suffix);
                            if("待确认".equals(status.getName())) platformCustomFieldItemDTO.setDefaultValue(status.getIdentifier());
                        });
                        platformCustomFieldItemDTO.setOptions(getOptionsStr(statusList));
                        break;
                    case "assignedTo":
                        platformCustomFieldItemDTO.setOptions(getOptionsStr(listOrganizationMembers(), "accountId","organizationMemberName"));
                        if(StringUtils.isNotBlank(request.getUserPlatformInfo())) {
                            platformCustomFieldItemDTO.setDefaultValue(aliyunProjexUserConfig.getAccountId()+suffix);
                        }
                        break;
                    case "workitem.verifier":
                        platformCustomFieldItemDTO.setOptions(getOptionsStr(listOrganizationMembers(), "accountId","organizationMemberName"));
                        if(StringUtils.isNotBlank(request.getUserPlatformInfo())) {
                            platformCustomFieldItemDTO.setDefaultValue(aliyunProjexUserConfig.getAccountId()+suffix);
                        }
                        break;
//                    case "priority":
//                        platformCustomFieldItemDTO.setOptions(getOptionsStr(item.getOptions(), "identifier","value"));
//                        break;
                    case "space"://项目归属先不获取，默认只有当前项目
                        List<ListProjectsResponseBody.Projects> projectList =asyncClient.getProjectList();
                        platformCustomFieldItemDTO.setOptions(getOptionsStr(projectList));
                        platformCustomFieldItemDTO.setDefaultValue(aliyunProjexProjectConfig.getProjectIdentifier());
                        break;
                    case "sprint":
                        platformCustomFieldItemDTO.setOptions(getOptionsStr(asyncClient.listSprints()));
                        break;
//                    case "seriousLevel":
//                        platformCustomFieldItemDTO.setOptions(getOptionsStr(item.getOptions(), "identifier","value"));
//                        break;
                    default:
//                        if("CustomField".equals(item.getType())){
                            if("user".equals(item.getClassName())){
                                platformCustomFieldItemDTO.setOptions(getOptionsStr(listOrganizationMembers(), "accountId","organizationMemberName"));
                            } else if(StringUtils.equals("cascading",item.getClassName())){
                                //层级字段先不搞
                            } else {
                                platformCustomFieldItemDTO.setOptions(getOptionsStr(item.getOptions(), "identifier","value"));
                            }
//                        }else{
//                            platformCustomFieldItemDTO.setOptions(getOptionsStr(item.getOptions(), "identifier","value"));
//                        }
                        break;
                }
                platformCustomFieldItemDTO.setType(CustomFieldType.SELECT.getValue());
                break;
            case "multiList":
                switch (item.getIdentifier()){
                    case "ak.issue.member":
                        platformCustomFieldItemDTO.setOptions(getOptionsStr(listOrganizationMembers(), "accountId","organizationMemberName"));
                        break;
                    case "workitem.tracker":
                        platformCustomFieldItemDTO.setOptions(getOptionsStr(listOrganizationMembers(), "accountId","organizationMemberName"));
                        break;
                    case "tag":
//                        platformCustomFieldItemDTO.setOptions(getOptionsStr(listOrganizationMembers(), "accountId","organizationMemberName"));
                        break;
                    case "relatedSpace"://先不管

                        break;
                    default:
                        if(StringUtils.equals(item.getClassName(),"user")) {
                            platformCustomFieldItemDTO.setOptions(getOptionsStr(listOrganizationMembers(), "accountId","organizationMemberName"));
                        } else {
//                            platformCustomFieldItemDTO.setOptions(getOptionsStr(item.getOptions()));
                        }
                        break;
                }
                platformCustomFieldItemDTO.setType(CustomFieldType.MULTIPLE_SELECT.getValue());
                break;
            case "input":
                switch (item.getIdentifier()){
                    case "79":
                        platformCustomFieldItemDTO.setType(CustomFieldType.DATE.getValue());
                        break;
                    case "80":
                        platformCustomFieldItemDTO.setType(CustomFieldType.DATE.getValue());
                        break;
                    case "101586":
                        platformCustomFieldItemDTO.setType(CustomFieldType.FLOAT.getValue());
                        break;
                    case "101587":
                        platformCustomFieldItemDTO.setType(CustomFieldType.FLOAT.getValue());
                        break;
                    default:
                        if(StringUtils.equals(item.getClassName(),"date")) {
                            platformCustomFieldItemDTO.setType(CustomFieldType.DATE.getValue());
                        } else if(StringUtils.equals(item.getClassName(),"dateTime")) {
                            platformCustomFieldItemDTO.setType(CustomFieldType.DATETIME.getValue());
                        } else if(StringUtils.equals(item.getClassName(),"int")) {
                            platformCustomFieldItemDTO.setType(CustomFieldType.INT.getValue());
                        } else if(StringUtils.equals(item.getClassName(),"float")) {
                            platformCustomFieldItemDTO.setType(CustomFieldType.FLOAT.getValue());
                        } else if(StringUtils.equals(item.getClassName(),"text")){
                            platformCustomFieldItemDTO.setType(CustomFieldType.TEXTAREA.getValue());
                        } else {
                            platformCustomFieldItemDTO.setType(CustomFieldType.INPUT.getValue());
                        }
                        break;
                }
                break;
            default:
                break;
        }
        return platformCustomFieldItemDTO;
    }

    private String getOptionsStr(List list){
        return getOptionsStr(list, "identifier", "name");
    }
    private String getOptionsStr(List list, String valueKey, String textKey){
        List options = new ArrayList<>();
        list.forEach(val -> {
            Map<String,Object> map = JSON.parseMap(JSON.toJSONString(val));
            Map jsonObject = new LinkedHashMap();
            jsonObject.put("value", map.get(valueKey));
            jsonObject.put("text", map.get(textKey));
            options.add(jsonObject);
        });
        return JSON.toJSONString(options);
    }

    private void validateIssueType() {
        if (StringUtils.isBlank(aliyunProjexProjectConfig.getAliyunIssueTypeId())) {
            MSPluginException.throwException("请在项目中配置 " + this.key + " 缺陷类型！");
        }
    }

    private void validateProjectKey() {
        if (StringUtils.isBlank(aliyunProjexProjectConfig.getProjectIdentifier())) {
            MSPluginException.throwException("请在项目设置配置 " + this.key + "项目标识符");
        }
    }

    private void validateProject(){
        validateProjectKey();
        validateIssueType();
    }

//    private List<ListWorkitemsResponseBody.Workitems> filterSyncJiraIssueByCreated(List<ListWorkitemsResponseBody.Workitems> listWorkitems, SyncAllIssuesRequest syncRequest){
//        if (syncRequest.getCreateTime() == null) {
//            return listWorkitems;
//        }
//        List<ListWorkitemsResponseBody.Workitems> filterIssues = listWorkitems.stream().filter(item -> {
//            long createTimeMills = 0;
//            try {
//                createTimeMills = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse((String) item.getFields().get("created")).getTime();
//                if (syncRequest.isPre()) {
//                    return createTimeMills <= syncRequest.getCreateTime().longValue();
//                } else {
//                    return createTimeMills >= syncRequest.getCreateTime().longValue();
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }).collect(Collectors.toList());
//        return filterIssues;
//    }

    private String getMDByHtml(String htmlTemp)throws Exception{
        JSONObject json = JSONObject.parseObject(htmlTemp);
        String html = json.getString("htmlValue");
        String markDown = asyncClient.getMDByHTML(html);
        Map<String,Object> map = loadHtmlImageAndInput(markDown);
        String input = map.get("input")+"";
        map.remove("input");
        for(String key: map.keySet()){
            input = input.replace(key, downloadHtmlImage(key));
        }
        return input;
//        Document doc = Jsoup.parse(html);
//        Elements headings = doc.select("*");
//        String htmlTemp = "";
//        for (Element heading : headings) {
//            if(StringUtils.equalsAny(heading.tag().getName(), "span", "body")){
//                String text = heading.text();
//                htmlTemp += text+"\\n";
//                System.out.println(text);
//            }
//            if(heading.tag().getName().equals("img")){
//                String stc = heading.attributes().get("src");
//                stc = stc.substring(2,stc.length()-2);
//                htmlTemp += stc+"\\n";
//                System.out.println(stc);
//            }
//        }
//        return htmlTemp;
    }

    public String downloadHtmlImage(String key){
        return null;
    }

    private List<PlatformCustomFieldItemDTO> updateFieldValue(String id, List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOS){
//        List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOList = JSON.parseArray(super.defaultCustomFields, PlatformCustomFieldItemDTO.class);
        GetWorkItemInfoResponseBody.Workitem workitem = asyncClient.getWorkItemInfo(id);

        List<PlatformCustomFieldItemDTO> tempList = new ArrayList<>();
        List<GetWorkItemInfoResponseBody.CustomFields> customFields = workitem.getCustomFields();
        HashMap<String,String> map = new HashMap<>();
        if(customFields != null && customFields.size() != 0){
            customFields.forEach(item->map.put(item.getFieldIdentifier(),item.getValue()));
        }

        for(PlatformCustomFieldItemDTO item : platformCustomFieldItemDTOS){
            PlatformCustomFieldItemDTO temp = new PlatformCustomFieldItemDTO();
            BeanUtils.copyBean(temp,item);
            switch (item.getCustomData()){
                case "assignedTo":
                    temp.setValue(workitem.getAssignedTo()+suffix);
                    break;
                case "subject":
                    temp.setValue(workitem.getSubject());
                    break;
                case "document":
                    String document = workitem.getDocument();
//                    if(StringUtils.isNotBlank(document)){
//                        try {
//                            String md = getMDByHtml(document);
//                            temp.setValue(md);
//                        }catch (Exception e){
                            temp.setValue(document);
//                        }
////                    String md = htmlDesc2MsDesc("<article class=\"4ever-article\"><p style=\"text-align:left;text-indent:0;margin-left:0;margin-top:0;margin-bottom:0\"><span data-type=\"text\">测试desc</span></p><p style=\"text-align:left;text-indent:0;margin-left:0;margin-top:0;margin-bottom:0\"><span data-type=\"text\"></span><img src=\"https://devops.aliyun.com/projex/api/workitem/file/url?fileIdentifier=8c79ff7329141b8fd4f508cd29\" style=\"width:528px;height:83.656050955414px\"/><span data-type=\"text\"></span></p></article>");
//                    }
                    break;
                case "workitemType":
                    temp.setValue(workitem.getWorkitemTypeIdentifier());
                    break;
                case "status":
                    temp.setValue(workitem.getStatusIdentifier()+suffix);
                    break;
                case "workitem.verifier":
                    if(workitem.getVerifier().size() != 0) temp.setValue(workitem.getVerifier().get(0)+suffix);
                    break;
                case "space":
                    temp.setValue(workitem.getSpaceIdentifier());
                    break;
                case "sprint":
                    if(workitem.getSprint().size() != 0) temp.setValue(workitem.getSprint().get(0));
                    break;
                case "ak.issue.member":
                    List<String> participantList = new ArrayList<>();
                    if(workitem.getParticipant() != null && workitem.getParticipant().size() != 0){
                        for(String p:workitem.getParticipant()){
                            participantList.add(p+suffix);
                        }
                        temp.setValue(JSON.toJSONString(participantList));
                    }
                    break;
                case "workitem.tracker":
                    List<String> trackerList = new ArrayList<>();
                    if(workitem.getTracker() != null && workitem.getTracker().size() != 0){
                        for(String p:workitem.getTracker()){
                            trackerList.add(p+suffix);
                        }
                        temp.setValue(JSON.toJSONString(trackerList));
                    }
//                    temp.setValue(JSON.toJSONString(workitem.getTracker()+suffix));
                    break;
                case "tag":
                    temp.setValue(JSON.toJSONString(workitem.getTag()));
                    break;
                default:
                    String tempValue = "";
                    tempValue = map.get(item.getCustomData());
//                    if(StringUtils.equals(item.getType(),"datetime")){
//                        temp.setValue(tempValue);
//                    }else if(StringUtils.equals(item.getType(),"date")){
//                        temp.setValue(tempValue);
//                     }else
                     if(StringUtils.equals(item.getType(),"multipleSelect")){
                        if(StringUtils.isBlank(tempValue)){
                            tempValue = "[]";
                        }else{
                            tempValue = "["+tempValue+"]";
                        }
                    }
                    temp.setValue(tempValue);
                    break;
            }
            tempList.add(temp);
        }
        return tempList;
    }
    private PlatformIssuesDTO getUpdateIssue(ListWorkitemsResponseBody.Workitems listWorkitem, List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOS) {
        PlatformIssuesDTO platformIssuesDTO = new PlatformIssuesDTO();
        platformIssuesDTO.setId(listWorkitem.getIdentifier());
        platformIssuesDTO.setPlatform(this.key);
        platformIssuesDTO.setPlatformId(listWorkitem.getIdentifier());
        platformIssuesDTO.setCustomFieldList(updateFieldValue(listWorkitem.getIdentifier(), platformCustomFieldItemDTOS));
        platformIssuesDTO.setCustomFields(defaultCustomFields);
        return platformIssuesDTO;
    }

    public void syncProjexIssueAttachments(SyncIssuesResult syncIssuesResult, PlatformIssuesDTO issue, ListWorkitemsResponseBody.Workitems jiraIssue) {
        try {
//            List attachments = (List) jiraIssue.getFields().get(ATTACHMENT_NAME);
//            // 同步Jira中新的附件
//            if (CollectionUtils.isNotEmpty(attachments)) {
//                Map<String, List<PlatformAttachment>> attachmentMap = syncIssuesResult.getAttachmentMap();
//                attachmentMap.put(issue.getId(), new ArrayList<>());
//                for (int i = 0; i < attachments.size(); i++) {
//                    Map attachment = (Map) attachments.get(i);
//                    String filename = attachment.get("filename").toString();
//                    if ((issue.getDescription() == null || !issue.getDescription().contains(filename))
//                            && (issue.getCustomFields() == null || !issue.getCustomFields().contains(filename))
//                    ) {
//                        PlatformAttachment syncAttachment = new PlatformAttachment();
//                        // name 用于查重
//                        syncAttachment.setFileName(filename);
//                        // key 用于获取附件内容
//                        syncAttachment.setFileKey(attachment.get("content").toString());
//                        attachmentMap.get(issue.getId()).add(syncAttachment);
//                    }
//                }
//            }

        } catch (Exception e) {
            LogUtil.error(e);
            MSPluginException.throwException(e);
        }
    }

    @Override
    public void syncAllIssues(SyncAllIssuesRequest syncRequest) {
        System.out.println("sync start");
        aliyunProjexProjectConfig = getProjectConfig(syncRequest.getProjectConfig());
        System.out.println("projectconfig end");
        validateProject();
        System.out.println("check end");
        super.defaultCustomFields = syncRequest.getDefaultCustomFields();
        List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOS = getThirdPartCustomField(syncRequest.getProjectConfig());
        System.out.println("field end");
        SyncAllIssuesResult syncIssuesResult = new SyncAllIssuesResult();
        List<ListWorkitemsResponseBody.Workitems> listWorkitems = asyncClient.listWorkitems("Bug");
        System.out.println("get bug end");
        List<String> allIds = listWorkitems.stream().map(ListWorkitemsResponseBody.Workitems::getIdentifier).collect(Collectors.toList());
        System.out.println("all ids end");
        syncIssuesResult.setAllIds(allIds);
        listWorkitems.forEach(item ->{
            System.out.println("for workItem "+item.getSubject());
            PlatformIssuesDTO issuesWithBLOBs = getUpdateIssue(item, platformCustomFieldItemDTOS);
            System.out.println("for workItem item end");
            issuesWithBLOBs.setCustomFields(JSON.toJSONString(issuesWithBLOBs.getCustomFieldList()));
            syncIssuesResult.getUpdateIssues().add(issuesWithBLOBs);
//            asyncClient.listAttachment(issuesWithBLOBs);
//            syncProjexIssueAttachments(syncIssuesResult, issuesWithBLOBs, item);
        });
        HashMap<Object, Object> syncParam = buildSyncAllParam(syncIssuesResult);
        System.out.println("buildSyncAllParam end");
        Consumer<Map> consumer = syncRequest.getHandleSyncFunc();
        consumer.accept(syncParam);
        System.out.println("collbak end");
    }

    @Override
    public List<SelectOption> getProjectOptions(GetOptionRequest request) {
        String method = request.getOptionMethod();
        try {
            // 这里反射调用 getIssueTypes 获取下拉框选项
            return (List<SelectOption>) this.getClass().getMethod(method, request.getClass()).invoke(this, request);
        } catch (InvocationTargetException e) {
            MSPluginException.throwException(e.getTargetException());
        } catch (Exception e) {
            MSPluginException.throwException(e);
        }
        return null;
    }

    public List<SelectOption> getIssueTypes(GetOptionRequest request) {
        aliyunProjexProjectConfig = getProjectConfig(request.getProjectConfig());
        List<ListProjectWorkitemTypesResponseBody.WorkitemTypes> workitemTypes = asyncClient.listProjectWorkitemTypes("Bug");
        return workitemTypes
                .stream()
                .map(item -> new SelectOption(item.getName(), item.getIdentifier()))
                .collect(Collectors.toList());
    }

    public List<SelectOption> getStoryTypes(GetOptionRequest request) {
        aliyunProjexProjectConfig = getProjectConfig(request.getProjectConfig());
        List<ListProjectWorkitemTypesResponseBody.WorkitemTypes> workitemTypes = asyncClient.listProjectWorkitemTypes("Req");
        return workitemTypes
                .stream()
                .map(item -> new SelectOption(item.getName(), item.getIdentifier()))
                .collect(Collectors.toList());
    }

    @Override
    public void syncIssuesAttachment(SyncIssuesAttachmentRequest request) {
        GetWorkitemAttachmentCreatemetaResponseBody getWorkitemAttachmentCreatemetaResponseBody = asyncClient.getWorkitemAttachmentCreatemeta(request);
        asyncClient.putObject(request, getWorkitemAttachmentCreatemetaResponseBody);
        asyncClient.workitemAttachmentCreate(request,getWorkitemAttachmentCreatemetaResponseBody);
    }

    @Override
    public List<PlatformStatusDTO> getStatusList(String projectConfig) {
        return new ArrayList<>();
    }
}
