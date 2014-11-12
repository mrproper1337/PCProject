import java.awt.event.*;
import pojo.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/*
 * Created by JFormDesigner on Sun Nov 09 21:32:23 EET 2014
 */



/**
 * @author Ash Coopeer
 */
public class MainForm extends JFrame {

    private ConnectHibernate ch;
    private DefaultTableModel students_tm,groups_tm;
    //private List groupList,studentList;

    public MainForm() {
        ch = new ConnectHibernate();
        initComponents();
        setModels(ch.loadTable("from Group"),ch.loadTable("from Student"));
        table1.setModel(students_tm);
    }
    private void setModels(List groupList,List studentList){
        groups_tm = new DefaultTableModel(){

            @Override
            public int getRowCount() {
                return groupList.size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public String getColumnName(int columnIndex) {
                if(columnIndex==0)return "ID";
                else return "Група";
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                try{
                    if(columnIndex==0)
                        return Class.forName("java.lang.Integer");
                    else
                        return Class.forName("java.lang.String");

                }catch(ClassNotFoundException e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Group ret;
                if(columnIndex==0){
                    ret = (pojo.Group)groupList.get(rowIndex);
                    return ret.getGroupId();
                }

                else{
                    ret = (pojo.Group)groupList.get(rowIndex);
                    return ret.getGroupName();
                }

            }
        };
        students_tm = new DefaultTableModel(){

            @Override
            public int getRowCount() {
                return studentList.size();
            }

            @Override
            public int getColumnCount() {
                return 5;
            }

            @Override
            public String getColumnName(int columnIndex) {
                switch(columnIndex){
                    case 0:
                        return "ID";
                    case 1:
                        return "П.І.Б.";
                    case 2:
                        return "Стать";
                    case 3:
                        return "Група здоров'я";
                    case 4:
                        return "Група";

                }
                return null;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                try{
                    switch(columnIndex){
                        case 0:
                            return Class.forName("java.lang.Integer");
                        case 1:
                            return Class.forName("java.lang.String");
                        case 2:
                            return Class.forName("java.lang.String");
                        case 3:
                            return Class.forName("java.lang.String");
                        case 4:
                            return Class.forName("java.lang.String");

                    }
                }catch(ClassNotFoundException e){
                    e.printStackTrace();
                }
                return null;

            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Student res=(pojo.Student)studentList.get(rowIndex);
                switch(columnIndex){
                    case 0:
                        return res.getStudentId();
                    case 1:
                        return res.getName();
                    case 2:
                        if(res.getGender()==1)
                            return "хлопець";
                        else
                            return "дівчина";
                    case 3:
                        if(res.getHealthGroup()==1)
                            return "Спецгрупа";
                        else
                            return "Звичайна";
                    case 4:
                        return res.getGroupId().getGroupName();
                }
                return null;
            }
        };

        comboBox1.addItem("Всі");
        for(Group a:(List<pojo.Group>)groupList)
            comboBox1.addItem(a.getGroupName());

        comboBox2.addItem("Хлопці");
        comboBox2.addItem("Дівчата");

    }

    private void comboBox1ItemStateChanged(ItemEvent e) {
        if(comboBox1.getSelectedIndex()!=0){
            comboBox2.setEnabled(true);
        }
        else comboBox2.setEnabled(false);
    }

    private void initComponents() {

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Ash Hisenberg
        tabbedPane1 = new JTabbedPane();
        ReadForm = new JPanel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panel3 = new JPanel();
        comboBox1 = new JComboBox();
        comboBox2 = new JComboBox();
        checkBox1 = new JCheckBox();
        comboBox3 = new JComboBox();
        WriteForm = new JPanel();

        //======== this ========
        Container contentPane = getContentPane();

        //======== tabbedPane1 ========
        {

            //======== ReadForm ========
            {

                // JFormDesigner evaluation mark
                ReadForm.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                        "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                        java.awt.Color.red), ReadForm.getBorder())); ReadForm.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});


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
                            .addGap(0, 443, Short.MAX_VALUE)
                    );
                    panel3Layout.setVerticalGroup(
                        panel3Layout.createParallelGroup()
                                .addGap(0, 500, Short.MAX_VALUE)
                    );
                }

                //---- comboBox1 ----
                comboBox1.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        comboBox1ItemStateChanged(e);
                    }
                });

                //---- comboBox2 ----
                comboBox2.setEnabled(false);

                //---- checkBox1 ----
                checkBox1.setText("\u0421\u043f\u0435\u0446\u0433\u0440\u0443\u043f\u0430");
                checkBox1.setFont(new Font("Segoe UI", Font.PLAIN, 18));

                GroupLayout ReadFormLayout = new GroupLayout(ReadForm);
                ReadForm.setLayout(ReadFormLayout);
                ReadFormLayout.setHorizontalGroup(
                    ReadFormLayout.createParallelGroup()
                            .addGroup(ReadFormLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(ReadFormLayout.createParallelGroup()
                                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 595, GroupLayout.PREFERRED_SIZE)
                                            .addGroup(ReadFormLayout.createSequentialGroup()
                                                    .addComponent(comboBox1, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(comboBox2, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(checkBox1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGap(32, 32, 32)))
                                    .addGroup(ReadFormLayout.createParallelGroup()
                                            .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(ReadFormLayout.createSequentialGroup()
                                                    .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 193, Short.MAX_VALUE)))
                                    .addContainerGap())
                );
                ReadFormLayout.setVerticalGroup(
                    ReadFormLayout.createParallelGroup()
                            .addGroup(ReadFormLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(ReadFormLayout.createParallelGroup()
                                            .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                                    .addGroup(ReadFormLayout.createParallelGroup()
                                            .addComponent(comboBox1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(comboBox2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                                            .addComponent(checkBox1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(comboBox3, GroupLayout.Alignment.TRAILING))
                                    .addContainerGap())
                );
                ReadFormLayout.linkSize(SwingConstants.VERTICAL, new Component[] {checkBox1, comboBox1, comboBox2, comboBox3});
            }
            tabbedPane1.addTab("\u041f\u0435\u0440\u0435\u0433\u043b\u044f\u0434", ReadForm);

            //======== WriteForm ========
            {

                GroupLayout WriteFormLayout = new GroupLayout(WriteForm);
                WriteForm.setLayout(WriteFormLayout);
                WriteFormLayout.setHorizontalGroup(
                    WriteFormLayout.createParallelGroup()
                        .addGap(0, 1052, Short.MAX_VALUE)
                );
                WriteFormLayout.setVerticalGroup(
                    WriteFormLayout.createParallelGroup()
                        .addGap(0, 563, Short.MAX_VALUE)
                );
            }
            tabbedPane1.addTab("\u0420\u0435\u0434\u0430\u0433\u0443\u0432\u0430\u043d\u043d\u044f", WriteForm);
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
    private JTabbedPane tabbedPane1;
    private JPanel ReadForm;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel panel3;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JCheckBox checkBox1;
    private JComboBox comboBox3;
    private JPanel WriteForm;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
