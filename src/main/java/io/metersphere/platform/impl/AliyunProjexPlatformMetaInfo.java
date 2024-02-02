package io.metersphere.platform.impl;

import io.metersphere.platform.api.AbstractPlatformMetaInfo;

public class AliyunProjexPlatformMetaInfo extends AbstractPlatformMetaInfo {

    public static final String KEY = "AliyunProjex";

    public AliyunProjexPlatformMetaInfo() {
        super(AliyunProjexPlatformMetaInfo.class.getClassLoader());
    }

    @Override
    public String getVersion() {
        return "2.10.1";
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public boolean isThirdPartTemplateSupport() {
        return true;
    }
}
