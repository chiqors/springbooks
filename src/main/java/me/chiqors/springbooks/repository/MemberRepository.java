package me.chiqors.springbooks.repository;

import me.chiqors.springbooks.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Page<Member> findByNameContainingIgnoreCaseAndDeletedIsFalseOrderByRegisteredAtDesc(String name, Pageable pageable);

    Page<Member> findByDeletedIsFalseOrderByRegisteredAtDesc(Pageable pageable);

    Member findByMemberCodeAndDeletedIsFalse(String memberCode);
}