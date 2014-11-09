package pojo;

import javax.persistence.*;

@Entity
@Table( name = "st_group" )

public class Group {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name = "groupId")
    private int groupId;

    @Column(name = "groupName")
    private String groupName;

    public Group(){}

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
