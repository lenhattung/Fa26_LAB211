/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import model.Customer;

/**
 *
 * @author Le Nhat Tung
 */
public class Customers extends ArrayList<Customer> implements Workable<Customer> {

    private static final long serialVersionUID = 1L;
    private static final String TABLE_HEADER
            = "------------------------------------------------------------------\n"
            + String.format("%-6s| %-25s| %-12s| %s", "Code", "Customer Name", "Phone", "Email")
            + "\n"
            + "------------------------------------------------------------------";
    private static final String TABLE_FOOTER
            = "------------------------------------------------------------------";

    // ================== Function 1: Register customers ==================
    /**
     * Them moi 1 khach hang vao danh sach. Luu y: viec kiem tra "id da ton tai
     * hay chua" (validate uniqueness) duoc thuc hien TRUOC khi goi addNew() -
     * thuong o dispatcher layer, bang cach goi isExist(id) ben duoi. addNew()
     * chi lam dung 1 viec: them.
     */
    @Override
    public void addNew(Customer x) {
        if (!this.isDuplicated(x)) {
            this.add(x);
        }
    }

    @Override
    public void update(Customer x) {
        Customer old = searchById(x.getId());
        if (old != null) {
            old.setName(x.getName());
            old.setPhone(x.getPhone());
            old.setEmail(x.getEmail());
        }
    }

    @Override
    public Customer searchById(String x) {
        for (Customer c : this) {
            if (c.getId().equalsIgnoreCase(x)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Overload: hien thi mot danh sach con (vd: ket qua sau khi filterByName)
     * Dung chung cho ca Function 3 va Function 8 -> tranh trung lap ma nguon
     */
    public void showAll(List<Customer> list) {
        if (list.isEmpty()) {
            System.out.println("Does not have any customer information.");
            return;
        }
        List<Customer> sorted = new ArrayList<>(list);
        Collections.sort(sorted, Comparator.comparing(Customer::getName));
        System.out.println(TABLE_HEADER);
        for (Customer c : sorted) {
            System.out.println(c);
        }
        System.out.println(TABLE_FOOTER);
    }

    @Override
    public void showAll() {
        showAll(this);
    }

    public boolean isExist(String id) {
        return searchById(id) != null;
    }

    public boolean isDuplicated(Customer x) {
        return this.contains(x);
    }
    
    public List<Customer> filterByName(String name){
        List<Customer> result = new ArrayList<>();
        
        for(Customer c : this){
            if(c.getName().toLowerCase().contains(name.toLowerCase())){
                result.add(c);    
            }
        }
        return result;
    }
}
