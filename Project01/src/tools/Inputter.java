/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

import java.util.Scanner;
import java.util.function.Consumer;
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

    private void inputField(String prompt, String pattern, boolean isUpdate, Consumer<String> setter) {
        if (!isUpdate) {
            setter.accept(inputAndLoop(prompt, pattern));
            return;
        }
        String value = getString(prompt);
        if (value.trim().isEmpty()) {
            return; // giu gia tri cu
        }
        if (Acceptable.isValid(value, pattern)) {
            setter.accept(value);
        } else {
            System.out.println("Invalid format ! Keeping old value.");
        }
    }

    public void inputCustomerInfo(Customer c, boolean isUpdate) {
        inputField(isUpdate ? "New name [Enter to keep old]: " : "Customer name [2-25 chars]: ",
                Acceptable.NAME_VALID, isUpdate, c::setName);

        inputField(isUpdate ? "New phone [Enter to keep old]: " : "Phone number [10 digits, VN operator]: ",
                Acceptable.VN_TELCO_VALID, isUpdate, c::setPhone);

        inputField(isUpdate ? "New email [Enter to keep old]: " : "Email address: ",
                Acceptable.EMAIL_VALID, isUpdate, c::setEmail);
    }

}
