package com.sber.java13.ecoteam.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "wastes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "waste_seq", allocationSize = 1)
public class Waste extends GenericModel {
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "short_title", nullable = false)
    private String shortTitle;
    
    @Column(name = "code", nullable = false)
    private String code;
    
    @Column(name = "description", nullable = false, length = 1000)
    private String description;
    
    @OneToMany(mappedBy = "waste", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Order> orders;
    
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "wastes_points",
    joinColumns = @JoinColumn(name = "waste_id"), foreignKey = @ForeignKey(name = "FK_WASTES_POINTS"),
    inverseJoinColumns = @JoinColumn(name = "point_id"), inverseForeignKey = @ForeignKey(name = "FK_POINTS_WASTES"))
    private Set<Point> points;
}
