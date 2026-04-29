package com.ekwe_hub.zeepark.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Document(collection = "users")
public class Admin extends User {

    public Admin(String username, String password, String email) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setRole(UserRole.ADMIN);
    }
}
