package me.chiqors.springbooks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToMany;

import java.util.Date;
import java.util.List;

/**
 * Represents a member entity.
 */
@Getter @Setter
@Entity
@NoArgsConstructor
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
    private Date registeredAt;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "member_code")
    private String memberCode;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    // -------------- Out Relationships --------------

    // JSON Ignore to prevent infinite recursion
    @OneToMany(mappedBy = "member")
    private List<Transaction> transactions;

    // -------------- Methods --------------

    public Member(String name, String email, String phone, Date registeredAt, String memberCode, Date updatedAt) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.registeredAt = registeredAt;
        this.memberCode = memberCode;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", registeredAt=" + registeredAt +
                ", deleted=" + deleted +
                ", memberCode='" + memberCode + '\'' +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
