package com.example.tea_leaves_project.Payload;

import com.example.tea_leaves_project.exception.CodeResponse;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {
    private int code;
    private String status;
    private String message;
    private String data;

    public static ResponseData resp(){
        return new ResponseDataBuilder().
                code(CodeResponse.OK.code).
                status(CodeResponse.OK.status).
                message(CodeResponse.OK.message).
                build();
    }

}
