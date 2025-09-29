package org.fhq.common.core.exception;

import lombok.Getter;
import org.fhq.common.util.ErrorCode;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

@Getter
public class BusinessException extends HttpStatusCodeException {

    private final ErrorCode code;

    public BusinessException(HttpStatusCode statusCode) {
        this(statusCode, ErrorCode.FAIL);
    }

    public BusinessException(HttpStatusCode statusCode, ErrorCode code) {
        super(statusCode);
        this.code = code;
    }
}
