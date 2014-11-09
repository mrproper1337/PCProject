package pojo;

import javax.persistence.*;
import javax.persistence.Table;


@Entity
@Table ( name = "student" )
public class Student {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name = "studentId")
    private int studentId;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private int gender;

    @Column(name = "healthGroup")
    private int healthGroup;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group groupId;

    public Student(){}

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getHealthGroup() {
        return healthGroup;
    }

    public void setHealthGroup(int healthGroup) {
        this.healthGroup = healthGroup;
    }

    public Group getGroupId() {
        return groupId;
    }

    public void setGroupId(Group groupId) {
        this.groupId = groupId;
    }
}
