package com.ekwe_hub.zeepark.model.payment;

public record TransactionResult(String transactionId, boolean success, String message) {
}
