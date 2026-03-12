package org.example.backendpractice.controller;

import lombok.RequiredArgsConstructor;
import org.example.backendpractice.controller.dto.AuthPasswordChangeRequest;
import org.example.backendpractice.controller.dto.MemberResponse;
import org.example.backendpractice.entity.Member;
import org.example.backendpractice.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {

        private final MemberService memberService;

        @GetMapping("/{memberId}")
        public MemberResponse findMember(@PathVariable Long memberId) {

            return memberService.findById(memberId);
        }

        @GetMapping
        public List<MemberResponse> findAllMembers() {
            return memberService.findAllMembers();
        }

        @GetMapping("/{memberId}/password")
        public void changePassword(@PathVariable Long memberId, @RequestBody AuthPasswordChangeRequest request) {
            memberService.changePassword(memberId, request);
        }
}
