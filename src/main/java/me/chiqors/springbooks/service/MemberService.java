package me.chiqors.springbooks.service;

import me.chiqors.springbooks.dto.MemberDTO;
import me.chiqors.springbooks.model.Member;
import me.chiqors.springbooks.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    // ------------------- CONSTRUCTOR -------------------

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // ------------------- CONVERTER -------------------

    public MemberDTO convertToDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        // memberDTO.setId(member.getId());
        memberDTO.setName(member.getName());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setPhone(member.getPhone());
        memberDTO.setRegisteredAt(member.getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        memberDTO.setDeleted(member.isDeleted());
        memberDTO.setMemberCode(member.getMemberCode());
        return memberDTO;
    }

    public Member convertToEntity(MemberDTO memberDTO) {
        Member member = new Member();
        // member.setId(memberDTO.getId());
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setPhone(memberDTO.getPhone());
        member.setRegisteredAt(LocalDate.parse(memberDTO.getRegisteredAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        member.setDeleted(memberDTO.isDeleted());
        member.setMemberCode(memberDTO.getMemberCode());
        return member;
    }

    // ------------------- CRUD -------------------

    public List<MemberDTO> getAllMembers(String name) {
        List<Member> members;

        if (name != null) {
            members = memberRepository.findByNameContainingAndDeletedIsFalse(name);
        } else {
            members = memberRepository.findAllMembersByDeletedIsFalse();
        }

        return members.stream()
                .sorted(Comparator.comparing(Member::getName))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MemberDTO getMemberById(long id) {
        Member member = memberRepository.findByIdAndDeletedIsFalse(id);
        if (member != null) {
            return convertToDTO(member);
        }

        return null;
    }

    public MemberDTO getMemberByCode(String memberCode) {
        Member member = memberRepository.findByMemberCodeAndDeletedIsFalse(memberCode);
        if (member != null) {
            return convertToDTO(member);
        }

        return null;
    }

    public MemberDTO addMember(MemberDTO memberDTO) {
        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setPhone(memberDTO.getPhone());
        member.setRegisteredAt(LocalDate.now());
        member.setDeleted(false);
        // generate member code, format: M<day><month><year><id>
        String memberCode = "M" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) + member.getId();
        member.setMemberCode(memberCode);

        memberRepository.save(member);

        return convertToDTO(member);
    }

    public MemberDTO updateMember(String memberCode, MemberDTO memberDTO) {
        Member member = memberRepository.findByMemberCodeAndDeletedIsFalse(memberCode);
        if (member != null) {
            member.setName(memberDTO.getName());
            member.setEmail(memberDTO.getEmail());
            member.setPhone(memberDTO.getPhone());

            memberRepository.save(member);

            return convertToDTO(member);
        }

        return null;
    }

    public boolean deleteMember(String memberCode) {
        Member member = memberRepository.findByMemberCodeAndDeletedIsFalse(memberCode);
        if (member != null) {
            member.setDeleted(true);

            memberRepository.save(member);

            return true;
        }

        return false;
    }
}
