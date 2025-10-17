package com.lynx.lynx_wrs.http.exception.domain;
import th.co.exvention.pet.http.exception.AppException;
import th.co.exvention.pet.http.exception.ErrorCode;

public class OtpInvalidException extends AppException {
    public OtpInvalidException() { super(ErrorCode.OTP_INVALID); }
}
