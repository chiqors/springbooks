package me.chiqors.springbooks.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "registered_at")
    private String registeredAt;

    // -------------- Out Relationships --------------

    // JSON Ignore to prevent infinite recursion
    @OneToMany(mappedBy = "member")
    private List<Transaction> transactions;

    // -------------- Methods --------------

    // Default Constructor for JPA
    public Member() {}

    @Override
    public String toString() {
        String content = "Member [";
        content += "id=" + id + ", ";
        content += "name=" + name + ", ";
        content += "email=" + email + ", ";
        content += "phone=" + phone + ", ";
        content += "registeredAt=" + registeredAt + "]";
        return content;
    }
}
