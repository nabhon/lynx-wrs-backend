package com.lynx.lynx_wrs.http.exception.domain;

import th.co.exvention.pet.http.exception.AppException;
import th.co.exvention.pet.http.exception.ErrorCode;
/**
 * UsernameAlreadyTakenException
 * ข้อยกเว้นที่เกิดขึ้นเมื่อชื่อผู้ใช้ที่พยายามลงทะเบียนถูกใช้งานแล้ว
 * ใช้สำหรับแจ้งให้ทราบว่าชื่อผู้ใช้ไม่สามารถใช้ได้
 * Created: 31/8/2025
 */

public class UsernameAlreadyTakenException extends AppException {
    public UsernameAlreadyTakenException() { super(ErrorCode.USERNAME_ALREADY_TAKEN); }
}
