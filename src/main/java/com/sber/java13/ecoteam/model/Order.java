package com.sber.java13.ecoteam.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "order_seq", allocationSize = 1)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Order extends GenericModel {
    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_ORDER_USER"))
    private User user;
    
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
    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "point_id", foreignKey = @ForeignKey(name = "FK_POINT"))
    private Point point;
}
