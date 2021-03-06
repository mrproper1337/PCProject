package pojo;

import javax.persistence.*;

@Entity
@Table( name = "sport_n_name")

public class SportNormName {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name = "sportNormNameId")
    private int sportNormNameId;

    @Column(name = "sportNormName")
    private String sportNormName;

    @Column(name = "markMode")
    private int markMode;

    public int getSportNormNameId() {
        return sportNormNameId;
    }

    public void setSportNormNameId(int sportNormNameId) {
        this.sportNormNameId = sportNormNameId;
    }

    public String getSportNormName() {
        return sportNormName;
    }

    public void setSportNormName(String sportNormName) {
        this.sportNormName = sportNormName;
    }

    public int getMarkMode() {
        return markMode;
    }

    public void setMarkMode(int markMode) {
        this.markMode = markMode;
    }
}
