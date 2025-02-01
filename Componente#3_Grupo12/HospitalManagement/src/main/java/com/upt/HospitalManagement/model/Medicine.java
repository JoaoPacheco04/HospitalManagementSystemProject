package com.upt.HospitalManagement.model;

import jakarta.persistence.*;

/*
 * This is the entity class for the medicines 
 */

@Entity
@Table(name = "medicines")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type; // e.g., capsule, tablet, liquid

    @Column(name = "quantity", nullable = false)
    private int quantity; // Available quantity in stock

    @Column(name = "price", nullable = false)
    private double price; // Price of the medicine

    // Default Constructors
    public Medicine() {}

    public Medicine(String type, int quantity, double price) {
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }
    
    // Getters, and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

	@Override
	public String toString() {
		return "Medicine [id=" + id + ", type=" + type + ", quantity=" + quantity + ", price=" + price + "]\n";
	}
}
