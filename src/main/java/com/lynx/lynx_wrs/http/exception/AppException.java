package com.lynx.lynx_wrs.http.exception;

import org.springframework.http.HttpStatus;

/**
 * AppException
 * เป็นคลาสที่ใช้สำหรับจัดการข้อผิดพลาดในแอปพลิเคชัน
 * โดยมีการกำหนดรหัสข้อผิดพลาดและข้อความที่เกี่ยวข้อง
 * Created: 31/8/2025
 */
public class AppException extends RuntimeException {
    private final ErrorCode code;

    /** Constructors */
    public AppException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    /** Constructor with custom message */
    public AppException(ErrorCode code, String overrideMessage) {
        super(overrideMessage);
        this.code = code;
    }


    public HttpStatus getHttpStatus() { return code.getStatus(); }
    public ErrorCode getCode() { return code; }
}