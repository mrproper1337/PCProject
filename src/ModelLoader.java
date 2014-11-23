import pojo.*;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.List;

public class ModelLoader {

    ConnectHibernate ch;
    MainForm form;
    List<Integer> groupsId,snId;
    String lastQuery,currentQuery;

    ModelLoader(MainForm form, ConnectHibernate ch){
        this.form = form;
        this.ch = ch;
        lastQuery = "";
        groupsId=new ArrayList<>();
        snId=new ArrayList<>();
    }

    public void getGroupModel(final boolean editable){
        currentQuery="from Group";
        if(!lastQuery.equals(currentQuery)){
            System.out.println("getGroupModel");
            DefaultTableModel groups_tm;
            lastQuery = currentQuery;
            final List groupList=ch.loadTable(currentQuery);
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
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return editable && columnIndex == 1;
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

                @Override
                public void setValueAt(Object aValue, int row, int column) {
                    Group obj =(Group)groupList.get(row);
                    obj.setGroupName(aValue.toString());
                    ch.updateInTable(obj);
                }
            };
            if(editable)form.table2.setModel(groups_tm);
            else form.table1.setModel(groups_tm);
        }
    }
    public void getStudentModel(final boolean editable){
        int check = form.checkBox1.isSelected() ? 1 : 0;
        if(!editable){
            currentQuery = "from Student where(" +
                    "groupId = "+groupsId.get(form.comboBox1.getSelectedIndex()-1)+
                    " and " +
                    "gender ="+form.comboBox2.getSelectedIndex()+
                    " and " +
                    "healthGroup = "+check+
                    ")";
        }
        else currentQuery = "from Student";
        if(!lastQuery.equals(currentQuery)){
            System.out.println("setStudModels");
            DefaultTableModel students_tm;
            lastQuery = currentQuery;
            final List studentList=ch.loadTable(currentQuery);
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
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnIndex!=0 && editable;
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
                            if(!editable){
                                if(res.getGender()==0)
                                    return "хлопець";
                                else
                                    return "дівчина";
                            }
                            else break;
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

                @Override
                public void setValueAt(Object aValue, int row, int column) {
                    Student obj =(Student)studentList.get(row);
                    switch(column){
                        case 1:
                            obj.setName(aValue.toString());
                            break;
                        case 2:
                    }
                    ch.updateInTable(obj);
                }
            };
            if(editable){
                form.table2.setModel(students_tm);
                TableColumn genderColumn = form.table2.getColumnModel().getColumn(2);
                JComboBox genders = new JComboBox();
                genders.addItem("хлопець");
                genders.addItem("дівчина");
                genderColumn.setCellEditor(new DefaultCellEditor(genders));

            }
            else form.table1.setModel(students_tm);
        }
    }
    public void setGroupCombo(){
        final List groupList=ch.loadTable("from Group");
        form.listenerIsStopped=true;
        groupsId.clear();
        form.comboBox1.removeAllItems();
        form.comboBox1.addItem("По групам");
        for(Group a:(List<pojo.Group>)groupList){
            form.comboBox1.addItem(a.getGroupName());
            groupsId.add(a.getGroupId());
        }
        form.listenerIsStopped=false;
    }
    public void setSNCombo(){
        snId.clear();
        form.comboBox3.removeAllItems();
        System.out.println("setSNModels");
        List snList=ch.loadTable("from SportNormName");
        for(SportNormName a:(List<SportNormName>)snList){
            form.comboBox3.addItem(a.getSportNormName());
            snId.add(a.getSportNormNameId());
        }
    }

}
