/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

/**
 *
 * @author Le Nhat Tung
 */
public interface Acceptable {

    String CUS_ID_VALID = "^[CcGKk]\\d{4}$";
    String NAME_VALID = "^.{2,25}$";
    // Dau so nha mang VN pho bien (Viettel, Vina, Mobi, Vietnamobile, Gmobile)
    String VN_TELCO_VALID = "^(03[2-9]|05[6|8|9]|07[0|6-9]|08[1-9]|09[0-9])\\d{7}$";
    String INTEGER_VALID = "^-?\\d+$";
    String POSITIVE_INT_VALID = "^[1-9]\\d*$";
    String DOUBLE_VALID = "^-?\\d+(\\.\\d+)?$";
    String POSITIVE_DOUBLE_VALID = "^[0-9]+(\\.[0-9]+)?$";
    String EMAIL_VALID = "^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$";
    String MENU_ID_VALID = "^[A-Za-z]{2}\\d{3}$"; // vd: PW003 - dieu chinh neu csv khac

    /**
     * Kiem tra du lieu co trong data co phu hop voi mau pattern theo yeu cau
     * khong
     *
     * @param data du lieu can kiem tra
     * @param pattern mau du lieu duoc xem nhu dieu kien bat buoc
     * @return true is valid, false is invalid
     */
    static boolean isValid(String data, String pattern) {
        return data != null && data.matches(pattern);
    }
}
