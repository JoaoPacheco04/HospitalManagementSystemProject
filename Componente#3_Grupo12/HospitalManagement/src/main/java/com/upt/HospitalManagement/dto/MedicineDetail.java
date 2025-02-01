package com.upt.HospitalManagement.dto;
/*
 * this is the details of medicines 
 */
public class MedicineDetail {

    private String type;
    private int quantity;
    private double pricePerUnit;
    private double cost;

    public MedicineDetail(String type, int quantity, double pricePerUnit, double cost) {
        this.type = type;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.cost = cost;
    }

    // Getters and Setters

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

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}

