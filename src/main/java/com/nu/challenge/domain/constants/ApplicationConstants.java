package com.nu.challenge.domain.constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ApplicationConstants {
    public static final String BUY_OPERATION = "buy";
    public static final String SELL_OPERATION = "sell";
    public static final double TAX_FREE_CEILING = 20000.00;
    public static final double TAX_FEE = 0.2;
    public static final BigDecimal BD_ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.CEILING);
}
