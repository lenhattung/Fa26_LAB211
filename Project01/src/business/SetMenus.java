/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import model.SetMenu;
import tools.FileUtils;

/**
 *
 * @author Le Nhat Tung
 */
public class SetMenus extends ArrayList<SetMenu>{
    private static final long serialVersionUID = 1L;
    private String pathFile;
    private boolean fileFound; // ghi nhan lan doc gan nhat co tim thay khong
    
    public SetMenus(){
        super();
        this.pathFile="feastMenu.csv";
        this.fileFound = false;
    }
    
     /**
     * Tach 1 dong du lieu CSV thanh doi tuong SetMenu.
     * Tra ve null neu dong khong hop le (vd: dong tieu de khong parse duoc gia).
     */
    public SetMenu dataToObject(String line) {
        String[] p = FileUtils.splitCsvLine(line);
        if (p.length < 4) {
            return null;
        }
        try {
            String menuId = p[0].trim();
            String menuName = p[1].trim();
            double price = Double.parseDouble(p[2].trim());
            String ingredients = p[3].trim();
            return new SetMenu(menuId, menuName, price, ingredients);
        } catch (NumberFormatException ex) {
            return null; // dong header khong parse duoc gia -> tu dong bo qua
        }
    }
    
    // ================== Function 4: Display feast menus ==================
    /**
     * Doc du lieu tu feastMenu.csv, chuyen tung dong thanh SetMenu object.
     * Su dung lai FileUtils.readTextLines() (tools layer) thay vi tu viet
     * lai logic doc file + kiem tra ton tai - tranh trung lap ma nguon.
     */
    
    public void readFromFile(){
        this.clear();
        List<String> lines = FileUtils.readTextLines(pathFile);
        this.fileFound = (lines!=null);
        
        if(!fileFound){
            System.out.println("Cannot read data from feastMenu.csv. Please check it.");
            return;
        }
        
        for (String line : lines) {
            SetMenu m = dataToObject(line);
            if(m!=null){
                this.add(m);
            }
        }
    }
    
    /** Hien thi danh sach thuc don, sap xep tang dan theo gia */
    public void showMenuList() {
        if (!fileFound || this.isEmpty()) {
            return; // thong bao loi da duoc in trong readFromFile(), khong in lai
        }
        List<SetMenu> sorted = new ArrayList<>(this);
        Collections.sort(sorted, Comparator.comparingDouble(SetMenu::getPrice));

        System.out.println("---------------------------------------------------");
        System.out.println(" List of Set Menus for ordering party:");
        for (SetMenu m : sorted) {
            System.out.println("---------------------------------------------------");
            System.out.println("Code   :" + m.getMenuId());
            System.out.println("Name   :" + m.getMenuName());
            System.out.println("Price  : " + String.format("%,.0f", m.getPrice()) + " Vnd");
            System.out.println("Ingredients:");
            System.out.println(m.getIngredients());
        }
        System.out.println("---------------------------------------------------");
    }
}
