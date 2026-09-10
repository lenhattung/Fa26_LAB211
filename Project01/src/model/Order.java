/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author Le Nhat Tung
 */
public class Order {
    private String orderCode, customerId, province, menuId;
    private int numOfTables;
    private Date eventDate;

    public Order() {
    }

    public Order(String orderCode, String customerId, String province, String menuId, int numOfTables, Date eventDate) {
        this.orderCode = orderCode;
        this.customerId = customerId;
        this.province = province;
        this.menuId = menuId;
        this.numOfTables = numOfTables;
        this.eventDate = eventDate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public int getNumOfTables() {
        return numOfTables;
    }

    public void setNumOfTables(int numOfTables) {
        this.numOfTables = numOfTables;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public String toString() {
        return "Order{" + "orderCode=" + orderCode + ", customerId=" + customerId + ", province=" + province + ", menuId=" + menuId + ", numOfTables=" + numOfTables + ", eventDate=" + eventDate + '}';
    }
    
    
 }
