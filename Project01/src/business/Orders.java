/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import model.Order;

/**
 *
 * @author Le Nhat Tung
 */
public class Orders extends HashSet<Order> implements Workable<Order> {

    /**
     * Kiem tra don hang co bi trung hay khong, dua vao equals() da dinh nghia
     * trong Order (customerId + menuId + eventDate). Nho co equals() nay, chi
     * can goi contains() la du - khong can tu viet logic so sanh thu cong.
     */
    public boolean isDuplicate(Order x) {
        return this.contains(x);
    }

    @Override
    public void addNew(Order x) {
        if (!this.isDuplicate(x)) {
            this.add(x);
        }
    }

    @Override
    public void update(Order x) {
        Order old = searchById(x.getOrderCode());
        if (old != null) {
            old.setMenuId(x.getMenuId());
            old.setNumOfTables(x.getNumOfTables());
            old.setEventDate(x.getEventDate());
        }
    }

    @Override
    public Order searchById(String orderCode) {
        for (Order o : this) {
            if (o.getOrderCode().equalsIgnoreCase(orderCode)) {
                return o;
            }
        }
        return null;
    }

    @Override
    public void showAll() {
        if (this.isEmpty()) {
            System.out.println("No data in the system.");
            return;
        }

        List<Order> sorted = new ArrayList<>(this);
        Collections.sort(sorted, Comparator.comparing(Order::getEventDate));
        for (Order o : sorted) {
            System.out.println(o);
        }
    }

}
