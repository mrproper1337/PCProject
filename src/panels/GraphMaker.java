package panels;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pojo.*;
import tableModels.ConnectHibernate;
import tableModels.SNNModel;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public class GraphMaker {
    private String results,marks;
    private JPanel panel;
    private JLabel label1,label2;
    private Object lastObject;
    public GraphMaker(JPanel panel,JLabel l1,JLabel l2){
        this.panel = panel;
        label1 = l1;
        label2 = l2;
    }

    public void resetStats(){
        if(lastObject!=null)
            setStats(lastObject);
    }

    public <T>void setStats(T item){
        lastObject = item;
        panel.removeAll();
        if(item instanceof Group)
            panel.add(makeStatGraph((Group) item));
        else
            panel.add(makeStatGraph((Student) item));
        label1.setText(getResults());
        label2.setText(getMarks());
        panel.revalidate();
    }
    private int getMark(SportNorm sn,double result){
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
    private ChartPanel makeStatGraph(Group currGroup){
        double[] averageResults = new double[4];
        List currStGr = ConnectHibernate.loadTable("from Student where(" +
                "groupId = " + currGroup.getGroupId() +
                " and " +
                "gender =" +ReadPanel.comboBox2.getSelectedIndex() +
                " and " +
                "healthGroup = " +(ReadPanel.checkBox1.isSelected()?1:0)+
                ")");

        List currSP = ConnectHibernate.loadTable("from SportNorm where " +
                "(sportNormNameId =" + SNNModel.snnList.get(ReadPanel.comboBox3.getSelectedIndex()).getSportNormNameId() +
                " and " +
                "genderNorm =" +ReadPanel.comboBox2.getSelectedIndex() +
                " and " +
                "healthGroupNorm =" +(ReadPanel.checkBox1.isSelected()?1:0)+
                ")");
        String studQr = "",spQr = "";
        int iii;
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
            for(Result res:(List<Result>)ConnectHibernate.loadTable("from Result where " +
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
        SportNormName spncurr = SNNModel.snnList.get(ReadPanel.comboBox3.getSelectedIndex());
        results = "Середні результати по курсам : ";
        marks = "Середні оцінки по курсам : ";
        for(int i = 0;i<4;i++){
            if(averageResults[i]!=0)
                groupSeries.add(i+1,spncurr.getMarkMode()==0?averageResults[i]*-1:averageResults[i]);
            SportNorm resNorm = null;
            for(SportNorm sp:(List<SportNorm>)currSP)
                if(sp.getCourseNorm() == (i+1))
                    resNorm = sp;
            results += "  "+(i+1)+"-й : "+(averageResults[i]==0?"н/а":averageResults[i]);
            marks +=   "  "+(averageResults[i]==0?"н/а":getMark(resNorm,averageResults[i]));
        }

        XYSeriesCollection data = new XYSeriesCollection(groupSeries);
        ChartPanel cp = new ChartPanel(createChart( data, "Середні показники " + currGroup.getGroupName()));
        cp.setPreferredSize(new java.awt.Dimension(500, 270));
        return cp;
    }
    private ChartPanel makeStatGraph(Student currStudent){
        double[] normResults = new double[4];
        List currSP = ConnectHibernate.loadTable("from SportNorm where " +
                "(sportNormNameId =" + SNNModel.snnList.get(ReadPanel.comboBox3.getSelectedIndex()).getSportNormNameId() +
                " and " +
                "genderNorm =" +ReadPanel.comboBox2.getSelectedIndex() +
                " and " +
                "healthGroupNorm =" +(ReadPanel.checkBox1.isSelected()?1:0)+
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
            for(Result res:(List<Result>)ConnectHibernate.loadTable("from Result where studentId ="+currStudent.getStudentId()+" and( "+spQr+" )")){
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
        SportNormName spncurr = SNNModel.snnList.get(ReadPanel.comboBox3.getSelectedIndex());
        results = "Результати по курсам : ";
        marks = "Оцінки по курсам : ";
        for(int i = 0;i<4;i++){
            if(normResults[i]!=0)
                studSeries.add((i+1), spncurr.getMarkMode() == 0 ? normResults[i] * -1 : normResults[i]);
            SportNorm resNorm = null;
            for(SportNorm sp:(List<SportNorm>)currSP)
                if(sp.getCourseNorm() == (i+1))
                    resNorm = sp;
            results += "  "+(i+1)+"-й : "+(normResults[i]==0?"н/а":normResults[i]);
            marks +=   "  "+(normResults[i]==0?"н/а":getMark(resNorm,normResults[i]));
        }


        XYDataset data = new XYSeriesCollection(studSeries);
        ChartPanel cp = new ChartPanel(createChart((XYSeriesCollection) data, "Показники : " + currStudent.getName()));
        cp.setPreferredSize(new java.awt.Dimension(500, 270));
        return cp;
    }
    private JFreeChart createChart(XYSeriesCollection data, String func) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                func, //Заголовок діаграми
                "Курс",  //назва осі X
                "Результат",  //назва осі Y
                data, //дані
                PlotOrientation.VERTICAL, //орієнтація
                true, // включити легенду
                true, //підказки
                false // urls
        );


        return chart;
    }
    private String getResults() {
        return results;
    }
    private String getMarks() {
        return marks;
    }
}
