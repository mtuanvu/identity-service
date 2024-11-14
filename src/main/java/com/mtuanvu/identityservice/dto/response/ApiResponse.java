package com.mtuanvu.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) //các thuộc tính có giá trị null thi sẽ không hiển thị khi đối tượng được chuyển thành Json
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T result;
}
