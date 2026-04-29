package com.ekwe_hub.zeepark.model.user;

import com.ekwe_hub.zeepark.model.vehicle.Vehicle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "users")
@EqualsAndHashCode(callSuper = true)
public class Customer extends User {

    @DBRef
    private List<Vehicle> vehicles = new ArrayList<>();
    public Customer(String username, String password, String email) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setRole(UserRole.CUSTOMER);
    }

}
