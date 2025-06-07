package GUI.Component;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SelectForm extends JPanel {
    private JLabel lblTitle;
    public JComboBox<String> cbb;
    
    public SelectForm(String title, String[] obj) {
        this.setLayout(new GridLayout(2, 1));
        this.setBackground(Color.white);
        this.setBorder(new EmptyBorder(0, 10, 5, 10));
        
        lblTitle = new JLabel(title);
        cbb = new JComboBox<>(obj);
        
        this.add(lblTitle);
        this.add(cbb);
    }
    
    public void setArr(String[] obj) {
        this.cbb.setModel(new DefaultComboBoxModel<>(obj));
    }
    
    public String getValue() {
        return (String) cbb.getSelectedItem();
    }
    
    public Object getSelectedItem() {
        return cbb.getSelectedItem();
    }
    
    public int getSelectedIndex() {
        return cbb.getSelectedIndex();
    }
    
    public void setSelectedIndex(int index) {
        if (index >= -1 && index < cbb.getItemCount()) {
            cbb.setSelectedIndex(index);
        } else {
            // Handle invalid index - you could:
            // 1. Set to -1 (no selection)
            // 2. Set to 0 (first item)
            // 3. Throw a custom exception
            // Here we'll set to -1 and log a warning
            cbb.setSelectedIndex(-1);
            System.err.println("Warning: Attempted to set invalid index " + index + 
                             " in SelectForm (max: " + (cbb.getItemCount()-1) + ")");
        }
    }
    
    public void setSelectedItem(Object item) {
        cbb.setSelectedItem(item);
    }

    public JLabel getLblTitle() {
        return lblTitle;
    }

    public void setLblTitle(JLabel lblTitle) {
        this.lblTitle = lblTitle;
    }

    public JComboBox<String> getCbb() {
        return cbb;
    }

    public void setCbb(JComboBox<String> cbb) {
        this.cbb = cbb;
    }
    
    public void setEnabled(boolean enable) {
        cbb.setEnabled(enable);
    }
    
    public void setDisable() {
        cbb.setEnabled(false);
    }
    
    public int getItemCount() {
        return cbb.getItemCount();
    }
}