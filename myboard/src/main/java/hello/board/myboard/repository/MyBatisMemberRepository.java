package hello.board.myboard.repository;

import hello.board.myboard.repository.dao.MemberDao;
import hello.board.myboard.vo.AuthorityVo;
import hello.board.myboard.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository {
    private final MemberDao memberDao;
    @Override
    public MemberVo save(MemberVo user) {
        memberDao.save(user);
        return user;
    }

    @Override
    public void insertAuthority(List<AuthorityVo> authorities) {
        memberDao.insertAuthority(authorities);
    }

    @Override
    public Optional<MemberVo> findByEmail(String email) {
        return memberDao.findByEmail(email);
    }

    @Override
    public Optional<MemberVo> findByName(String name) {
        return memberDao.findByName(name);
    }
}
