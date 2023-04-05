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
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "order_seq", allocationSize = 1)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Order extends GenericModel {
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "users_orders",
            joinColumns = @JoinColumn(name = "order_id"), foreignKey = @ForeignKey(name = "FK_ORDERS_USERS"),
            inverseJoinColumns = @JoinColumn(name = "user_id"), inverseForeignKey = @ForeignKey(name = "FK_USERS_ORDERS"))
    private Set<User> users;
    
    @Column(name = "on_date", nullable = false)
    private LocalDate onDate;
    
    @Column(name = "is_in_work", columnDefinition = "boolean default false")
    private Boolean isInWork;
    
    @Column(name = "is_completed", columnDefinition = "boolean default false")
    private Boolean isCompleted;
    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "waste_id", foreignKey = @ForeignKey(name = "FK_WASTE"))
    private Waste waste;
    
    @Column(name = "weight", nullable = false)
    private Integer weight;
}
