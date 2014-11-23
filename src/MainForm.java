import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.table.DefaultTableModel;

/*
 * Created by JFormDesigner on Sun Nov 09 21:32:23 EET 2014
 */



/**
 * @author Ash Coopeer
 */
public class MainForm extends JFrame {

    ConnectHibernate ch;
    ModelLoader tml;
    boolean listenerIsStopped;

    public MainForm() {
        ch = new ConnectHibernate();
        tml = new ModelLoader(this,ch);
        listenerIsStopped = true;
        initComponents();
        tml.getGroupModel(false);
        tml.getGroupModel(true);
        tml.setGroupCombo();
        tml.setSNCombo();
        //updateUI();
    }

    private void applyReadFormFilter(ItemEvent e) {
        if(!listenerIsStopped){
            if(comboBox1.getSelectedIndex() == 0)tml.getGroupModel(false);
            else tml.getStudentModel(false);
            table1.updateUI();
        }
    }
    private void applyWriteFormFilter(ItemEvent e) {
        switch(comboBox4.getSelectedIndex()){
            case 0:
                tml.getGroupModel(true);
                break;
            case 1:
                tml.getStudentModel(true);
                break;
        }
        table2.updateUI();
    }

    private void updateUI(){
        table1.updateUI();
        table2.updateUI();
        comboBox1.updateUI();
        comboBox3.updateUI();
    }

//    Групи
//            Студенти
//    Результати
//            Нормативи
//

    //       U CAN'T TOUCH DIS
    //           \/\/\/

    private void initComponents() {

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Ash Hisenberg
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panel3 = new JPanel();
        comboBox1 = new JComboBox();
        comboBox2 = new JComboBox<>();
        checkBox1 = new JCheckBox();
        comboBox3 = new JComboBox();
        panel2 = new JPanel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        comboBox4 = new JComboBox<>();
        button1 = new JButton();

        //======== this ========
        Container contentPane = getContentPane();

        //======== tabbedPane1 ========
        {

            //======== panel1 ========
            {

                // JFormDesigner evaluation mark
                panel1.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                        "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                        java.awt.Color.red), panel1.getBorder())); panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});


                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(table1);
                }

                //======== panel3 ========
                {
                    panel3.setBackground(new Color(204, 204, 255));

                    GroupLayout panel3Layout = new GroupLayout(panel3);
                    panel3.setLayout(panel3Layout);
                    panel3Layout.setHorizontalGroup(
                        panel3Layout.createParallelGroup()
                            .addGap(0, 486, Short.MAX_VALUE)
                    );
                    panel3Layout.setVerticalGroup(
                        panel3Layout.createParallelGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                    );
                }

                //---- comboBox1 ----
                comboBox1.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyReadFormFilter(e);
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
                        applyReadFormFilter(e);
                    }
                });

                //---- checkBox1 ----
                checkBox1.setText("\u0421\u043f\u0435\u0446\u0433\u0440\u0443\u043f\u0430");
                checkBox1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                checkBox1.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyReadFormFilter(e);
                    }
                });

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(checkBox1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 313, Short.MAX_VALUE)))
                            .addContainerGap())
                );
                panel1Layout.setVerticalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(scrollPane1)
                                .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(comboBox1)
                                .addComponent(checkBox1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addGroup(panel1Layout.createParallelGroup()
                                        .addComponent(comboBox3, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBox2, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))))
                            .addContainerGap())
                );
            }
            tabbedPane1.addTab("\u041f\u0435\u0440\u0435\u0433\u043b\u044f\u0434", panel1);

            //======== panel2 ========
            {

                //======== scrollPane2 ========
                {
                    scrollPane2.setViewportView(table2);
                }

                //---- comboBox4 ----
                comboBox4.setModel(new DefaultComboBoxModel<>(new String[] {
                    "\u0413\u0440\u0443\u043f\u0438",
                    "\u0421\u0442\u0443\u0434\u0435\u043d\u0442\u0438",
                    "\u0420\u0435\u0437\u0443\u043b\u044c\u0442\u0430\u0442\u0438",
                    "\u041d\u043e\u0440\u043c\u0430\u0442\u0438\u0432\u0438"
                }));
                comboBox4.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyWriteFormFilter(e);
                    }
                });

                //---- button1 ----
                button1.setText("\u0414\u043e\u0434\u0430\u0442\u0438 \u0440\u044f\u0434\u043e\u043a");

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel2Layout.createParallelGroup()
                                .addGroup(panel2Layout.createSequentialGroup()
                                    .addComponent(comboBox4, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button1, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 555, Short.MAX_VALUE))
                                .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE))
                            .addContainerGap())
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(comboBox4)
                                .addComponent(button1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
            }
            tabbedPane1.addTab("\u0420\u0435\u0434\u0430\u0433\u0443\u0432\u0430\u043d\u043d\u044f", panel2);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(tabbedPane1)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(tabbedPane1)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Ash Hisenberg
    JTabbedPane tabbedPane1;
    JPanel panel1;
    JScrollPane scrollPane1;
    JTable table1;
    JPanel panel3;
    JComboBox comboBox1;
    JComboBox<String> comboBox2;
    JCheckBox checkBox1;
    JComboBox comboBox3;
    JPanel panel2;
    JScrollPane scrollPane2;
    public JTable table2;
    JComboBox<String> comboBox4;
    JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main(String[] args) {
        MainForm form = new MainForm();
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        form.setVisible(true);
        form.setResizable(false);
    }
}
