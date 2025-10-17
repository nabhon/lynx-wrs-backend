package com.lynx.lynx_wrs.http.exception.domain;

import com.lynx.lynx_wrs.http.exception.AppException;
import com.lynx.lynx_wrs.http.exception.ErrorCode;

/**
 * EmailAlreadyTakenException
 * Exception ที่เกิดขึ้นเมื่อพยายามลงทะเบียนด้วยอีเมลที่มีอยู่แล้วในระบบ
 * ใช้ ErrorCode.EMAIL_ALREADY_TAKEN
 * Created: 31/8/2025
 */

public class EmailAlreadyTakenException extends AppException {
    public EmailAlreadyTakenException() { super(ErrorCode.EMAIL_ALREADY_TAKEN); }
}
