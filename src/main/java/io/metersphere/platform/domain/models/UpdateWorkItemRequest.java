package io.metersphere.platform.domain.models;

import lombok.Data;

@Data
public class UpdateWorkItemRequest {
    private String propertyKey;
    private String propertyValue;
    private String identifier;
    private String fieldType;
}
