package com.lynx.lynx_wrs.http.exception.domain;
import th.co.exvention.pet.http.exception.AppException;
import th.co.exvention.pet.http.exception.ErrorCode;

public class InvalidJwtException extends AppException {
    public InvalidJwtException() { super(ErrorCode.INVALID_JWT); }
}
