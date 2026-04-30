package com.ekwe_hub.zeepark.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RevenueReportResponse(
        LocalDate from,
        LocalDate to,
        BigDecimal totalRevenue,
        int totalTransactions
) {}
