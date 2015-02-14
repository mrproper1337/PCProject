import pojo.*;
import java.awt.event.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.GroupLayout;


/**
 * @author Nazar Rudenko
 * https://github.com/mrproper1337
 */
public class MainForm extends JFrame {


    ModelLoader read_ml,write_ml;
    GraphMaker gm;
    boolean listenerIsStopped;

    public MainForm() {

        read_ml = new ModelLoader(table2,0,0,0,0);
        write_ml = new ModelLoader(table2,0);
        gm = new GraphMaker(panel3,label1,label2, read_ml);
        listenerIsStopped = read_ml.listenerIsStopped;
        initComponents();
        table1.setModel(read_ml.getGroupModel(false));
        table2.setModel(read_ml.getGroupModel(true));


//        read_ml.getGroupModel(false);
//        read_ml.getGroupModel(true);
//        read_ml.setGroupCombo();
//        read_ml.setSNCombo();
    }
    private void applyReadFormFilter(ItemEvent e) {
        if(!listenerIsStopped){
            if(comboBox1.getSelectedIndex() == 0) read_ml.getGroupModel(false);
            else read_ml.getStudentModel(false);
            table1.updateUI();
        }
    }
    private void applyWriteFormFilter(ItemEvent e) {
        comboBox5.setEnabled(false);
        comboBox6.setEnabled(false);

        switch(comboBox4.getSelectedIndex()){
            case 0:
                read_ml.getGroupModel(true);
                break;
            case 1:
                comboBox5.setEnabled(true);
                read_ml.getStudentModel(true);
                break;
            case 2:
                read_ml.getSNNameModel();
                break;
            case 3:
                read_ml.getSportNormModel();
                break;
            case 4:
                comboBox5.setEnabled(true);
                comboBox6.setEnabled(true);
                read_ml.getResultModel(read_ml.studentsId.get(0));
                break;
        }
        table2.updateUI();
    }
    private void applyResultFilter(ItemEvent e) {
        if(!listenerIsStopped && comboBox4.getSelectedIndex()!=1){
            if(e.getSource()==comboBox5){
                listenerIsStopped = true;
                comboBox6.removeAllItems();
                read_ml.studentsId.clear();
                for(Student s:(List<Student>)ch.loadTable("from Student where " +
                                "groupId = " + read_ml.groupsId.get(comboBox5.getSelectedIndex())
                ))
                {
                    comboBox6.addItem(s.getName());
                    read_ml.studentsId.add(s.getStudentId());
                }
                listenerIsStopped = false;
                read_ml.getResultModel(read_ml.studentsId.get(0));
            }
            else read_ml.getResultModel(read_ml.studentsId.get(comboBox6.getSelectedIndex()));
        }
    }
    private void applyStudentFilter(ItemEvent e) {
        if(!listenerIsStopped && comboBox4.getSelectedIndex()==1)
            read_ml.getStudentModel(true);
    }




    private void comboBox3ItemStateChanged(ItemEvent e) {
        if(!listenerIsStopped){
            if(comboBox1.getSelectedIndex() == 0)
                makeStatGraph((Group)lastStat);
            else
                makeStatGraph((Student)lastStat);
        }
    }
    private void addNewRow(ActionEvent e) {
        lastQuery = "";
        switch(comboBox4.getSelectedIndex()){
            case 0:
                Group group = new Group();
                group.setGroupName("new");
                read_ml.ch.addToTable(group);
                read_ml.getGroupModel(true);
                read_ml.setGroupCombo();
                break;
            case 1:
                Student student = new Student();
                student.setName("new");
                student.setGroupId((Group) read_ml.groupList.get(comboBox5.getSelectedIndex()));
                read_ml.ch.addToTable(student);
                read_ml.getStudentModel(true);
                break;
            case 2:
                SportNormName snn = new SportNormName();
                snn.setSportNormName("new");
                read_ml.ch.addToTable(snn);
                read_ml.getSNNameModel();
                break;
            case 3:
                SportNorm sn = new SportNorm();
                sn.setSportNormNameId((SportNormName) read_ml.sportNormNameList.get(0));
                sn.setCourseNorm(1);
                sn.setGenderNorm(0);
                sn.setHealthGroupNorm(0);
                sn.setExcellentMark(0);
                sn.setSatisfactorilyMark(0);
                sn.setGoodMark(0);
                read_ml.ch.addToTable(sn);
                read_ml.getSportNormModel();
                break;
            case 4:
                Result result = new Result();
                result.setSportNormId((SportNorm) read_ml.currentAllowedNorms.get(0));
                result.setStudentId(read_ml.currentResultStudent);
                result.setResult(0);
                read_ml.ch.addToTable(result);
                read_ml.getResultModel(read_ml.studentsId.get(comboBox6.getSelectedIndex()));
                break;
        }
        table2.updateUI();
        read_ml.getGroupModel(false);
    }

