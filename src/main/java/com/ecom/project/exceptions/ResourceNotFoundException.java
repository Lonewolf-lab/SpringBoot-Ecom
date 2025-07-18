package com.ecom.project.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String ResourceName;
    String field;
    String fieldName;
    Long fieldId;

    public ResourceNotFoundException(){

    }

    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s : '%s'", resourceName, field, fieldName));
        ResourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s : '%s'", resourceName, field, fieldId));
        ResourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }
}
