
package com.POSGlobal.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

public class PanelChart extends javax.swing.JPanel {

    List<Slice> arrList=null;
    
    public PanelChart(List<Slice> tempArrList) {
        initComponents();
        arrList=tempArrList;
    }
    
    public void paint(Graphics g) {
        drawPie((Graphics2D) g, getBounds(), arrList);
    }
    
    void drawPie(Graphics2D g, Rectangle area, List<Slice> slices) {
        double total = 0.0D;
        for (int i = 0; i < slices.size(); i++) {
            Slice obj=slices.get(i);
            total += obj.getValue();
        }
        double curValue = 0.0D;
        int startAngle = 0;
        for (int i = 0; i < slices.size(); i++) {
            Slice obj=slices.get(i);
            startAngle = (int) (curValue * 360 / total);
            int arcAngle = (int) (obj.getValue() * 360 / total);
            g.setColor(obj.getColor());
            g.fillArc(area.x, area.y, area.width, area.height, 
            startAngle, arcAngle);
            curValue += obj.getValue();
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 786, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 743, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
class Slice {
   double value;
   Color color;
   public Slice(double value, Color color) {  
      this.value = value;
      this.color = color;
   }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}