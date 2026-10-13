package com.pawcare.util;
import javax.swing.Icon;
import java.awt.*;
/** Small vector icons drawn by the app; no external icon download required. */
public class NavIcon implements Icon {
    private final int type;
    public NavIcon(int type) { this.type=type; }
    public int getIconWidth() { return 22; }
    public int getIconHeight() { return 22; }
    public void paintIcon(Component c,Graphics g,int x,int y) {
        Graphics2D p=(Graphics2D)g.create(); p.translate(x,y); p.setColor(c.getForeground());
        p.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); p.setStroke(new BasicStroke(1.7f));
        switch(type) {
            case 0: for(int i=0;i<2;i++) for(int j=0;j<2;j++) p.drawRoundRect(3+i*10,3+j*10,6,6,2,2); break;
            case 1: p.drawOval(8,2,7,7); p.drawArc(3,11,17,15,0,180); break;
            case 2: case 7: p.fillOval(6,10,11,9); p.fillOval(2,6,5,6); p.fillOval(7,1,5,7); p.fillOval(13,2,5,7); p.fillOval(17,7,4,6); break;
            case 3: p.drawOval(2,13,6,6); p.drawOval(14,13,6,6); p.drawLine(6,14,17,3); p.drawLine(16,14,5,3); break;
            case 4: p.drawRoundRect(3,5,17,15,3,3); p.drawLine(3,10,20,10); p.drawLine(7,2,7,7); p.drawLine(16,2,16,7); p.fillRect(7,13,3,3); break;
            case 5: p.drawRoundRect(2,5,19,13,3,3); p.drawLine(2,10,21,10); p.drawLine(14,15,18,15); break;
            case 6: p.drawLine(3,20,21,20); p.fillRect(5,12,3,7); p.fillRect(11,7,3,12); p.fillRect(17,2,3,17); break;
            default: p.drawLine(4,3,4,20); p.drawLine(4,3,12,3); p.drawLine(4,20,12,20); p.drawLine(10,11,21,11); p.drawLine(17,7,21,11); p.drawLine(17,15,21,11);
        }
        p.dispose();
    }
}
