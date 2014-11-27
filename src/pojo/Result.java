package pojo;

import javax.persistence.*;
import javax.persistence.Table;

@Entity
@Table(name = "result")
public class Result{


    @Id @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name = "resultId")
    private int resultId;

    @ManyToOne
    @JoinColumn(name = "sportNormId")
    private SportNorm sportNormId;

    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student studentId;

    @Column (name = "result")
    private double result;

    public Result(){}

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public SportNorm getSportNormId() {
        return sportNormId;
    }

    public void setSportNormId(SportNorm sportNormId) {
        this.sportNormId = sportNormId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
