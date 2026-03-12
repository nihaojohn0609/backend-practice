package org.example.backendpractice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "posts")

public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="member_id")
    private Long memberId;
    private String memberName;
    private String password;

    @Builder
    public Member(String memberName, String password) {
        this.memberName = memberName;
        this.password = password;
    }

    public void changePassword(String oldPassword, String newPassword) {
        if(!password.equals(oldPassword)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        this.password = newPassword;
    }

    // User 삭제되도 post는 유지, data는 필요할 때가 되면 가져옴
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<Post> posts = new ArrayList<>();
}