package me.chiqors.springbooks.repository;

import me.chiqors.springbooks.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllMembersByDeletedIsFalse();
    List<Member> findByNameContainingAndDeletedIsFalse(String name);
    Member findByIdAndDeletedIsFalse(long memberId);
}