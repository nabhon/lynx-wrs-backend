package com.lynx.lynx_wrs.http.exception.domain;


import com.lynx.lynx_wrs.http.exception.AppException;
import com.lynx.lynx_wrs.http.exception.ErrorCode;

public class InvalidJwtException extends AppException {
    public InvalidJwtException() { super(ErrorCode.INVALID_JWT); }
}
