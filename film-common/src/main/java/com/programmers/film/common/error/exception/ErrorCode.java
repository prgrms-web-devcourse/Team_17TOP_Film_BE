package com.programmers.film.common.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input value"),
    INVALID_TYPE_VALUE(400, "C002", "Invalid Type Value"),
    ENTITY_NOT_FOUND(400, "C003", "Entity Not Found"),
    METHOD_NOT_ALLOWED(405, "C004", "Invalid Input value"),
    INTERNAL_SERVER_ERROR(500, "C005", "Server Error"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is denied"),

    // Login
    NICKNAME_NULL(400, "L001", "Nickname Not Exist"),
    USER_NOT_EXIST(400, "L002", "User Not Exist"),
    KAKAO_ERROR(400, "L003", "Kakao Server Error"),
    SERVER_ERROR(400, "L004", "DB Server Error"),

    // Map
    LOCATION_ERROR(400, "M001", "Location Input Error"),

    // User
    NICKNAME_DUPLICATED(400, "U001", "Nickname Is Duplicated"),
    ALREADY_CREATED(400, "U002", "User Already Created"),
    NICKNAME_ERROR(400,"U003", "Nickname Is Out Of The Regex"),

    // Post
    POST_ID_ERROR(400, "P001", "Post Id Not Exist"),
    POST_FORBIDDEN(403, "P002", "Post Access Denied"),
    TITLE_ERROR(400, "P003", "Title Error"),
    PREVIEW_TEXT_ERROR(400, "P004", "Preview Text Error"),
    OPENABLE_AT_ERROR(400, "P005", "Date Range Error"),
    AUTHORITY_ERROR(400, "P006", "Authority Error"),
    AUTHOR_NICKNAME_ERROR(400, "P007", "Author Nickname Error"),
    IMAGE_ERROR(400, "P008", "Image Error"),
    CONTENT_ERROR(400, "P009", "Content Error");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}