package com.lynx.lynx_wrs.http.exception.domain;


import com.lynx.lynx_wrs.http.exception.AppException;
import com.lynx.lynx_wrs.http.exception.ErrorCode;

public class CommunityAlreadyExistException extends AppException {
    public CommunityAlreadyExistException() {super(ErrorCode.COMMUNITY_ALREADY_EXISTS);
    }
}
