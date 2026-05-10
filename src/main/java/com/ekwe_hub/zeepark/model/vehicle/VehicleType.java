package com.ekwe_hub.zeepark.model.vehicle;

import java.math.BigDecimal;

public enum VehicleType {
    BICYCLE {
        @Override
        public BigDecimal getBasePrice() {
            return BigDecimal.valueOf(50.00);
        }
    },
    SUV {
        @Override
        public BigDecimal getBasePrice() {
            return BigDecimal.valueOf(150.00);
        }
    },
    CAR {
        @Override
        public BigDecimal getBasePrice() {
            return BigDecimal.valueOf(100.00);
        }
    },
    EV {
        @Override
        public BigDecimal getBasePrice() {
            return BigDecimal.valueOf(90.00);
        }
    };


    public abstract BigDecimal getBasePrice();
}
