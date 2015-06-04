package tableModels;

import panels.WritePanel;
import pojo.Result;
import pojo.SportNorm;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ResultModel extends DefaultTableModel {
    static List<Result> resultList = ConnectHibernate.loadTable("from Result where studentId = "+StudentModel.studentList.get(0).getStudentId());
    public ResultModel(int studentId){
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
        SportNorm sn = resultList.get(0).getSportNormId();
        switch(column){
            case 0:
                for(Object o:ConnectHibernate.loadTable("from SportNorm where ( " +
                        "courseNorm = 1"+
                        " and " +
                        "genderNorm ="+sn.getGenderNorm() +
                        " and " +
                        "healthGroupNorm ="+sn.getHealthGroupNorm() +
                        ")")){
                    SportNorm sportNorm = (SportNorm)o;
                    if(sportNorm.getSportNormNameId().getSportNormName().equals(aValue.toString()))
                        obj.setSportNormId(sportNorm);
                }
                obj.setStudentId(resultList.get(0).getStudentId());
                obj.setResult(1);
                break;
            case 1:
                for(Object o:ConnectHibernate.loadTable("from SportNorm where ( " +
                        "courseNorm ="+Integer.parseInt(aValue.toString())+
                        " and " +
                        "genderNorm ="+sn.getGenderNorm() +
                        " and " +
                        "healthGroupNorm ="+sn.getHealthGroupNorm() +
                        ")")){
                    SportNorm sportNorm = (SportNorm)o;
                    if(sportNorm.getSportNormNameId().equals(sn.getSportNormNameId()))
                        obj.setSportNormId(sportNorm);
                }
                obj.setStudentId(resultList.get(0).getStudentId());
                obj.setResult(1);
            case 2:
                for(Object o:ConnectHibernate.loadTable("from SportNorm where ( " +
                        "courseNorm = 1"+
                        " and " +
                        "genderNorm ="+sn.getGenderNorm() +
                        " and " +
                        "healthGroupNorm ="+sn.getHealthGroupNorm() +
                        ")")){
                    SportNorm sportNorm = (SportNorm)o;
                    if(sportNorm.getSportNormNameId().equals(sn.getSportNormNameId()))
                        obj.setSportNormId(sportNorm);
                }
                obj.setStudentId(resultList.get(0).getStudentId());
                obj.setResult(Double.parseDouble(aValue.toString()));
        }
        WritePanel.updateModel();
    }
    private void deleteRow(int row){
        Result obj = resultList.get(row);
        ConnectHibernate.deleteFromTable(obj);
        WritePanel.updateModel();
    }
    private void updateRow(Object aValue, int row, int column){
        Result obj = resultList.get(row);
        SportNorm sn = resultList.get(row).getSportNormId();
        switch (column){
            case 0:
                for(Object o:ConnectHibernate.loadTable("from SportNorm where ( " +
                        "courseNorm ="+sn.getCourseNorm()+
                        " and " +
                        "genderNorm ="+sn.getGenderNorm() +
                        " and " +
                        "healthGroupNorm ="+sn.getHealthGroupNorm() +
                        ")")){
                    SportNorm sportNorm = (SportNorm)o;
                    if(sportNorm.getSportNormNameId().getSportNormName().equals(aValue.toString()))
                        obj.setSportNormId(sportNorm);
                }
                break;
            case 1:
                for(Object o:ConnectHibernate.loadTable("from SportNorm where ( " +
                        "courseNorm ="+Integer.parseInt(aValue.toString())+
                        " and " +
                        "genderNorm ="+sn.getGenderNorm() +
                        " and " +
                        "healthGroupNorm ="+sn.getHealthGroupNorm() +
                        ")")){
                    SportNorm sportNorm = (SportNorm)o;
                    if(sportNorm.getSportNormNameId().equals(sn.getSportNormNameId()))
                        obj.setSportNormId(sportNorm);
                }
                break;
            case 2:
                obj.setResult(Integer.parseInt(aValue.toString()));
        }
        ConnectHibernate.updateInTable(obj);
        WritePanel.updateModel();
    }

}
