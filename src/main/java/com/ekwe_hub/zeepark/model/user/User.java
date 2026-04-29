package com.ekwe_hub.zeepark.model.user;

import com.ekwe_hub.zeepark.model.common.BaseDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "users")
@EqualsAndHashCode(callSuper = true)
public abstract class User extends BaseDocument {
    @Indexed(unique = true)
    private String username;
    private String password;
    private String email;
    private String phone;
    private UserRole role;
}
