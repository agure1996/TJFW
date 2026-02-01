package com.example.tjfw.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * A message describing the status or outcome of the operation.
     * For example, "Success", "Error", or "Upload failed!".
     */
    @JsonProperty("message")
    private String message;
    /**
     * Optional data object providing additional details about the response.
     * This can include specific results, error details, or any other relevant information.
     */
    @JsonProperty("data")
    private T data;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }


}
