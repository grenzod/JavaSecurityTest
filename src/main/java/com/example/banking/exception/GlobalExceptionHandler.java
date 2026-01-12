package com.example.banking.exception;

import com.example.banking.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Pattern SENSITIVE_DATA_PATTERN =
            Pattern.compile("(TransactionID|Account|InDebt|Have|Time)=([^,\\s]+)");

    // Xử lý lỗi chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        log.error("Unexpected Error: ", ex);
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Xử lý lỗi giao dịch
    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ApiResponse<Object>> handleTransactionException(TransactionException ex) {
        // Tạo chuỗi log thô chứa dữ liệu
        String rawLogMessage = String.format(
                "Transaction Error: %s | Context: [TransactionID=%s, Account=%s, InDebt=%s, Have=%s, Time=%s]",
                ex.getMessage(),
                ex.getTransactionId(),
                ex.getAccount(),
                ex.getInDebt(),
                ex.getHave(),
                ex.getTime()
        );

        // Thực hiện Masking (Che dấu bằng ?)
        String maskedLogMessage = maskSensitiveData(rawLogMessage);

        // Ghi log đã che (An toàn)
        log.error(maskedLogMessage, ex.getCause());

        // Trả về response cho Client (Ẩn chi tiết lỗi nội bộ)
        return new ResponseEntity<>(
                ApiResponse.error("Transaction failed due to system error. Please contact support."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // Hàm che dữ liệu
    private String maskSensitiveData(String input) {
        if (input == null) return null;
        Matcher matcher = SENSITIVE_DATA_PATTERN.matcher(input);
        return matcher.replaceAll("$1=?");
    }
}

