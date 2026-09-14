package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class ho tro cac thao tac doc/ghi file dung chung cho nhieu business class
 * (Customers, Orders, SetMenus, ...) - tranh lap lai logic I/O o nhieu noi.
 */
public class FileUtils {

    private static final Pattern CSV_PATTERN = Pattern.compile("\"([^\"]*)\"|([^,]+)");

    // ================== Doc file TEXT (vd: feastMenu.csv - Function 4) ==================
    public static List<String> readTextLines(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            return null;
        }
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

    public static String[] splitCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        Matcher m = CSV_PATTERN.matcher(line);
        while (m.find()) {
            tokens.add(m.group(1) != null ? m.group(1) : m.group(2));
        }
        return tokens.toArray(new String[0]);
    }

    // ================== Doc/Ghi OBJECT FILE (customers.dat, feast_order_service.dat - Function 7) ==================
    /**
     * Doc du lieu tu object file (binary), tra ve dang List<T>.
     * Ap dung Generic Methods de dung chung cho ca Customer va Order,
     * tranh viet lap readFromFile() rieng cho tung loai du lieu.
     * @param filePath duong dan file can doc
     * @return danh sach doi tuong doc duoc; danh sach rong neu file khong ton tai
     */
    public static <T> List<T> readFromFile(String filePath) {
        List<T> result = new ArrayList<>();
        File f = new File(filePath);
        if (!f.exists()) {
            System.out.println("File not found !.\"" + filePath + "\"");
            return result;
        }
        try (FileInputStream fis = new FileInputStream(f);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (fis.available() > 0) {
                @SuppressWarnings("unchecked")
                T x = (T) ois.readObject();
                result.add(x);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Ghi danh sach doi tuong xuong object file (binary).
     * Ap dung Generic Methods, dung chung cho Customers va Orders.
     * @param li danh sach doi tuong can ghi
     * @param filePath duong dan file dich
     */
    public static <T> void saveToFile(List<T> li, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(new File(filePath));
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (T i : li) {
                oos.writeObject(i);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}