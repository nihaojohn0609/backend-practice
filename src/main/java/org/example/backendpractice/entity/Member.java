package org.example.backendpractice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.backendpractice.controller.dto.MemberUpdateRequest;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"posts", "comments"})

public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="member_id")
    private Long memberId;
    private String loginId;
    private String memberName;
    private String password;

    @Builder
    public Member(String loginId, String memberName, String password) {
        this.loginId = loginId;
        this.memberName = memberName;
        this.password = password;
    }

    public void updateMemberInfo(String memberName, String password) {
        if(memberName != null && !memberName.isBlank()) {
            this.memberName = memberName;
        }
        if(password != null && !password.isBlank()) {
            this.password = password;
        }
    }

    // Member 삭제되도 post는 유지, data는 필요할 때가 되면 가져옴
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @PreRemove // DB에서 Member가 삭제되기 직전(@PreRemove)에 실행되는 메서드
    private void preRemove() {
        // 내가 쓴 모든 게시글을 찾아가서, 작성자(user)를 null로 만들어 연결을 끊어버립니다.
        for (Post post : posts) {
            post.setMember(null);
        }
        for (Comment comment : comments) {
            comment.setMember(null);
        }
    }
}