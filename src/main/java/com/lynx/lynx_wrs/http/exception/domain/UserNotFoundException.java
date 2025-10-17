package com.lynx.lynx_wrs.http.exception.domain;
import th.co.exvention.pet.http.exception.AppException;
import th.co.exvention.pet.http.exception.ErrorCode;
/**
 * UserNotFoundException
 * Exception ที่เกิดขึ้นเมื่อไม่พบผู้ใช้ในระบบ
 * ใช้ ErrorCode.USER_NOT_FOUND
 * Created: 31/8/2025
 */
public class UserNotFoundException extends AppException {
    public UserNotFoundException() { super(ErrorCode.USER_NOT_FOUND); }
}
