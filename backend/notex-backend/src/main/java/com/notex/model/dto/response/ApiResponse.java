// package com.notex.model.dto.response;

// import com.fasterxml.jackson.annotation.JsonInclude;

// /**
//  * Generic API response wrapper used by controllers.
//  * Contains a success flag, optional message and optional payload data.
//  */
// @JsonInclude(JsonInclude.Include.NON_NULL)
// public class ApiResponse<T> {

//     private boolean success;
//     private String message;
//     private T data;

//     public ApiResponse() { }

//     public ApiResponse(boolean success, String message, T data) {
//         this.success = success;
//         this.message = message;
//         this.data = data;
//     }

//     public boolean isSuccess() {
//         return success;
//     }

//     public void setSuccess(boolean success) {
//         this.success = success;
//     }

//     public String getMessage() {
//         return message;
//     }

//     public void setMessage(String message) {
//         this.message = message;
//     }

//     public T getData() {
//         return data;
//     }

//     public void setData(T data) {
//         this.data = data;
//     }

//     // Convenience factory methods
//     public static <T> ApiResponse<T> ok() {
//         return new ApiResponse<>(true, null, null);
//     }

//     public static <T> ApiResponse<T> ok(T data) {
//         return new ApiResponse<>(true, null, data);
//     }

//     public static <T> ApiResponse<T> ok(String message, T data) {
//         return new ApiResponse<>(true, message, data);
//     }

//     public static <T> ApiResponse<T> error(String message) {
//         return new ApiResponse<>(false, message, null);
//     }

//     public static <T> ApiResponse<T> error(String message, T data) {
//         return new ApiResponse<>(false, message, data);
//     }
// }

//New Code -
package com.notex.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private Boolean success;
    private String message;
    private T data;
    
    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }
}