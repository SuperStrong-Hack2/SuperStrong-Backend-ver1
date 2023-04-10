package SuperStrong.coinWallet.repository;

import SuperStrong.coinWallet.entity.MemberTmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTmpRepository extends JpaRepository<MemberTmp, String> {
    MemberTmp findByEmail(String email);
    MemberTmp findByMemberId(String memberId);
}
