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
}
