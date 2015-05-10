import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pojo.*;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;

public class ModelLoader {
    final static ConnectHibernate ch = new ConnectHibernate();
    MarkCalculator mc;
    Object lastStat;
    final JTable table;
    final JPanel gr_p;
    final JLabel gr_l1,gr_l2;
    List<Integer> groupsId,snId,studentsId;
    List groupList, sportNormNameList, currentAllowedNorms;
    Student currentResultStudent;
    String lastQuery,currentQuery;
    boolean readyToListen;
    final boolean editable;
    int group,gender,health,sport_n;

    ModelLoader(final JTable table,final JPanel gr_p,JLabel gr_l1,JLabel gr_l2){

        mc = new MarkCalculator();
        lastQuery = "";
        groupsId = new ArrayList<>();
        snId = new ArrayList<>();
        studentsId = new ArrayList<>();
        sportNormNameList = ch.loadTable("from SportNormName");
        readyToListen = false;

        this.table = table;
        this.gr_p = gr_p;
        this.gr_l1 = gr_l1;
        this.gr_l2 = gr_l2;
        this.editable = false;

        this.group = 1;
        this.gender = 0;
        this.health = 0;
        this.sport_n = 1;
    }

    ModelLoader(final JTable table){

        lastQuery = "";
        groupsId = new ArrayList<>();
        snId = new ArrayList<>();
        studentsId = new ArrayList<>();
        sportNormNameList = ch.loadTable("from SportNormName");
        readyToListen = false;

        this.table = table;
        this.gr_p = null;
        this.gr_l1 = null;
        this.gr_l2 = null;
        this.editable = true;

        this.group = 1;
    }

    public void setModel(int group,int gender,int health,int sport_n){
        this.group = group < 0 ? 0 : group;
        this.gender = gender;
        this.health = health;
        this.sport_n = sport_n;
    }
    public void setModel(int group){
        this.group = group;
    }

    public boolean isReadyToListen() {
        return readyToListen;
    }
    public void setReadyToListen(boolean readyToListen) {
        this.readyToListen = readyToListen;
    }


    private void deleteRow(int modelKey,Object toRemove){
        lastQuery = "";
        switch (modelKey){
            case 1:
                Group group = (Group)toRemove;
                for(Student st:(List<Student>)ch.loadTable("from Student where groupId ="+group.getGroupId())){
                    for(Result rs:(List<Result>)ch.loadTable("from Result where studentId ="+st.getStudentId())){
                        ch.deleteFromTable(rs);
                    }
                    ch.deleteFromTable(st);
                }
                ch.deleteFromTable(group);
                break;
            case 2:
                Student student = (Student)toRemove;
                for(Result rs:(List<Result>)ch.loadTable("from Result where studentId ="+student.getStudentId())){
                    ch.deleteFromTable(rs);
                }
                ch.deleteFromTable(student);
                break;
            case 3:
                SportNormName sportNorN = (SportNormName)toRemove;
                for(SportNorm sn:(List<SportNorm>)ch.loadTable("from SportNorm where  sportNormNameId =" + sportNorN.getSportNormNameId())){
                    for(Result rs:(List<Result>)ch.loadTable("from Result where sportNormId ="+sn.getSportNormId())){
                        ch.deleteFromTable(rs);
                    }
                    ch.deleteFromTable(sn);
                }
                ch.deleteFromTable(sportNorN);
                break;
            case 4:
                SportNorm sportN = (SportNorm)toRemove;
                for(Result rs:(List<Result>)ch.loadTable("from Result where sportNormId =" + sportN.getSportNormId())){
                    ch.deleteFromTable(rs);
                }
                ch.deleteFromTable(sportN);
                break;
            case 5:
                ch.deleteFromTable(toRemove);
                break;
        }
    }

    public void setStats(Group cgroup){
        lastStat = cgroup;
        gr_p.removeAll();
        gr_p.add(new ChartPanel(mc.makeStatGraph(cgroup)));
        gr_l1.setText(mc.getResults());
        gr_l2.setText(mc.getMarks());
        gr_p.revalidate();
    }
    public void setStats(Student cstudent){
        lastStat = cstudent;
        gr_p.removeAll();
        gr_p.add(new ChartPanel(mc.makeStatGraph(cstudent)));
        gr_l1.setText(mc.getResults());
        gr_l2.setText(mc.getMarks());
        gr_p.revalidate();
    }

