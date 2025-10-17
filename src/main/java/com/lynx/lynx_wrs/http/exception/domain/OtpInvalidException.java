package com.lynx.lynx_wrs.http.exception.domain;

import com.lynx.lynx_wrs.http.exception.AppException;
import com.lynx.lynx_wrs.http.exception.ErrorCode;

public class OtpInvalidException extends AppException {
    public OtpInvalidException() { super(ErrorCode.OTP_INVALID); }
}
