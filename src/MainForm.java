import pojo.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
/*
 * Created by JFormDesigner on Sun Nov 09 21:32:23 EET 2014
 */



/**
 * @author Ash Coopeer
 */
public class MainForm extends JFrame {
    private ConnectHibernate ch;
    private DefaultTableModel students_tm,groups_tm;
    public MainForm() {
        ch = new ConnectHibernate();
        initComponents
                ();

        students_tm = new DefaultTableModel(){

            @Override
            public int getRowCount() {
                return 0;
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public String getColumnName(int columnIndex) {
                return null;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return null;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return null;
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

            }
        };

    }



//    java.util.List res = ch.loadTable(textField1.getText());
//    String source = "";
//    for(pojo.Group a:(List<pojo.Group>)res){
//        source +="Id: " +a.getGroupId();
//        source +="   Name: " + a.getGroupName()+"\n";
//    }
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Ash Hisenberg
        scrollPane2 = new JScrollPane();
        table1 = new JTable();
        comboBox1 = new JComboBox();
        panel1 = new JPanel();
        comboBox2 = new JComboBox();
        comboBox3 = new JComboBox();
        checkBox1 = new JCheckBox();
        checkBox2 = new JCheckBox();

        //======== this ========
        Container contentPane = getContentPane();

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(table1);
        }

        //======== panel1 ========
        {

            // JFormDesigner evaluation mark
            panel1.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), panel1.getBorder())); panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});


            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGap(0, 444, Short.MAX_VALUE)
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGap(0, 513, Short.MAX_VALUE)
            );
        }

        //---- checkBox1 ----
        checkBox1.setText("\u0421\u043f\u0435\u0446\u0433\u0440\u0443\u043f\u0430");
        checkBox1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        checkBox1.setHorizontalAlignment(SwingConstants.CENTER);

        //---- checkBox2 ----
        checkBox2.setText("\u0413\u0440\u0430\u0444\u0456\u043a");
        checkBox2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        checkBox2.setHorizontalAlignment(SwingConstants.CENTER);

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 594, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(checkBox1, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(checkBox2, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(4, Short.MAX_VALUE))
        );
        contentPaneLayout.linkSize(SwingConstants.HORIZONTAL, new Component[]{checkBox1, comboBox1, comboBox2, comboBox3});
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addGroup(GroupLayout.Alignment.LEADING, contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(checkBox1)
                                    .addComponent(checkBox2, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
                            .addComponent(comboBox1, GroupLayout.Alignment.LEADING)
                            .addComponent(comboBox2)
                            .addComponent(comboBox3))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup()
                            .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                            .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Ash Hisenberg
    private JScrollPane scrollPane2;
    private JTable table1;
    private JComboBox comboBox1;
    private JPanel panel1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
