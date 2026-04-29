package com.ekwe_hub.zeepark.model.vehicle;

import java.math.BigDecimal;

public enum VehicleType {
    BICYCLE {
        @Override
        public BigDecimal getBasePrice() {
            return BigDecimal.valueOf(850.00);
        }
    },
    SUV {
        @Override
        public BigDecimal getBasePrice() {
            return BigDecimal.valueOf(1000.00);
        }
    },
    CAR {
        @Override
        public BigDecimal getBasePrice() {
            return BigDecimal.valueOf(1200.00);
        }
    },
    EV {
        @Override
        public BigDecimal getBasePrice() {
            return BigDecimal.valueOf(1500.00);
        }
    };


    public abstract BigDecimal getBasePrice();
}