    public void getGroupModel(){
        currentQuery="from Group";
        groupList = ch.loadTable("from Group");
        DefaultTableModel groups_tm;
        lastQuery = currentQuery;
        groups_tm = new DefaultTableModel(){

            @Override
            public int getRowCount() {
                return groupList.size();
            }

            @Override
            public int getColumnCount() {
                return 1;
            }

            @Override
            public String getColumnName(int columnIndex) {
                return "Група";
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if(!editable)
                    setStats((Group) groupList.get(rowIndex));
                return editable;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Group ret = (pojo.Group)groupList.get(rowIndex);
                return ret.getGroupName().equals("new") ? "" : ret.getGroupName();
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                Group obj =(Group)groupList.get(row);
                if(aValue.toString().equals("") &&
                        JOptionPane.showConfirmDialog(null, "Видалити групу ?\nТакож будуть видалені " +
                                        "студенти\nданної групи та їх результати !", "Поле не може бути пустим",
                                JOptionPane.YES_NO_OPTION)==0)
                    deleteRow(1,obj);
                else{
                    obj.setGroupName(aValue.toString());
                    ch.updateInTable(obj);
                }
            }
        };
        table.setModel(groups_tm);
    }
    public void getStudentModel(){
        if(!editable){
            currentQuery = "from Student where(" +
                    "groupId = "+group+
                    " and " +
                    "gender ="+gender+
                    " and " +
                    "healthGroup = "+health+
                    ")";
        }
        else currentQuery = "from Student where groupId = "+group;

        if(!lastQuery.equals(currentQuery)){
            DefaultTableModel students_tm;
            lastQuery = currentQuery;
            final java.util.List studentList=ch.loadTable(currentQuery);

            students_tm = new DefaultTableModel(){

                @Override
                public int getRowCount() {
                    return studentList.size();
                }

                @Override
                public int getColumnCount() {
                    return editable?3:1;
                }

                @Override
                public String getColumnName(int columnIndex) {
                    if(editable)
                        switch(columnIndex){
                            case 0:
                                return "П.І.Б.";
                            case 1:
                                return "Стать";
                            case 2:
                                return "Група здоров'я";

                        }
                    return "П.І.Б.";
                }

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    if(!editable)
                        setStats((Student)studentList.get(rowIndex));
                    return editable;
                }

                @Override
                public Object getValueAt(int rowIndex, int columnIndex) {
                    Student res=(pojo.Student)studentList.get(rowIndex);
                    if(editable)
                        switch(columnIndex){
                            case 0:
                                if(res.getName().equals("new"))
                                    return "";
                                return res.getName();
                            case 1:
                                if(res.getGender()==0 || res.getName().equals("new"))
                                    return "хлопець";
                                else
                                    return "дівчина";
                            case 2:
                                if(res.getHealthGroup()==0 || res.getName().equals("new"))
                                    return "Звичайна";
                                else
                                    return "Спецгрупа";
                        }
                    if(res.getName().equals("new"))
                        return "";
                    return res.getName();
                }

                @Override
                public void setValueAt(Object aValue, int row, int column) {
                    Student obj =(Student)studentList.get(row);
                    if(aValue.toString().equals("") &&
                            JOptionPane.showConfirmDialog(null,"Видалити студента ?\nТакож будуть видалені усі\nйого результати !","Поле не може бути пустим",
                                    JOptionPane.YES_NO_OPTION)==0)
                        deleteRow(2,obj);
                    else{
                        switch(column){
                            case 0:
                                obj.setName(aValue.toString());
                                break;
                            case 1:
                                if(aValue=="хлопець")
                                    obj.setGender(0);
                                else
                                    obj.setGender(1);
                                break;
                            case 2:
                                if(aValue=="Звичайна")
                                    obj.setHealthGroup(0);
                                else
                                    obj.setHealthGroup(1);
                                break;
                        }
                        ch.updateInTable(obj);
                    }


                }
            };
            if(editable){
                table.setModel(students_tm);

                ArrayList<String> cbItem = new ArrayList();
                cbItem.add("хлопець");
                cbItem.add("дівчина");
                initInsertedCombos(table,1,cbItem);
                cbItem.clear();

                cbItem.add("Звичайна");
                cbItem.add("Спецгрупа");
                initInsertedCombos(table,2,cbItem);
                cbItem.clear();
            }
            else table.setModel(students_tm);
        }
    }
    public void getSNNameModel(){
        currentQuery="from SportNormName";
        sportNormNameList = ch.loadTable(currentQuery);
        DefaultTableModel sportNormName_tm;
        lastQuery = currentQuery;
        sportNormName_tm = new DefaultTableModel(){

            @Override
            public int getRowCount() {
                return sportNormNameList.size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public String getColumnName(int columnIndex) {
                if(columnIndex == 0)return "Норматив";
                else
                    return "\"Модальність\" нормативу";
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return true;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                SportNormName ret;
                ret = (SportNormName)sportNormNameList.get(rowIndex);
                if(columnIndex == 0)return ret.getSportNormName().equals("new") ? "" : ret.getSportNormName();
                else return ret.getMarkMode() == 0 ? "Менше - краще" : "Більше - краще";
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                SportNormName obj =(SportNormName)sportNormNameList.get(row);
                if(aValue.toString().equals("") &&
                        JOptionPane.showConfirmDialog(null,"Видалити норматив ?\nТакож будуть видалені вимоги до нормативу\nта його результати !","Поле не може бути пустим",
                                JOptionPane.YES_NO_OPTION)==0){
                    deleteRow(3,obj);

                }else{
                    if(column == 0)obj.setSportNormName(aValue.toString());
                    else if(aValue.equals("Менше - краще"))
                        obj.setMarkMode(0);
                    else  obj.setMarkMode(1);

                    ch.updateInTable(obj);
                }
            }
        };
        table.setModel(sportNormName_tm);

        ArrayList<String> cbItem = new ArrayList();
        cbItem.add("Менше - краще");
        cbItem.add("Більше - краще");
        initInsertedCombos(table,1,cbItem);
        cbItem.clear();
    }
    public void getSportNormModel(){
        currentQuery="from SportNorm";
        DefaultTableModel sportNorm_tm;
        lastQuery = currentQuery;
        final List sportNormList=ch.loadTable(currentQuery);
        sportNorm_tm = new DefaultTableModel(){

            @Override
            public int getRowCount() {
                return sportNormList.size();
            }

            @Override
            public int getColumnCount() {
                return 7;
            }

            @Override
            public String getColumnName(int columnIndex) {
                switch(columnIndex) {
                    case 0:
                        return "Норматив";
                    case 1:
                        return "Курс";
                    case 2:
                        return "Стать";
                    case 3:
                        return "Група здоров'я";
                    case 4:
                        return "Відмінно";
                    case 5:
                        return "Добре";
                    case 6:
                        return "Задовільно";
                }
                return "";
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return true;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                SportNorm res=(SportNorm)sportNormList.get(rowIndex);
                boolean newRow = false;
                if(res.getGoodMark()==0 && res.getSatisfactorilyMark()==0 && res.getExcellentMark()==0 )
                    newRow = true;

                switch(columnIndex){
                    case 0:
                        if(newRow)return sportNormNameList.get(0);
                        return res.getSportNormNameId().getSportNormName();
                    case 1:
                        if(newRow)return 1;
                        return res.getCourseNorm();
                    case 2:
                        if(res.getGenderNorm()==0 || newRow)
                            return "хлопці";
                        else
                            return "дівчата";
                    case 3:
                        if(res.getHealthGroupNorm()==0 || newRow)
                            return "Звичайна";
                        else
                            return "Спецгрупа";
                    case 4:
                        if(newRow) return "";
                        return res.getExcellentMark();
                    case 5:
                        if(newRow) return "";
                        return res.getGoodMark();
                    case 6:
                        if(newRow) return "";
                        return res.getSatisfactorilyMark();
                }
                return "";
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                SportNorm obj =(SportNorm)sportNormList.get(row);
                if(aValue.toString().equals("") &&
                        JOptionPane.showConfirmDialog(null,"Видалити вимоги до нормативу?\nТакож будуть видалені його результати !","Поле не може бути пустим",
                                JOptionPane.YES_NO_OPTION)==0){
                    deleteRow(4,obj);
                }else{
                    switch(column){
                        case 0:
                            for(SportNormName snn:(List<SportNormName>)sportNormNameList)
                                if(snn.getSportNormName().equals(aValue))
                                    obj.setSportNormNameId(snn);
                            break;
                        case 1:
                            obj.setCourseNorm(Integer.parseInt(aValue.toString()));
                            break;
                        case 2:
                            if(aValue=="хлопці")
                                obj.setGenderNorm(0);
                            else
                                obj.setGenderNorm(1);
                            break;
                        case 3:
                            if(aValue=="Звичайна")
                                obj.setHealthGroupNorm(0);
                            else
                                obj.setHealthGroupNorm(1);
                            break;
                        case 4:
                            obj.setExcellentMark(Double.parseDouble(aValue.toString()));
                            break;
                        case 5:
                            obj.setGoodMark(Double.parseDouble(aValue.toString()));
                            break;
                        case 6:
                            obj.setSatisfactorilyMark(Double.parseDouble(aValue.toString()));
                            break;
                    }
                    ch.updateInTable(obj);
                }


            }
        };
        table.setModel(sportNorm_tm);

        ArrayList cbItem = new ArrayList();

        for(SportNormName snn:(List<SportNormName>)sportNormNameList)
            cbItem.add(snn.getSportNormName());
        initInsertedCombos(table,0,cbItem,160);
        cbItem.clear();

        cbItem.add(1);
        cbItem.add(2);
        cbItem.add(3);
        cbItem.add(4);
        initInsertedCombos(table, 1, cbItem, 40);
        cbItem.clear();

        cbItem.add("хлопці");
        cbItem.add("дівчата");
        initInsertedCombos(table,2,cbItem,80);
        cbItem.clear();

        cbItem.add("Звичайна");
        cbItem.add("Спецгрупа");
        initInsertedCombos(table,3,cbItem,90);
        cbItem.clear();

    }
    public void getResultModel(final int studentId){
        currentQuery="from Result where studentId = "+studentId;
        DefaultTableModel result_tm;
        final List resultList = ch.loadTable(currentQuery);
        lastQuery = currentQuery;

        currentResultStudent =(Student)ch.loadTable("from Student where studentId ="+studentId).get(0);
        currentAllowedNorms = new ArrayList();
        for(SportNorm sn:(List<SportNorm>)ch.loadTable("from SportNorm where(" +
                " genderNorm ="+ currentResultStudent.getGender()+
                " and "+
                " healthGroupNorm ="+ currentResultStudent.getHealthGroup()+
                ")")){
            currentAllowedNorms.add(sn);
        }

        result_tm = new DefaultTableModel(){

            @Override
            public int getRowCount() {
                return resultList.size();
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public String getColumnName(int columnIndex) {
                switch(columnIndex) {
                    case 0:
                        return "Норматив";
                    case 1:
                        return "Курс";
                    case 2:
                        return "Результат";
                }
                return "";
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return true;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Result res=(Result)resultList.get(rowIndex);
                switch(columnIndex){
                    case 0:
                        if(res.getResult()==0)return sportNormNameList.get(0);
                        return res.getSportNormId().getSportNormNameId().getSportNormName();
                    case 1:
                        if(res.getResult()==0) return 1;
                        return res.getSportNormId().getCourseNorm();
                    case 2:
                        if(res.getResult()==0) return "";
                        return res.getResult();
                }
                return "";
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                Result obj =(Result)resultList.get(row);
                if(aValue.toString().equals("") &&
                        JOptionPane.showConfirmDialog(null,"Видалити результат ?","Поле не може бути пустим",
                                JOptionPane.YES_NO_OPTION)==0){
                    deleteRow(5,obj);
                }else{
                    switch(column){
                        case 0:
                            for(SportNorm asn:(List<SportNorm>) currentAllowedNorms)
                                if(asn.getSportNormNameId().getSportNormName().equals(aValue) &&
                                        asn.getCourseNorm()==Integer.parseInt(getValueAt(row,1).toString()))
                                    obj.setSportNormId(asn);
                            break;
                        case 1:
                            for(SportNorm asn:(List<SportNorm>) currentAllowedNorms)
                                if(asn.getSportNormNameId().getSportNormName().equals(getValueAt(row,0).toString()) &&
                                        asn.getCourseNorm()==Integer.parseInt(aValue.toString()))
                                    obj.setSportNormId(asn);
                            break;
                        case 2:
                            obj.setResult(Double.parseDouble(aValue.toString()));
                            break;
                    }
                    ch.updateInTable(obj);
                }

            }
        };
        table.setModel(result_tm);

        ArrayList cbItem = new ArrayList();

        for(SportNorm allowed:(List<SportNorm>) currentAllowedNorms)
            if(!cbItem.contains(allowed.getSportNormNameId().getSportNormName()))
                cbItem.add(allowed.getSportNormNameId().getSportNormName());
        initInsertedCombos(table,0,cbItem,160);
        cbItem.clear();

        for(SportNorm allowed:(List<SportNorm>) currentAllowedNorms)
            if(!cbItem.contains(allowed.getCourseNorm()))
                cbItem.add(allowed.getCourseNorm());
        initInsertedCombos(table,1,cbItem,40);
        cbItem.clear();

    }

    private void initInsertedCombos(JTable table,int columnIndex,ArrayList<String> rows){//after TM init
        TableColumn column;
        column = table.getColumnModel().getColumn(columnIndex);
        column.setCellRenderer(new ComboBoxCellRenderer(rows));
        column.setCellEditor(new ComboBoxCellEditor(rows));
    }
    private void initInsertedCombos(JTable table,int columnIndex,ArrayList<String> rows,int width){
        TableColumn column;
        column = table.getColumnModel().getColumn(columnIndex);
        column.setCellRenderer(new ComboBoxCellRenderer(rows,width));
        column.setCellEditor(new ComboBoxCellEditor(rows,width));
    }
    public void setGroupCombo(JComboBox combo){
        List groupList=ch.loadTable("from Group");
        setReadyToListen(false);
        groupsId.clear();
        combo.removeAllItems();
        if(!editable)
            combo.addItem("По групам");
        for(Group a:(java.util.List<Group>)groupList){
            combo.addItem(a.getGroupName());
            groupsId.add(a.getGroupId());
        }
        setReadyToListen(true);
    }
    public void setSNCombo(JComboBox combo){
        setReadyToListen(false);
        snId.clear();
        combo.removeAllItems();
        for(SportNormName a:(List<SportNormName>)sportNormNameList){
            combo.addItem(a.getSportNormName());
            snId.add(a.getSportNormNameId());
        }
        setReadyToListen(true);
    }
    public void setStudentCombo(JComboBox combo){
        setReadyToListen(false);
        combo.removeAllItems();
        studentsId.clear();
        for(Student s:(List<Student>)ch.loadTable("from Student where groupId = "+group)){
            combo.addItem(s.getName());
            studentsId.add(s.getStudentId());
        }
        setReadyToListen(true);
    }


    class MarkCalculator {
        GraphMaker gr;
        String results,marks;

        MarkCalculator(){
            gr = new GraphMaker();
            results = null;
            marks = null;
        }

        private  int getMark(SportNorm sn,double result){
            int mark = 2;
            if(sn.getSportNormNameId().getMarkMode() == 0){
                if(result <= sn.getExcellentMark())
                    mark = 5;
                else if(result <= sn.getGoodMark())
                    mark = 4;
                else if(result <= sn.getSatisfactorilyMark())
                    mark = 3;
            }
            else{
                if(result >= sn.getExcellentMark())
                    mark = 5;
                else if(result >= sn.getGoodMark())
                    mark = 4;
                else if(result >= sn.getSatisfactorilyMark())
                    mark = 3;
            }
            return mark;
        }
        public JFreeChart makeStatGraph(Group currGroup){
            double[] averageResults = new double[4];
            List currStGr = ch.loadTable("from Student where(" +
                    "groupId = "+currGroup.getGroupId()+
                    " and " +
                    "gender ="+gender+
                    " and " +
                    "healthGroup = "+health+
                    ")");

            List currSP = ch.loadTable("from SportNorm where " +
                    "(sportNormNameId =" +sport_n+
                    " and " +
                    "genderNorm =" +gender+
                    " and " +
                    "healthGroupNorm =" +health+
                    ")");
            int iii;
            String studQr = "",spQr = "";
            iii = 0;
            for(Student st:(List<Student>)currStGr){
                if(iii==0)
                    studQr = (" studentId =" + st.getStudentId());
                else
                    studQr += (" or studentId =" + st.getStudentId());
                iii++;
            }
            iii = 0;
            for(SportNorm sp:(List<SportNorm>)currSP){
                if(iii==0)
                    spQr = (" sportNormId =" + sp.getSportNormId());
                else
                    spQr += (" or sportNormId =" + sp.getSportNormId());
                iii++;
            }


            int[] rc = new int[4];
            if(!Objects.equals(studQr, "") && !Objects.equals(spQr, ""))
                for(Result res:(List<Result>)ch.loadTable("from Result where " +
                                " ("+studQr+")" +
                                " and " +
                                " ("+spQr+")"
                )){
                    switch(res.getSportNormId().getCourseNorm()){
                        case 1:
                            averageResults[0] += res.getResult();
                            rc[0]++;
                            break;
                        case 2:
                            averageResults[1] += res.getResult();
                            rc[1]++;
                            break;
                        case 3:
                            averageResults[2] += res.getResult();
                            rc[2]++;
                            break;
                        case 4:
                            averageResults[3] += res.getResult();
                            rc[3]++;
                            break;
                    }
                }

            for(int i = 0;i<4;i++)
                if(rc[i]!=0)
                    averageResults[i]/=rc[i];

            XYSeries groupSeries = new XYSeries("Середні показники "+currGroup.getGroupName());
            SportNormName spncurr = (SportNormName)ch.loadTable("from SportNormName where sportNormNameId ="+sport_n).get(0);
            results = "Середні результати по курсам : ";
            marks = "Середні оцінки по курсам : ";
            for(int i = 1;i<=4;i++){
                groupSeries.add(i,spncurr.getMarkMode()==0?averageResults[i-1]*-1:averageResults[i-1]);
                SportNorm resNorm = null;
                for(SportNorm sp:(List<SportNorm>)currSP)
                    if(sp.getCourseNorm() == i)
                        resNorm = sp;
                results += "    "+i+"-й : "+(averageResults[i-1]==0?"н/а":averageResults[i-1]);
                marks +=   "       "+(averageResults[i-1]==0?"н/а":getMark(resNorm,averageResults[i-1]));
            }


            XYDataset data = new XYSeriesCollection(groupSeries);
            return gr.createChart((XYSeriesCollection) data, "Середні показники " + currGroup.getGroupName());
        }
        public JFreeChart makeStatGraph(Student currStudent){
            double[] normResults = new double[4];
            List currSP = ch.loadTable("from SportNorm where " +
                    "(sportNormNameId =" +sport_n+
                    " and " +
                    "genderNorm =" +gender+
                    " and " +
                    "healthGroupNorm =" +health+
                    ")");
            int iii;
            String spQr = "";
            iii = 0;
            for(SportNorm sp:(List<SportNorm>)currSP){
                if(iii==0)
                    spQr = (" sportNormId =" + sp.getSportNormId());
                else
                    spQr += (" or sportNormId =" + sp.getSportNormId());
                iii++;
            }
            if(!spQr.equals(""))
                for(Result res:(List<Result>)ch.loadTable("from Result where studentId ="+currStudent.getStudentId()+" and( "+spQr+" )")){
                    switch(res.getSportNormId().getCourseNorm()){
                        case 1:
                            normResults[0] = res.getResult();
                            break;
                        case 2:
                            normResults[1] = res.getResult();
                            break;
                        case 3:
                            normResults[2] = res.getResult();
                            break;
                        case 4:
                            normResults[3] = res.getResult();
                            break;
                    }
                }
            XYSeries studSeries = new XYSeries("Показники : "+currStudent.getName());
            SportNormName spncurr = (SportNormName)ch.loadTable("from SportNormName where sportNormNameId ="+sport_n).get(0);
            results = "Результати по курсам : ";
            marks = "Оцінки по курсам : ";
            for(int i = 1;i<=4;i++){
                studSeries.add(i, spncurr.getMarkMode() == 0 ? normResults[i - 1] * -1 : normResults[i - 1]);
                SportNorm resNorm = null;
                for(SportNorm sp:(List<SportNorm>)currSP)
                    if(sp.getCourseNorm() == i)
                        resNorm = sp;
                results += "    "+i+"-й : "+(normResults[i-1]==0?"н/а":normResults[i-1]);
                marks +=   "       "+(normResults[i-1]==0?"н/а":getMark(resNorm,normResults[i-1]));
            }


            XYDataset data = new XYSeriesCollection(studSeries);
            return gr.createChart((XYSeriesCollection) data, "Показники : " + currStudent.getName());
        }
        public String getResults() {
            return results;
        }
        public String getMarks() {
            return marks;
        }
    }
    class ComboBoxPanel extends JPanel {
        ArrayList comboItems = null;
        protected JComboBox comboBox;
        public ComboBoxPanel(ArrayList comboItems) {

            this.comboItems = comboItems;
            comboBox = new JComboBox(comboItems.toArray()){
                @Override public Dimension getPreferredSize() {
                    Dimension d = super.getPreferredSize();
                    return new Dimension(140, d.height-5);
                }
            };
            setOpaque(true);
            add(comboBox);
        }
        public ComboBoxPanel(ArrayList comboItems, final int width) {

            this.comboItems = comboItems;
            comboBox = new JComboBox(comboItems.toArray()){
                @Override public Dimension getPreferredSize() {
                    Dimension d = super.getPreferredSize();
                    return new Dimension(width, d.height-5);
                }
            };
            setOpaque(true);
            add(comboBox);
        }
    }
    class ComboBoxCellRenderer extends ComboBoxPanel implements TableCellRenderer {
        public ComboBoxCellRenderer(ArrayList comboItems) {
            super(comboItems);
        }
        public ComboBoxCellRenderer(ArrayList comboItems,int width) {
            super(comboItems,width);
        }
        @Override public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setBackground(isSelected?table.getSelectionBackground():table.getBackground());
            if(value!=null) {
                comboBox.setSelectedItem(value);
            }
            return this;
        }
    }
    class ComboBoxCellEditor   extends ComboBoxPanel implements TableCellEditor {
        public ComboBoxCellEditor(ArrayList comboItems) {
            super(comboItems);
            comboBox.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) {
                    fireEditingStopped();
                }
            });
        }
        public ComboBoxCellEditor(ArrayList comboItems,int width) {
            super(comboItems,width);
            comboBox.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) {
                    fireEditingStopped();
                }
            });
        }
        @Override public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.setBackground(table.getSelectionBackground());
            comboBox.setSelectedItem(value);
            return this;
        }

        //Copid from DefaultCellEditor.EditorDelegate
        @Override public Object getCellEditorValue() {
            return comboBox.getSelectedItem();
        }
        @Override public boolean shouldSelectCell(EventObject anEvent) {
            if(anEvent instanceof MouseEvent) {
                MouseEvent e = (MouseEvent)anEvent;
                return e.getID() != MouseEvent.MOUSE_DRAGGED;
            }
            return true;
        }
        @Override public boolean stopCellEditing() {
            if(comboBox.isEditable()) {
                comboBox.actionPerformed(new ActionEvent(this, 0, ""));
            }
            fireEditingStopped();
            return true;
        }

        //Copid from AbstractCellEditor
        //protected EventListenerList listenerList = new EventListenerList();
        transient protected ChangeEvent changeEvent = null;

        @Override public boolean isCellEditable(EventObject e) {
            return true;
        }
        @Override public void  cancelCellEditing() {
            fireEditingCanceled();
        }
        @Override public void addCellEditorListener(CellEditorListener l) {
            listenerList.add(CellEditorListener.class, l);
        }
        @Override public void removeCellEditorListener(CellEditorListener l) {
            listenerList.remove(CellEditorListener.class, l);
        }
        public CellEditorListener[] getCellEditorListeners() {
            return listenerList.getListeners(CellEditorListener.class);
        }
        protected void fireEditingStopped() {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for(int i = listeners.length-2; i>=0; i-=2) {
                if(listeners[i]==CellEditorListener.class) {
                    // Lazily create the event:
                    if(changeEvent == null) changeEvent = new ChangeEvent(this);
                    ((CellEditorListener)listeners[i+1]).editingStopped(changeEvent);
                }
            }
        }
        protected void fireEditingCanceled() {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for(int i = listeners.length-2; i>=0; i-=2) {
                if(listeners[i]==CellEditorListener.class) {
                    // Lazily create the event:
                    if(changeEvent == null) changeEvent = new ChangeEvent(this);
                    ((CellEditorListener)listeners[i+1]).editingCanceled(changeEvent);
                }
            }
        }
    }

}
