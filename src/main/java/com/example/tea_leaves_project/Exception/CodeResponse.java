package com.example.tea_leaves_project.Exception;

import lombok.Getter;

import java.net.HttpURLConnection;

@Getter
public enum CodeResponse {
    OK(HttpURLConnection.HTTP_OK, "OK", "Thành công"),
    CREATED(HttpURLConnection.HTTP_CREATED, "Created", "Tạo mới thành công"),
    DELETED(HttpURLConnection.HTTP_NO_CONTENT, "Deleted", "Xóa thành công"),
    NOT_FOUND(HttpURLConnection.HTTP_NOT_FOUND, "NotFound", "Không tìm thấy thông tin yêu cầu"),
    INVALID_ARGUMENT(HttpURLConnection.HTTP_BAD_REQUEST, "InvalidArgument", "Tham số không hợp lệ"),
    INTERNAL(HttpURLConnection.HTTP_INTERNAL_ERROR, "InternalServerError", "Có lỗi xảy ra"),
    FORBIDDEN(HttpURLConnection.HTTP_FORBIDDEN, "Forbidden", "Không xác thực được thông tin người dùng"),
    UNAUTHORIZED(HttpURLConnection.HTTP_UNAUTHORIZED, "Unauthorized", "Bạn không có quyền truy cập tài nguyên"),
    FILE_IO_EXCEPTION(HttpURLConnection.HTTP_BAD_REQUEST, "FileException", "Có lỗi xảy ra khi xử lý file"),
    DATA_LOSS(HttpURLConnection.HTTP_INTERNAL_ERROR, "DataLoss", "Có lỗi xảy ra với dữ liệu"),
    EXISTED(HttpURLConnection.HTTP_BAD_REQUEST, "Existed", "Đã tồn tại"),
    INVALID_LENGTH(HttpURLConnection.HTTP_BAD_REQUEST, "InvalidLength", "Length field can not be greater than 99"),
    BAD_CREDENTIALS(HttpURLConnection.HTTP_BAD_REQUEST, "BadCredentials", "Sai thông tin đăng nhập");

    public int code;

    public String status;

    public String message;
    CodeResponse(int code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
