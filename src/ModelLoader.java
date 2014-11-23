import pojo.*;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ModelLoader {

    ConnectHibernate ch;
    MainForm form;
    List<Integer> groupsId,snId;
    DefaultTableModel currentModel;
    String lastQuery,currentQuery;

    ModelLoader(MainForm form, ConnectHibernate ch){
        this.form = form;
        this.ch = ch;
        lastQuery = "";
        currentModel=null;
        groupsId=new ArrayList<>();
        snId=new ArrayList<>();
    }

    public DefaultTableModel setGroupModels(final boolean editable){
        currentQuery="from Group";
        if(!lastQuery.equals(currentQuery)){
            System.out.println("setGroupModels");
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
            currentModel = groups_tm;
            return groups_tm;
        }
        else return currentModel;
    }
    public DefaultTableModel setStudentModels(final boolean editable){
        int check;
        check = form.checkBox1.isSelected() ? 1 : 0;
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
                            else{
                                JComboBox cb = new JComboBox();
                                cb.addItem("хлопець");
                                cb.addItem("дівчина");
                                cb.setSelectedIndex(res.getGender());
                            }
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
            currentModel = students_tm;
            return  students_tm;
        }
        else return currentModel;
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