    private void initComponents() {

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panel3 = new JPanel();
        comboBox1 = new JComboBox();
        comboBox2 = new JComboBox<>();
        checkBox1 = new JCheckBox();
        comboBox3 = new JComboBox();
        label1 = new JLabel();
        label2 = new JLabel();
        panel2 = new JPanel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        comboBox4 = new JComboBox<>();
        button1 = new JButton();
        comboBox5 = new JComboBox();
        comboBox6 = new JComboBox();

        //======== this ========
        setTitle("\u0421\u0442\u0430\u0442\u0438\u0441\u0442\u0438\u043a\u0430 \u0444\u0456\u0437\u0438\u0447\u043d\u0438\u0445 \u043f\u043e\u043a\u0430\u0437\u043d\u0438\u043a\u0456\u0432");
        Container contentPane = getContentPane();

        //======== tabbedPane1 ========
        {

            //======== panel1 ========
            {

                //======== scrollPane1 ========
                {

                    //---- table1 ----
                    table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    table1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                    table1.setRowHeight(20);
                    scrollPane1.setViewportView(table1);
                }

                //======== panel3 ========
                {
                    panel3.setBackground(Color.white);
                    panel3.setLayout(new BorderLayout());
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

                //---- comboBox3 ----
                comboBox3.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        comboBox3ItemStateChanged(e);
                    }
                });

                //---- label1 ----
                label1.setFont(new Font("Segoe UI", Font.ITALIC, 14));

                //---- label2 ----
                label2.setFont(new Font("Segoe UI", Font.ITALIC, 14));

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel1Layout.createParallelGroup()
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(checkBox1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(247, 247, 247)
                                    .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                    .addGap(220, 220, 220))
                                .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 336, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panel1Layout.createParallelGroup()
                                        .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addGroup(panel1Layout.createParallelGroup()
                                                .addComponent(label1, GroupLayout.PREFERRED_SIZE, 576, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label2, GroupLayout.PREFERRED_SIZE, 576, GroupLayout.PREFERRED_SIZE))
                                            .addGap(0, 0, Short.MAX_VALUE)))
                                    .addContainerGap())))
                );
                panel1Layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {comboBox1, comboBox2});
                panel1Layout.setVerticalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addComponent(panel3, GroupLayout.PREFERRED_SIZE, 312, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(label1, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(label2, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(comboBox1)
                                .addComponent(checkBox1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addGroup(panel1Layout.createParallelGroup()
                                        .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                                    .addGap(0, 0, Short.MAX_VALUE)))
                            .addContainerGap())
                );
            }
            tabbedPane1.addTab("\u041f\u0435\u0440\u0435\u0433\u043b\u044f\u0434", panel1);

            //======== panel2 ========
            {

                //======== scrollPane2 ========
                {

                    //---- table2 ----
                    table2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    table2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                    table2.setRowHeight(28);
                    table2.setComponentPopupMenu(null);
                    table2.setRowSorter(null);
                    table2.setMinimumSize(new Dimension(15, 28));
                    table2.setAlignmentX(0.0F);
                    table2.setAlignmentY(0.0F);
                    scrollPane2.setViewportView(table2);
                }

                //---- comboBox4 ----
                comboBox4.setModel(new DefaultComboBoxModel<>(new String[] {
                    "\u0413\u0440\u0443\u043f\u0438",
                    "\u0421\u0442\u0443\u0434\u0435\u043d\u0442\u0438",
                    "\u041d\u043e\u0440\u043c\u0430\u0442\u0438\u0432\u0438(\u041f\u0435\u0440\u0435\u043b\u0456\u043a)",
                    "\u041d\u043e\u0440\u043c\u0430\u0442\u0438\u0432\u0438(\u0412\u0438\u043c\u043e\u0433\u0438)",
                    "\u0420\u0435\u0437\u0443\u043b\u044c\u0442\u0430\u0442\u0438"
                }));
                comboBox4.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyWriteFormFilter(e);
                    }
                });

                //---- button1 ----
                button1.setText("\u0414\u043e\u0434\u0430\u0442\u0438 \u0440\u044f\u0434\u043e\u043a");
                button1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addNewRow(e);
                    }
                });

                //---- comboBox5 ----
                comboBox5.setEnabled(false);
                comboBox5.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyResultFilter(e);
                        applyStudentFilter(e);
                    }
                });

                //---- comboBox6 ----
                comboBox6.setEnabled(false);
                comboBox6.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        applyResultFilter(e);
                    }
                });

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addGroup(panel2Layout.createParallelGroup()
                                .addGroup(panel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(comboBox4)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboBox5)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboBox6, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)
                                    .addGap(223, 223, 223)
                                    .addComponent(button1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE))
                            .addGap(23, 23, 23))
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 424, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel2Layout.createParallelGroup()
                                .addComponent(comboBox5, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addComponent(comboBox4, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addComponent(button1)
                                .addComponent(comboBox6, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap())
                );
                panel2Layout.linkSize(SwingConstants.VERTICAL, new Component[] {button1, comboBox4, comboBox5, comboBox6});
            }
            tabbedPane1.addTab("\u0420\u0435\u0434\u0430\u0433\u0443\u0432\u0430\u043d\u043d\u044f", panel2);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(tabbedPane1, GroupLayout.DEFAULT_SIZE, 936, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(tabbedPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    JTabbedPane tabbedPane1;
    JPanel panel1;
    JScrollPane scrollPane1;
    JTable table1;
    JPanel panel3;
    JComboBox comboBox1;
    JComboBox<String> comboBox2;
    JCheckBox checkBox1;
    JComboBox comboBox3;
    JLabel label1;
    JLabel label2;
    JPanel panel2;
    JScrollPane scrollPane2;
    public JTable table2;
    JComboBox<String> comboBox4;
    JButton button1;
    JComboBox comboBox5;
    JComboBox comboBox6;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main(String[] args) {
        MainForm form = new MainForm();
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        form.setVisible(true);
        form.setResizable(false);
    }
}
