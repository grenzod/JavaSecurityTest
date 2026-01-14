package com.example.banking.service;

import com.example.banking.dto.IncomingTransactionDTO;
import com.example.banking.exception.TransactionException;

public interface ITransactionService {

    /**
     * Xử lý giao dịch chuyển khoản ngân hàng từ dữ liệu đầu vào đã được mã hóa.
     * <p>
     * Phương thức này thực hiện quy trình:
     * 1. Giải mã RSA các thông tin giao dịch (Account, Amount, Time...).
     * 2. Tạo 2 bản ghi lịch sử: Ghi nợ (Debit) người gửi và Ghi có (Credit) người nhận.
     * 3. Lưu xuống cơ sở dữ liệu (tự động mã hóa AES trường Account).
     *
     * @param dto Đối tượng {@link IncomingTransactionDTO} chứa các chuỗi String đã được mã hóa RSA
     * (bao gồm transactionId, fromAccount, toAccount, amount...).
     * @throws TransactionException Ném ra nếu xảy ra lỗi trong quá trình:
     * - Giải mã RSA (sai key, sai định dạng).
     * - Parse số tiền (BigDecimal).
     * - Lỗi kết nối Database.
     * Exception này chứa ngữ cảnh lỗi để phục vụ Logging an toàn.
     */
    void processTransaction(IncomingTransactionDTO dto);
}