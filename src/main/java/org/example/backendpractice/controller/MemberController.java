package org.example.backendpractice.controller;

import lombok.RequiredArgsConstructor;
import org.example.backendpractice.controller.dto.AuthPasswordChangeRequest;
import org.example.backendpractice.controller.dto.MemberResponse;
import org.example.backendpractice.controller.dto.MemberUpdateRequest;
import org.example.backendpractice.entity.Member;
import org.example.backendpractice.service.MemberService;
import org.springframework.security.core.Authentication;
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

        @PutMapping("/{memberId}")
        public MemberResponse updateMember(Authentication authentication, @PathVariable Long memberId, @RequestBody MemberUpdateRequest request) {
            String loginId = authentication.getName();
            return memberService.updateMember(memberId, loginId, request);
        }

        @DeleteMapping("/{memberId}")
        public void deleteMember(Authentication authentication, @PathVariable Long memberId) {
            String loginId = authentication.getName();
            memberService.deleteMember(memberId, loginId);
        }
}