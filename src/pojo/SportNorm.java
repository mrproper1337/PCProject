package pojo;

import javax.persistence.*;

@Entity
@Table( name = "sport_n" )

public class SportNorm {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name = "sportNormId")
    private int sportNormId;

    @Column(name = "sportNormName")
    private String sportNormName;

    @Column(name = "courseNorm")
    private int courseNorm;

    @Column(name = "genderNorm")
    private int genderNorm;

    @Column(name = "healthGroupNorm")
    private int healthGroupNorm;

    @Column(name = "exellentMark")
    private double exellentMark;

    @Column(name = "goodMark")
    private double goodMark;

    @Column(name = "satisfactorilyMark")
    private double satisfactorilyMark;


    public SportNorm(){}

    public int getSportNormId() {
        return sportNormId;
    }

    public void setSportNormId(int sportNormId) {
        this.sportNormId = sportNormId;
    }

    public String getSportNormName() {
        return sportNormName;
    }

    public void setSportNormName(String sportNormName) {
        this.sportNormName = sportNormName;
    }

    public int getCourseNorm() {
        return courseNorm;
    }

    public void setCourseNorm(int courseNorm) {
        this.courseNorm = courseNorm;
    }

    public int getGenderNorm() {
        return genderNorm;
    }

    public void setGenderNorm(int genderNorm) {
        this.genderNorm = genderNorm;
    }

    public int getHealthGroupNorm() {
        return healthGroupNorm;
    }

    public void setHealthGroupNorm(int healthGroupNorm) {
        this.healthGroupNorm = healthGroupNorm;
    }

    public double getExellentMark() {
        return exellentMark;
    }

    public void setExellentMark(double exellentMark) {
        this.exellentMark = exellentMark;
    }

    public double getGoodMark() {
        return goodMark;
    }

    public void setGoodMark(double goodMark) {
        this.goodMark = goodMark;
    }

    public double getSatisfactorilyMark() {
        return satisfactorilyMark;
    }

    public void setSatisfactorilyMark(double satisfactorilyMark) {
        this.satisfactorilyMark = satisfactorilyMark;
    }
}
