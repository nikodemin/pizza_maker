package com.t_systems.webstore.model.entity;

import com.t_systems.webstore.model.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractEntity {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private UserRole role;
}
