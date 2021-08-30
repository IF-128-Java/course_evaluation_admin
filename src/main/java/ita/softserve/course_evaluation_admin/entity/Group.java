package ita.softserve.course_evaluation_admin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"students", "courses", "chatRoom"})
@ToString(exclude = {"students", "courses", "chatRoom"})
@Builder
@Table(name = "groups")
public class Group {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @OneToMany(mappedBy = "group")
    private List<User> students = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "course_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;
}
