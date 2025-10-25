package com.lynx.lynx_wrs.http.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ErrorCode
 * Enum สำหรับรหัสข้อผิดพลาดต่างๆ ในระบบ
 * ประกอบด้วยรหัสสถานะ HTTP และข้อความที่เกี่ยวข้อง
 * Created: 31/8/2025
 */

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Auth
    INVALID_JWT("AUTH-001", HttpStatus.UNAUTHORIZED, "โทเค็นไม่ถูกต้องหรือหมดอายุแล้ว"),
    PERMISSION_DENIED("AUTH-002", HttpStatus.FORBIDDEN, "คุณไม่มีสิทธิ์ในการเข้าถึง"),
    TOKEN_EXPIRED("AUTH-003", HttpStatus.UNAUTHORIZED, "โทเค็นหมดอายุแล้ว"),
    INVALID_CREDENTIALS("AUTH-004", HttpStatus.UNAUTHORIZED, "ข้อมูลรับรองไม่ถูกต้อง"),
    AUTH_REVOKED("AUTH-005", HttpStatus.UNAUTHORIZED, "โทเค็นถูกเพิกถอนแล้ว กรุณาเข้าสู่ระบบใหม่"),
    REFRESH_TOKEN_EXPIRED("AUTH-006", HttpStatus.UNAUTHORIZED, "Refresh token หมดอายุแล้ว กรุณาเข้าสู่ระบบใหม่"),
    INVALID_REFRESH_TOKEN("AUTH-007", HttpStatus.UNAUTHORIZED, "Refresh token ไม่ถูกต้อง กรุณาเข้าสู่ระบบใหม่"),

    // User
    USER_NOT_FOUND("USER-001", HttpStatus.NOT_FOUND, "ไม่พบผู้ใช้"),
    EMAIL_ALREADY_TAKEN("USER-002", HttpStatus.CONFLICT, "อีเมลนี้มีผู้ใช้แล้ว"),
    USERNAME_ALREADY_TAKEN("USER-003", HttpStatus.CONFLICT, "ชื่อนี้มีผู้ใช้แล้ว"),
    INVALID_PASSWORD("USER-004", HttpStatus.BAD_REQUEST, "รหัสผ่านไม่ถูกต้อง"),
    USER_INACTIVE("USER-005", HttpStatus.FORBIDDEN, "บัญชีผู้ใช้ถูกปิดใช้งาน"),
    USER_BANNED("USER-006", HttpStatus.FORBIDDEN, "บัญชีผู้ใช้ถูกระงับ"),
    INVALID_USER_TYPE("USER-007", HttpStatus.BAD_REQUEST, "ประเภทผู้ใช้ไม่ถูกต้อง"),
    PASSWORD_MISMATCH("USER-008", HttpStatus.BAD_REQUEST, "รหัสผ่านไม่ตรงกัน"),
    WEAK_PASSWORD("USER-009", HttpStatus.BAD_REQUEST, "รหัสผ่านไม่ปลอดภัยพอ"),
    INVALID_EMAIL_FORMAT("USER-010", HttpStatus.BAD_REQUEST, "รูปแบบอีเมลไม่ถูกต้อง"),
    ACCOUNT_LOCKED("USER-011", HttpStatus.FORBIDDEN, "บัญชีผู้ใช้ถูกล็อก"),
    ACCOUNT_ALREADY_VERIFIED("USER-012", HttpStatus.BAD_REQUEST, "บัญชีผู้ใช้ได้รับการยืนยันแล้ว"),
    VERIFICATION_CODE_EXPIRED("USER-013", HttpStatus.BAD_REQUEST, "รหัสยืนยันหมดอายุแล้ว"),
    INVALID_VERIFICATION_CODE("USER-014", HttpStatus.BAD_REQUEST, "รหัสยืนยันไม่ถูกต้อง"),
    PROFILE_NOT_FOUND("USER-015", HttpStatus.NOT_FOUND, "ไม่พบโปรไฟล์ผู้ใช้"),
    EMAIL_NOT_VERIFIED("USER-016", HttpStatus.FORBIDDEN, "อีเมลยังไม่ได้รับการยืนยัน"),
    PHONE_ALREADY_TAKEN("USER-017", HttpStatus.CONFLICT, "เบอร์โทรศัพท์นี้มีผู้ใช้แล้ว"),

    // OTP
    OTP_INVALID("OTP-001", HttpStatus.BAD_REQUEST, "OTP ไม่ถูกต้อง"),
    OTP_EXPIRED("OTP-002", HttpStatus.BAD_REQUEST, "OTP หมดอายุแล้ว"),
    OTP_NOT_FOUND("OTP-003", HttpStatus.NOT_FOUND, "ไม่พบ OTP"),
    OTP_ALREADY_USED("OTP-004", HttpStatus.BAD_REQUEST, "OTP ถูกใช้แล้ว"),
    OTP_REQUEST_LIMIT_EXCEEDED("OTP-005", HttpStatus.TOO_MANY_REQUESTS, "เกินขีดจำกัดการขอ OTP"),

    // Community
    COMMUNITY_NOT_FOUND("COMM-001", HttpStatus.NOT_FOUND, "ไม่พบชุมชน"),
    COMMUNITY_ALREADY_EXISTS("COMM-002", HttpStatus.CONFLICT, "ชุมชนนี้มีอยู่แล้ว"),
    COMMUNITY_MEMBER_NOT_FOUND("COMM-003", HttpStatus.NOT_FOUND, "ไม่พบสมาชิกชุมชน"),
    COMMUNITY_MEMBER_ALREADY_EXISTS("COMM-004", HttpStatus.CONFLICT, "สมาชิกชุมชนนี้มีอยู่แล้ว"),
    COMMUNITY_POST_NOT_FOUND("COMM-005", HttpStatus.NOT_FOUND, "ไม่พบโพสต์ในชุมชน"),
    COMMUNITY_POST_ALREADY_EXISTS("COMM-006", HttpStatus.CONFLICT, "โพสต์ในชุมชนนี้มีอยู่แล้ว"),
    COMMUNITY_COMMENT_NOT_FOUND("COMM-007", HttpStatus.NOT_FOUND, "ไม่พบความคิดเห็นในชุมชน"),
    COMMUNITY_COMMENT_ALREADY_EXISTS("COMM-008", HttpStatus.CONFLICT, "ความคิดเห็นในชุมชนนี้มีอยู่แล้ว"),
    COMMUNITY_FORUM_NOT_FOUND("COMM-009", HttpStatus.NOT_FOUND, "ไม่พบฟอรั่มในชุมชน"),
    COMMUNITY_FORUM_ALREADY_EXISTS("COMM-010", HttpStatus.CONFLICT, "ฟอรั่มในชุมชนนี้มีอยู่แล้ว"),

    // Post
    POST_NOT_FOUND("POST-001", HttpStatus.NOT_FOUND, "ไม่พบโพสต์"),
    POST_ALREADY_EXISTS("POST-002", HttpStatus.CONFLICT, "โพสต์นี้มีอยู่แล้ว"),
    POST_LIKE_NOT_FOUND("POST-003", HttpStatus.NOT_FOUND, "ไม่พบการถูกใจโพสต์"),

    // Schedule Post
    SCHEDULE_POST_NOT_FOUND("SCHEDULE-001", HttpStatus.NOT_FOUND, "ไม่พบโพสต์ที่ตั้งเวลา"),
    SCHEDULE_POST_CANNOT_MODIFY_PUBLISHED("SCHEDULE-002", HttpStatus.BAD_REQUEST, "ไม่สามารถแก้ไขโพสต์ที่ตั้งเวลาที่เผยแพร่แล้วได้"),
    SCHEDULE_POST_CANNOT_DELETE_PUBLISHED("SCHEDULE-003", HttpStatus.BAD_REQUEST, "ไม่สามารถลบโพสต์ที่ตั้งเวลาที่เผยแพร่แล้วได้"),
    SCHEDULE_POST_CANNOT_PUBLISH_PAST("SCHEDULE-004", HttpStatus.BAD_REQUEST, "ไม่สามารถตั้งเวลาโพสต์ในอดีตได้"),

    // Comment
    COMMENT_NOT_FOUND("COMMENT-001", HttpStatus.NOT_FOUND, "ไม่พบความคิดเห็น"),
    COMMENT_CAN_NOT_DELETE_BY_THIS_USER("COMMENT-002", HttpStatus.FORBIDDEN, "ไม่สามารถลบความคิดเห็นของผู้อื่นได้"),
    COMMENT_CAN_NOT_EDIT_BY_THIS_USER("COMMENT-003", HttpStatus.FORBIDDEN, "ไม่สามารถแก้ไขความคิดเห็นของผู้อื่นได้"),

    // Moderator
    MODERATOR_NOT_FOUND("MODERATOR-001", HttpStatus.NOT_FOUND, "ไม่พบผู้ดูแล"),
    MODERATOR_ALREADY_EXISTS("MODERATOR-002", HttpStatus.CONFLICT, "ผู้ดูแลนี้มีอยู่แล้ว"),
    MODERATOR_PERMISSION_NOT_FOUND("MODERATOR-003", HttpStatus.NOT_FOUND, "ไม่พบสิทธิ์ของผู้ดูแล"),
    MODERATOR_ALREADY_EXIST("MODERATOR-004", HttpStatus.CONFLICT, "ผู้ใช้คนนี้เป็นผู้ดูเเลชุมชนนี้อยู่เเล้ว"),

    //Role
    ROLE_NOT_FOUND("ROLE-001", HttpStatus.NOT_FOUND, "ไม่พบบทบาท"),
    ROLE_USER_ALREADY_EXIST("ROLE-002", HttpStatus.CONFLICT, "ผู้ใช้มีบทบาทนี้อยู่แล้ว"),

    // Setting
    SETTING_NOT_FOUND("SETTING-001", HttpStatus.NOT_FOUND, "ไม่พบการตั้งค่า"),
    // Common
    BAD_REQUEST("COMMON-001", HttpStatus.BAD_REQUEST, "คำขอไม่ถูกต้อง"),
    INTERNAL_ERROR("COMMON-500", HttpStatus.INTERNAL_SERVER_ERROR, "เกิดข้อผิดพลาดภายในเซิร์ฟเวอร์"),
    UNAUTHORIZED("COMMON-401", HttpStatus.UNAUTHORIZED, "ไม่พบ Token หรือ Token ไม่ถูกต้อง"),

    //ProjectMenbers
    USER_NOT_FOUND_IN_PROJECT("PROJECT-002", HttpStatus.NOT_FOUND, "User Not Found In Project"),

    //Project
    PROJECT_NOT_FOUND("PROJECT-001", HttpStatus.NOT_FOUND, "Project Not Found");


    private final String code;
    private final HttpStatus status;
    private final String message;

}
