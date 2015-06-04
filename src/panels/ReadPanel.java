/*
 * Created by JFormDesigner on Wed Jun 03 17:56:16 EEST 2015
 */

package panels;

import pojo.Group;
import pojo.SportNormName;
import tableModels.ConnectHibernate;
import tableModels.GroupModel;
import tableModels.StudentModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author Urka
 */
public class ReadPanel extends JPanel {

    public static GraphMaker g;

    public ReadPanel() {
        initComponents();
        g = new GraphMaker(panel1,label1,label2);
        table1.setModel(new GroupModel(false));
        comboBox1.addItem("Усі групи");
        for(Object g: ConnectHibernate.loadTable("from Group")){
            String st = ((Group)g).getGroupName();
            comboBox1.addItem(st);
        }
        for(Object g: ConnectHibernate.loadTable("from SportNormName")){
            String st = ((SportNormName)g).getSportNormName();
            comboBox3.addItem(st);
        }

    }

    public static void updateModel(){
        if(comboBox1.getSelectedIndex()==0)
            table1.setModel(new GroupModel(false));
        if(comboBox1.getSelectedIndex()>0)
        table1.setModel(new StudentModel(
                GroupModel.groupList.get((comboBox1.getSelectedIndex()-1)).getGroupId(),
                comboBox2.getSelectedIndex(),
                checkBox1.isSelected()?1:0));
    }

    private void comboBox1ItemStateChanged(ItemEvent e) {
        updateModel();
    }

    private void comboBox3ItemStateChanged(ItemEvent e) {
        g.resetStats();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panel1 = new JPanel();
        label3 = new JLabel();
        comboBox1 = new JComboBox();
        comboBox2 = new JComboBox<>();
        checkBox1 = new JCheckBox();
        label1 = new JLabel();
        label2 = new JLabel();
        comboBox3 = new JComboBox();

        //======== this ========

        //======== scrollPane1 ========
        {

            //---- table1 ----
            table1.setRowHeight(20);
            scrollPane1.setViewportView(table1);
        }

        //======== panel1 ========
        {
            panel1.setLayout(new BorderLayout());

            //---- label3 ----
            label3.setText("\u041d\u0430\u0442\u0438\u0441\u043d\u0456\u0442\u044c \u043d\u0430 \u0433\u0440\u0443\u043f\u0443/\u0441\u0442\u0443\u0434\u0435\u043d\u0442\u0430");
            label3.setFont(new Font("Segoe UI", Font.BOLD, 16));
            label3.setHorizontalAlignment(SwingConstants.CENTER);
            panel1.add(label3, BorderLayout.CENTER);
        }

        //---- comboBox1 ----
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                comboBox1ItemStateChanged(e);
            }
        });

        //---- comboBox2 ----
        comboBox2.setModel(new DefaultComboBoxModel<>(new String[] {
            "\u0425\u043b\u043e\u043f\u0446\u0456",
            "\u0414\u0456\u0432\u0447\u0430\u0442\u0430"
        }));
        comboBox2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                comboBox1ItemStateChanged(e);
                comboBox3ItemStateChanged(e);
            }
        });

        //---- checkBox1 ----
        checkBox1.setText("\u0421\u043f\u0435\u0446\u0433\u0440\u0443\u043f\u0430");
        checkBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                comboBox1ItemStateChanged(e);
                comboBox3ItemStateChanged(e);
            }
        });

        //---- comboBox3 ----
        comboBox3.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                comboBox3ItemStateChanged(e);
            }
        });

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 345, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(label2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                                .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(label1, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE))
                            .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(checkBox1, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 151, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(label2, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(label1, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(checkBox1)
                                .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(2, 2, 2)))
                    .addContainerGap())
        );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private static JTable table1;
    private static JPanel panel1;
    private JLabel label3;
    private static JComboBox comboBox1;
    public static JComboBox<String> comboBox2;
    public static JCheckBox checkBox1;
    public static JLabel label1;
    public static JLabel label2;
    public static JComboBox comboBox3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
