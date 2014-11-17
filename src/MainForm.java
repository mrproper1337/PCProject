import java.awt.event.*;
import pojo.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/*
 * Created by JFormDesigner on Sun Nov 09 21:32:23 EET 2014
 */



/**
 * @author Ash Coopeer
 */
public class MainForm extends JFrame {

    private DefaultTableModel students_tm,groups_tm;
    ConnectHibernate ch;
    List<Integer> groupsId;
    boolean listenerIsStopped;
    public MainForm() {
        ch = new ConnectHibernate();
        groupsId=new ArrayList<>();
        initComponents();
        listenerIsStopped=true;
        comboBox2.addItem("Хлопці");
        comboBox2.addItem("Дівчата");
        setGroupModels();
        table1.setModel(groups_tm);

    }
    //should be called after st_group changes
    private void setGroupModels(){
        System.out.println("setGroupModels");
        final List groupList=ch.loadTable("from Group");
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
        listenerIsStopped=true;
        if(groupsId!=null)groupsId.clear();
        comboBox1.removeAllItems();
        comboBox1.addItem("Всі");
        for(Group a:(List<pojo.Group>)groupList){
            comboBox1.addItem(a.getGroupName());
            groupsId.add(a.getGroupId());
        }

        listenerIsStopped=false;
    }

    //should be called after students changes
    private void setStudentModels(){
        System.out.println("setStudModels");
        int check;
        if(checkBox1.isSelected())
            check=1;
        else
            check=0;

        String query = "from Student where(" +
                "groupId = "+groupsId.get(comboBox1.getSelectedIndex()-1)+
                " and " +
                "gender ="+comboBox2.getSelectedIndex()+
                " and " +
                "healthGroup = "+check+
                ")";
        System.out.println(query);
        final List studentList=ch.loadTable(query);
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
                        if(res.getGender()==0)
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
    }

    private void applyFilter(ItemEvent e) {
        if(!listenerIsStopped){
            if(comboBox1.getSelectedIndex()==0){
                setGroupModels();
                table1.setModel(groups_tm);
            }
            else{
                setStudentModels();
                table1.setModel(students_tm);
            }
            table1.updateUI();
        }
    }

    private void initComponents() {

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Ash Hisenberg
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panel3 = new JPanel();
        comboBox1 = new JComboBox();
        comboBox2 = new JComboBox();
        checkBox1 = new JCheckBox();
        comboBox3 = new JComboBox();
        panel2 = new JPanel();

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
                        applyFilter(e);
                    }
                });

                //---- comboBox2 ----
                comboBox2.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyFilter(e);
                    }
                });

                //---- checkBox1 ----
                checkBox1.setText("\u0421\u043f\u0435\u0446\u0433\u0440\u0443\u043f\u0430");
                checkBox1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                checkBox1.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyFilter(e);
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

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .addGap(0, 957, Short.MAX_VALUE)
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .addGap(0, 468, Short.MAX_VALUE)
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
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel panel3;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JCheckBox checkBox1;
    private JComboBox comboBox3;
    private JPanel panel2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
