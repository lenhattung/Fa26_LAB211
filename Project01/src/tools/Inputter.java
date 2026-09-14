/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

import java.util.Scanner;
import model.Customer;

/**
 *
 * @author Le Nhat Tung
 */
public class Inputter {

    private Scanner ndl;

    public Inputter() {
        this.ndl = new Scanner(System.in);
    }

    /**
     * Nhap du lieu chuoi truc tiep tu ban phim boi nguoi dung
     */
    public String getString(String mess) {
        System.out.print(mess);
        return ndl.nextLine();
    }

    /**
     * Nhap du lieu la so nguyen tu ban phim boi nguoi dung
     */
    public int getInt(String mess) {
        int result = 0;
        String temp = getString(mess);
        if (Acceptable.isValid(temp, Acceptable.INTEGER_VALID)) {
            result = Integer.parseInt(temp);
        }
        return result;
    }

    /**
     * Nhap du lieu la so double tu ban phim boi nguoi dung
     */
    public double getDouble(String mess) {
        double result = 0;
        String temp = getString(mess);
        if (Acceptable.isValid(temp, Acceptable.DOUBLE_VALID)) {
            result = Double.parseDouble(temp);
        }
        return result;
    }

    /**
     * Phuong thuc cho phep nhap va kiem tra du lieu, nhap lai neu "khong khop"
     *
     * @param mess Thong bao huong dan nhap du lieu
     * @param pattern Dieu kien kiem tra du lieu
     * @param isLoop Yeu cau lap de nhap du lieu cho toi khi dung
     * @return String da duoc kiem tra hop le
     */
    public String inputAndLoop(String mess, String pattern, boolean isLoop) {
        String result = "";
        boolean more = true;
        do {
            result = getString(mess);
            more = !Acceptable.isValid(result, pattern);
            if (more && (isLoop && result.length() > 0)) {
                System.out.println("Data is invalid !. Re-enter ...");
            }
        } while (isLoop && more);
        return result.trim();
    }

    /**
     * Cho phep nguoi dung nhap va kiem tra du lieu, bat buoc lap den khi hop le
     */
    public String inputAndLoop(String mess, String pattern) {
        return inputAndLoop(mess, pattern, true);
    }

    public void inputCustomerInfo(Customer c, boolean isUpdate) {
        // ------ Name ------
        String name = isUpdate
                ? getString("New name [Enter to keep old]: ")
                : inputAndLoop("Customer name [2-25 chars]: ", Acceptable.NAME_VALID);
        if (!isUpdate) {
            c.setName(name);
        } else if (!name.trim().isEmpty()) {
            if (Acceptable.isValid(name, Acceptable.NAME_VALID)) {
                c.setName(name);
            } else {
                System.out.println("Invalid name format ! Keeping old value.");
            }
        }

        // ------ Phone ------
        String phone = isUpdate
                ? getString("New phone [Enter to keep old]: ")
                : inputAndLoop("Phone number [10 digits, VN operator]: ", Acceptable.VN_TELCO_VALID);
        if (!isUpdate) {
            c.setPhone(phone);
        } else if (!phone.trim().isEmpty()) {
            if (Acceptable.isValid(phone, Acceptable.VN_TELCO_VALID)) {
                c.setPhone(phone);
            } else {
                System.out.println("Invalid phone format ! Keeping old value.");
            }
        }

        // ------ Email ------
        String email = isUpdate
                ? getString("New email [Enter to keep old]: ")
                : inputAndLoop("Email address: ", Acceptable.EMAIL_VALID);
        if (!isUpdate) {
            c.setEmail(email);
        } else if (!email.trim().isEmpty()) {
            if (Acceptable.isValid(email, Acceptable.EMAIL_VALID)) {
                c.setEmail(email);
            } else {
                System.out.println("Invalid email format ! Keeping old value.");
            }
        }
    }

}
