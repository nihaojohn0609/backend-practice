package org.example.backendpractice.service;

import org.example.backendpractice.controller.dto.AuthPasswordChangeRequest;
import org.example.backendpractice.controller.dto.MemberResponse;

import java.util.List;

public interface MemberService {

    List<MemberResponse> findAllMembers();

    MemberResponse findById(Long memberId);

    void changePassword(Long id, AuthPasswordChangeRequest request);

}
