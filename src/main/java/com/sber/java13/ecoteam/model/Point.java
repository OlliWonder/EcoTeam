package com.sber.java13.ecoteam.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "points")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "points_seq", allocationSize = 1)
public class Point extends GenericModel {
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "время работы")
    private String workingTime;
    
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "wastes_points",
            joinColumns = @JoinColumn(name = "point_id"), foreignKey = @ForeignKey(name = "FK_POINTS_WASTES"),
    inverseJoinColumns = @JoinColumn(name = "waste_id"), inverseForeignKey = @ForeignKey(name = "FK_WASTES_POINTS"))
    private Set<Waste> wastes;
}
