package com.sber.java13.ecoteam.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(name = "uniqueEmail", columnNames = "email"),
        @UniqueConstraint(name = "uniqueLogin", columnNames = "login")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "person_seq", allocationSize = 1)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class User extends GenericModel {
    
    @Column(name = "is_company", nullable = false, columnDefinition = "boolean default false")
    private Boolean isCompany;
    
    @Column(name = "login", nullable = false, unique = true)
    private String login;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "middle_name")
    private String middleName;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "achivement")
    @Enumerated
    private Achievement achievement;
    
    @Column(name = "change_password_token")
    private String changePasswordToken;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PERSONS_ROLE"))
    private Role role;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "users_orders",
            joinColumns = @JoinColumn(name = "user_id"), foreignKey = @ForeignKey(name = "FK_USERS_ORDERS"),
            inverseJoinColumns = @JoinColumn(name = "order_id"), inverseForeignKey = @ForeignKey(name = "FK_ORDERS_USERS"))
    private Set<Order> orders;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "point_id")
    private Point point;
}
