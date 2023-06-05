package me.chiqors.springbooks.service;

import me.chiqors.springbooks.dto.MemberDTO;
import me.chiqors.springbooks.model.Member;
import me.chiqors.springbooks.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Service class for handling Member-related operations.
 */
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    /**
     * Converts a Member entity to a MemberDTO.
     *
     * @param member the Member entity
     * @return the corresponding MemberDTO
     */
    public MemberDTO convertToDTO(Member member) {
        String registeredAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(member.getRegisteredAt());
        String updatedAt = member.getUpdatedAt() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(member.getUpdatedAt()) : null;

        return new MemberDTO(member.getName(), member.getEmail(), member.getPhone(), registeredAt, member.getMemberCode(), updatedAt);
    }

    /**
     * Retrieves all members with optional name filtering.
     *
     * @param name the name filter (optional)
     * @param page the page number (optional)
     * @param size the page size (optional)
     * @return the list of MemberDTOs
     */
    public Page<MemberDTO> getAllMembers(String name, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Member> memberPage;

        if (name != null) {
            memberPage = memberRepository.findByNameContainingIgnoreCaseAndDeletedIsFalseOrderByRegisteredAtDesc(name, pageable);
        } else {
            memberPage = memberRepository.findByDeletedIsFalseOrderByRegisteredAtDesc(pageable);
        }

        return memberPage.map(this::convertToDTO);
    }

    /**
     * Retrieves a member by its member code.
     *
     * @param memberCode the member code
     * @return the corresponding MemberDTO
     */
    public MemberDTO getMemberByCode(String memberCode) {
        Member member = memberRepository.findByMemberCodeAndDeletedIsFalse(memberCode);
        if (member != null) {
            return convertToDTO(member);
        }

        return null;
    }

    /**
     * Checks if a member code is valid.
     *
     * @param memberCode the member code
     * @return true if the member code is valid, false otherwise
     */
    public boolean isValidMemberCode(String memberCode) {
        Member member = memberRepository.findByMemberCodeAndDeletedIsFalse(memberCode);
        return member != null;
    }

    /**
     * Adds a new member.
     *
     * @param memberDTO the MemberDTO
     * @return the created MemberDTO
     */
    public MemberDTO addMember(MemberDTO memberDTO) {
        // generate member code, format: M<day><month><year><hour><minute><second><A-Z>
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyHHmmss");
        String generateChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String memberCode = "M" + formatter.format(memberDTO.getRegisteredAt()) + generateChar.charAt((int) (Math.random() * generateChar.length()));

        Date registeredAt = new Date();

        Member member = new Member(memberDTO.getName(), memberDTO.getEmail(), memberDTO.getPhone(), registeredAt, memberCode, null);

        memberRepository.save(member);

        return convertToDTO(member);
    }

    /**
     * Updates a member by its member code.
     *
     * @param memberDTO  the updated MemberDTO
     * @return the updated MemberDTO
     */
    public MemberDTO updateMember(MemberDTO memberDTO) {
        Member member = memberRepository.findByMemberCodeAndDeletedIsFalse(memberDTO.getMemberCode());
        if (member != null) {
            if (memberDTO.getName() != null) {
                member.setName(memberDTO.getName());
            }
            if (memberDTO.getEmail() != null) {
                member.setEmail(memberDTO.getEmail());
            }
            if (memberDTO.getPhone() != null) {
                member.setPhone(memberDTO.getPhone());
            }

            member.setUpdatedAt(new Date());

            memberRepository.save(member);

            return convertToDTO(member);
        }

        return null;
    }

    /**
     * Deletes a member by its member code.
     *
     * @param memberCode the member code
     */
    public void deleteMember(String memberCode) {
        Member member = memberRepository.findByMemberCodeAndDeletedIsFalse(memberCode);
        if (member != null) {
            member.setDeleted(true);
            member.setDeletedAt(new Date());

            memberRepository.save(member);
        }
    }
}