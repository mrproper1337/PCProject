package tableModels;

import panels.WritePanel;
import pojo.Result;
import pojo.SportNorm;
import pojo.Student;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ResultModel extends DefaultTableModel {
    static List<Result> resultList = ConnectHibernate.loadTable("from Result where studentId = "+StudentModel.studentList.get(0).getStudentId());
    private int studentId;
    public ResultModel(int studentId){
        this.studentId = studentId;
        resultList = ConnectHibernate.loadTable("from Result where studentId = "+studentId);
    }

    @Override
    public int getRowCount() {
        return resultList.size()+1;
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
    public Object getValueAt(int row, int column) {
        if(resultList.size() == row)return "";
        switch(column){
            case 0:
                return resultList.get(row).getSportNormId().getSportNormNameId().getSportNormName();
            case 1:
                return String.valueOf(resultList.get(row).getSportNormId().getCourseNorm());
            case 2:
                return resultList.get(row).getResult();
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        switch(CellUtilities.getActionId(row == resultList.size(), aValue.toString().equals(""))){
            case 0:
                addRow(aValue,column);
                break;
            case 1:
                deleteRow(row);
                break;
            case 2:
                updateRow(aValue,row,column);
                break;
        }
    }

    private void addRow(Object aValue,int column){
        Result obj = new Result();
        Student st = StudentModel.studentList.get(studentId);
        obj.setStudentId(st);
        switch(column){
            case 0:
                for(Object o:ConnectHibernate.loadTable("from SportNorm where ( " +
                        "courseNorm = 1"+
                        " and " +
                        "genderNorm ="+st.getGender() +
                        " and " +
                        "healthGroupNorm ="+st.getHealthGroup() +
                        ")")){
                    SportNorm sportNorm = (SportNorm)o;
                    if(sportNorm.getSportNormNameId().getSportNormName().equals(aValue.toString()))
                        obj.setSportNormId(sportNorm);
                }
                obj.setResult(1);
                break;
            case 1:
                SportNorm ssd = (SportNorm)ConnectHibernate.loadTable("from SportNorm where ( " +
                        "courseNorm =" + Integer.parseInt(aValue.toString()) +
                        " and " +
                        "genderNorm =" + st.getGender() +
                        " and " +
                        "healthGroupNorm =" + st.getGender() +
                        ")").get(0);
                obj.setSportNormId(ssd);
                obj.setResult(1);
            case 2:
                SportNorm ssdd = (SportNorm)ConnectHibernate.loadTable("from SportNorm where ( " +
                        "courseNorm = 1" +
                        " and " +
                        "genderNorm =" + st.getGender() +
                        " and " +
                        "healthGroupNorm =" + st.getGender() +
                        ")").get(0);
                obj.setSportNormId(ssdd);
                obj.setResult(Double.parseDouble(aValue.toString()));
        }
        ConnectHibernate.addToTable(obj);
        WritePanel.updateModel();
    }
    private void deleteRow(int row){
        Result obj = resultList.get(row);
        ConnectHibernate.deleteFromTable(obj);
        WritePanel.updateModel();
    }
    private void updateRow(Object aValue, int row, int column){
        Result obj = resultList.get(row);
        Student st = StudentModel.studentList.get(studentId);
        switch(column){
            case 0:
                for(Object o:ConnectHibernate.loadTable("from SportNorm where ( " +
                        "courseNorm = 1"+
                        " and " +
                        "genderNorm ="+st.getGender() +
                        " and " +
                        "healthGroupNorm ="+st.getGender() +
                        ")")){
                    SportNorm sportNorm = (SportNorm)o;
                    if(sportNorm.getSportNormNameId().getSportNormName().equals(aValue.toString()))
                        obj.setSportNormId(sportNorm);
                }
                break;
            case 1:
                SportNorm ssd = (SportNorm)ConnectHibernate.loadTable("from SportNorm where ( " +
                        "courseNorm =" + Integer.parseInt(aValue.toString()) +
                        " and " +
                        "genderNorm =" + st.getGender() +
                        " and " +
                        "healthGroupNorm =" + st.getGender() +
                        ")").get(0);
                obj.setSportNormId(ssd);
            case 2:
                obj.setResult(Double.parseDouble(aValue.toString()));
        }
        ConnectHibernate.updateInTable(obj);
        WritePanel.updateModel();
    }

}
