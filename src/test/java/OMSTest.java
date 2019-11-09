import org.ini4j.Ini;
import org.junit.Test;

import java.io.*;

public class OMSTest {
    @Test
    public void getIniOption() {
        Ini ini = new Ini();
        ini.put("paths", "orders", "src/main/resources/orders.json");
        System.out.println("getIniOption: " + ini.get("paths", "orders"));
    }

    @Test
    public void fileIsJson() throws Exception {
        File ordersFile = new File("src/main/resources/orders.json");
        Main.createFile(ordersFile);

        try (FileReader reader = new FileReader(ordersFile);
             BufferedReader br = new BufferedReader(reader)) {
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }

            System.out.println("fileIsJson: " + text.toString().matches("^\\s*\\[.*\\]\\s*$"));
        }
    }
}
