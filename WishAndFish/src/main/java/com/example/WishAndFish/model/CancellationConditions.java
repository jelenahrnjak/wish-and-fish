package com.example.WishAndFish.model;

import javax.persistence.*;

@Entity
@Table(name = "cancellation_conditions")
public class CancellationConditions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description", unique = false, nullable = false)
    private String description;

    @Column(name = "price", unique = false, nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "boat_id", nullable = false)
    private Boat boat;
}
