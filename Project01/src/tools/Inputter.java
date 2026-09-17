package tools;

import business.Customers;
import business.Orders;
import business.SetMenus;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;
import model.Customer;
import model.Order;

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

    /**
     * Nhap va kiem tra Customer code dung dinh dang (KHONG kiem tra trung -
     * viec do thuoc ve business layer, xu ly o dispatcher)
     */
    public String inputCustomerId() {
        return inputAndLoop("Customer code [C/G/K + 4 digits]: ", Acceptable.CUS_ID_VALID).toUpperCase();
    }

    // ========================================================================
    // CORE HELPER - dung chung cho MOI truong du lieu dang String, bat ke
    // dieu kien hop le la gi (regex, ton tai trong danh sach, ...)
    // Ap dung nguyen tac low coupling - high cohesion: 1 ham chi lam dung 1 viec
    // (nhap 1 truong String, kiem tra theo dieu kien duoc truyen vao), va co the
    // tai su dung cho bat ky loai du lieu String nao, khong rieng gi Customer/Order
    // ========================================================================
    /**
     * Nhap 1 truong du lieu String, dung chung cho ca "nhap moi" (bat buoc
     * dung) va "cap nhat" (cho phep bo trong de giu gia tri cu).
     *
     * @param prompt thong bao hien thi cho nguoi dung
     * @param validator dieu kien hop le - co the la regex (thong qua
     * Acceptable.isValid), hoac bat ky logic nao khac (vd: kiem tra ton tai
     * trong 1 danh sach)
     * @param isUpdate false: bat buoc nhap dung dinh dang, lap lai den khi hop
     * le true: cho phep bo trong -> giu gia tri cu; neu nhap sai -> canh bao,
     * giu gia tri cu
     * @param setter setter tuong ung cua doi tuong can gan gia tri (vd:
     * c::setName)
     */
    private void inputStringField(String prompt, Predicate<String> validator, boolean isUpdate, Consumer<String> setter) {
        if (!isUpdate) {
            String value;
            do {
                value = getString(prompt);
                if (!validator.test(value)) {
                    System.out.println("Data is invalid !. Re-enter ...");
                }
            } while (!validator.test(value));
            setter.accept(value);
            return;
        }
        String value = getString(prompt);
        if (value.trim().isEmpty()) {
            return; // giu gia tri cu
        }
        if (validator.test(value)) {
            setter.accept(value);
        } else {
            System.out.println("Invalid format ! Keeping old value.");
        }
    }

    /**
     * Bien the cua inputStringField() danh cho truong so nguyen duong (vd: so
     * ban). Tai su dung inputStringField() de kiem tra dinh dang, chi khac o
     * buoc chuyen doi String -> Integer truoc khi gan vao setter.
     */
    private void inputIntField(String prompt, boolean isUpdate, Consumer<Integer> setter) {
        Predicate<String> isPositiveInt = s -> Acceptable.isValid(s, Acceptable.POSITIVE_INT_VALID);
        inputStringField(prompt, isPositiveInt, isUpdate, s -> setter.accept(Integer.parseInt(s)));
    }

    /**
     * Bien the cua inputStringField() danh cho truong ngay thang - phai la ngay
     * hop le (dinh dang dd/MM/yyyy) VA phai o tuong lai.
     */
    private void inputDateField(String prompt, boolean isUpdate, Consumer<Date> setter) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false); // khong cho phep ngay khong hop le nhu 32/13/2025 tu "cuon" thanh ngay khac

        Predicate<String> isFutureDate = s -> {
            try {
                return df.parse(s).after(new Date());
            } catch (ParseException ex) {
                return false;
            }
        };

        inputStringField(prompt, isFutureDate, isUpdate, s -> {
            try {
                setter.accept(df.parse(s));
            } catch (ParseException ex) {
                // khong the xay ra vi validator da kiem tra parse thanh cong truoc do
            }
        });
    }

    // ========================================================================
    // Function 1, 2: Customer - nhap/cap nhat Name, Phone, Email
    // ========================================================================
    /**
     * Nhap (Function 1) hoac cap nhat (Function 2) thong tin Name/Phone/Email
     * cua khach hang. Khong xu ly Customer code (id) o day - viec kiem tra id
     * da ton tai/trung can den danh sach Customers (business layer), Inputter
     * (tools layer) khong nen phu thuoc nguoc len business cho phan nay - id
     * duoc xu ly rieng o dispatcher (Main).
     *
     * @param c doi tuong Customer can dien du lieu: - Customer moi (da co san
     * id) khi goi cho Function 1 (Register) - Customer cu tim duoc theo id khi
     * goi cho Function 2 (Update)
     * @param isUpdate false: bat buoc nhap dung dinh dang ca 3 truong (Function
     * 1) true: cho phep bo trong tung truong de giu gia tri cu (Function 2)
     */
    public void inputCustomerInfo(Customer c, boolean isUpdate) {
        inputStringField(isUpdate ? "New name [Enter to keep old]: " : "Customer name [2-25 chars]: ",
                s -> Acceptable.isValid(s, Acceptable.NAME_VALID), isUpdate, c::setName);

        inputStringField(isUpdate ? "New phone [Enter to keep old]: " : "Phone number [10 digits, VN operator]: ",
                s -> Acceptable.isValid(s, Acceptable.VN_TELCO_VALID), isUpdate, c::setPhone);

        inputStringField(isUpdate ? "New email [Enter to keep old]: " : "Email address: ",
                s -> Acceptable.isValid(s, Acceptable.EMAIL_VALID), isUpdate, c::setEmail);
    }

    /**
     * Nhap (Function 5) hoac cap nhat (Function 6) thong tin don hang. Function
     * 5 (isUpdate=false): nhap du CustomerId/MenuId/NumOfTables/EventDate, dong
     * thoi tu dong kiem tra trung (Customer+Menu+Date) va yeu cau nhap lai
     * Event Date neu phat hien trung, cho toi khi ra duoc 1 to hop hop le.
     * Function 6 (isUpdate=true): CHI cho phep sua NumOfTables va EventDate.
     *
     * @param o doi tuong Order can dien du lieu
     * @param customerList danh sach khach hang - xac thuc Customer Code (chi
     * dung khi isUpdate=false)
     * @param menuList danh sach thuc don - xac thuc Menu Code (chi dung khi
     * isUpdate=false)
     * @param orderList danh sach don hang hien co - dung de kiem tra trung (chi
     * dung khi isUpdate=false)
     * @param isUpdate false: nhap moi du 4 truong + tu dong kiem tra trung
     * (Function 5) true: chi cho phep sua NumOfTables/EventDate (Function 6)
     */
    public Order inputOrderInfo(Order o, Customers customerList, SetMenus menuList, Orders orderList, boolean isUpdate) {
        if (!isUpdate) {
            inputStringField("Customer code [C/G/K + 4 digits]: ",
                    s -> Acceptable.isValid(s.toUpperCase(), Acceptable.CUS_ID_VALID)
                    && customerList.searchById(s.toUpperCase()) != null,
                    false,
                    s -> o.setCustomerId(s.toUpperCase()));

            inputStringField("Code of Set Menu: ",
                    s -> menuList.searchById(s.toUpperCase()) != null, false,
                    s -> o.setMenuId(s.toUpperCase()));
        }

        inputIntField(isUpdate ? "New number of tables [Enter to keep old]: " : "Number of tables: ",
                isUpdate, o::setNumOfTables);

        inputDateField(isUpdate ? "New event date [dd/MM/yyyy] [Enter to keep old]: " : "Preferred event date [dd/MM/yyyy]: ",
                isUpdate, o::setEventDate);

        // Kiem tra trung: chi ap dung khi tao moi (Function 5), lap lai yeu cau nhap
        // Event Date khac neu to hop (Customer+Menu+Date) da ton tai
        if (!isUpdate) {
            while (orderList.isDuplicate(o)) {
                System.out.println("Dupplicate data ! Please enter a different event date.");
                inputDateField("Preferred event date [dd/MM/yyyy]: ", false, o::setEventDate);
            }
        }
        return o;
    }
}
