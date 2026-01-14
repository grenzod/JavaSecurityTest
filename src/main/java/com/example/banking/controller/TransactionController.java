package com.example.banking.controller;

import com.example.banking.dto.IncomingTransactionDTO;
import com.example.banking.response.ApiResponse;
import com.example.banking.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransactionService transactionService;

    /**
     * API tiếp nhận yêu cầu chuyển khoản.
     * <p>
     * <b>Lưu ý quan trọng:</b> Client (Frontend/Third-party) BẮT BUỘC phải mã hóa RSA
     * các trường dữ liệu trước khi gửi vào body.
     * Ví dụ: Không gửi "100000", phải gửi "Base64_String_RSA_Encrypted..."
     * * @param request DTO chứa các chuỗi đã mã hóa RSA.
     * @return Thông báo thành công nếu không có lỗi (Lỗi sẽ do GlobalExceptionHandler bắt).
     */
    @PostMapping("/process")
    public ResponseEntity<ApiResponse<String>> processTransaction(@RequestBody IncomingTransactionDTO request) {
        transactionService.processTransaction(request);

        return ResponseEntity.ok(
                ApiResponse.success(null, "The transaction has been received and successfully processed.")
        );
    }
}
