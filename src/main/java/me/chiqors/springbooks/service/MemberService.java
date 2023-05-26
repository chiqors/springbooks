package me.chiqors.springbooks.service;

import me.chiqors.springbooks.dto.MemberDTO;
import me.chiqors.springbooks.model.Member;
import me.chiqors.springbooks.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberDTO convertToDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setName(member.getName());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setPhone(member.getPhone());
        memberDTO.setRegisteredAt(member.getRegisteredAt());

        return memberDTO;
    }

    public List<MemberDTO> getAllMembers(String name, String sort, int page, int size) {
        List<Member> members;

        if (name != null) {
            members = memberRepository.findByNameContainingAndDeletedIsFalse(name);
        } else {
            members = memberRepository.findAllMembersByDeletedIsFalse();
        }

        if (sort != null) {
            if (sort.equals("asc")) {
                members.sort(Comparator.comparing(Member::getName));
            } else if (sort.equals("desc")) {
                members.sort(Comparator.comparing(Member::getName).reversed());
            }
        }

        if (page >= 0 && size > 0) {
            members = memberRepository.findAll(PageRequest.of(page, size)).toList();
        }

        return members.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public MemberDTO getMemberById(long id) {
        Member member = memberRepository.findByIdAndDeletedIsFalse(id);
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
        member.setRegisteredAt(memberDTO.getRegisteredAt());
        member.setDeleted(false);

        memberRepository.save(member);

        return convertToDTO(member);
    }

    public MemberDTO updateMember(long id, MemberDTO memberDTO) {
        Member member = memberRepository.findById(id).orElse(null);
        if (member != null) {
            member.setName(memberDTO.getName());
            member.setEmail(memberDTO.getEmail());
            member.setPhone(memberDTO.getPhone());

            memberRepository.save(member);

            return convertToDTO(member);
        }

        return null;
    }

    public boolean deleteMember(long id) {
        Member member = memberRepository.findById(id).orElse(null);
        if (member != null) {
            member.setDeleted(true);

            memberRepository.save(member);

            return true;
        }

        return false;
    }
}
