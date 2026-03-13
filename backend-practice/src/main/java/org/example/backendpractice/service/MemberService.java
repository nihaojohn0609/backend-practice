package org.example.backendpractice.service;

import org.example.backendpractice.controller.dto.AuthPasswordChangeRequest;
import org.example.backendpractice.controller.dto.MemberResponse;
import org.example.backendpractice.controller.dto.MemberUpdateRequest;

import java.util.List;

public interface MemberService {

    List<MemberResponse> findAllMembers();

    MemberResponse findById(Long memberId);

    MemberResponse updateMember(Long memberId, String loginId, MemberUpdateRequest request);

    void deleteMember(Long memberId, String LoginId);

}
