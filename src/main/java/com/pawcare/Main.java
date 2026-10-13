package com.pawcare;
import com.formdev.flatlaf.FlatLightLaf;
import com.pawcare.view.LoginFrame;
import javax.swing.*;
import java.util.TimeZone;
public class Main {
    public static void main(String[] args) {
        configureTheme();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
    public static void configureTheme() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Colombo"));
        FlatLightLaf.setup();
        UIManager.put("Button.arc",12); UIManager.put("Component.arc",10);
        UIManager.put("TextComponent.arc",10); UIManager.put("Component.focusWidth",1);
        UIManager.put("defaultFont",new java.awt.Font("SansSerif",java.awt.Font.PLAIN,13));
    }
}
