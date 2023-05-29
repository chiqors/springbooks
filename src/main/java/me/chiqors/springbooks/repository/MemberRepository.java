package me.chiqors.springbooks.repository;

import me.chiqors.springbooks.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllMembersByDeletedIsFalse();
    List<Member> findByNameContainingAndDeletedIsFalse(String name);
    Member findByIdAndDeletedIsFalse(long memberId);
    Member findByMemberCodeAndDeletedIsFalse(String memberCode);
}