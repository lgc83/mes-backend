package com.samsung.mes.service;

import java.util.List; //유 류[류시원]

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.samsung.mes.dto.MemberRequestDTO;
import com.samsung.mes.entity.Member;
import com.samsung.mes.repository.MemberRepository;

import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;

@Service
//@Transactional
//@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;//멤버변수2개 의존성 주입
	private final PasswordEncoder passwordEncoder;

	//
	@Autowired //생성자로 위에 맴버변수가 주입되어야 하는이유 
	//스프링이 자동으로 memberRepository, passwordEncoder를 넣어줌 이렇게 하면 서비스 안에서 바로 사용 가능
	public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional //중간에 실패하면 저장이 안되서 자동으로 롤백
	public Member register(MemberRequestDTO dto) {//회원가입 

		if(memberRepository.existsByEmail(dto.getEmail())) {
			throw new IllegalArgumentException("이미 가입된 이메일 입니다");
		}//같은 이메일이 있으면 가입 못하게 막음

		String encryptedPw = passwordEncoder.encode(dto.getPassword());
		//암호화된 문자열로 바꿔 저장

		// 3. DTO -> Entity (Builder 이용)
		Member member = Member.builder()
				.firstName(dto.getFirstName())
				.lastName(dto.getLastName())
				.email(dto.getEmail())
				.password(encryptedPw) // 실서비스에서는 암호화 필수
				.gender(dto.getGender())
				.companyName(dto.getCompanyName())
				.position(dto.getPosition())
				.tel(dto.getTel())
				.address(dto.getAddress())
				.detailAddress(dto.getDetailAddress())
				.build();
		return memberRepository.save(member);//db에 저장하고 저장된 객체를 리턴
	}

	public List<Member> getAllMembers(){//전체회원조회
		return memberRepository.findAll();
	}

	public Member getMemberById(Long id) {//특정회원조회
		return memberRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(""+id)
		);
	}

	@Transactional
	public void deleteMember(Long id) {//존재하는지 확인하고
		if(!memberRepository.existsById(id)) {
			throw new IllegalArgumentException("삭제할 회원이 없음"+id);
		}
	}

	public Member login(String email, String password) {
		Member member = memberRepository.findByEmail(email.trim())
				.orElseThrow(() -> new RuntimeException("존재하지 않는 이메일"));

		// 소셜 로그인으로 가입한 경우 → 이메일/비밀번호 로그인 불가
		if (member.getProvider() != null && !member.getProvider().isBlank()) {
			throw new RuntimeException("소셜가입:" + member.getProvider());
		}

		boolean ok = passwordEncoder.matches(password, member.getPassword());
		if (!ok) throw new RuntimeException("비밀번호 불일치");
		return member;
	}

	/** OAuth2 소셜 로그인: provider+providerId로 조회, 없으면 신규 생성 */
	@Transactional
	public Member findOrCreateOAuthUser(String provider, String providerId, String email, String name) {
		return memberRepository.findByProviderAndProviderId(provider, providerId)
				.orElseGet(() -> {
					// 기존 이메일로 가입된 회원이 있으면 provider 정보만 업데이트
					Member existing = memberRepository.findByEmail(email).orElse(null);
					if (existing != null) {
						existing.setProvider(provider);
						existing.setProviderId(providerId);
						return memberRepository.save(existing);
					}
					// 신규 생성 (소셜 전용 - 비밀번호는 사용 안 함)
					String[] names = (name != null && !name.isBlank()) ? name.split(" ", 2) : new String[]{"", ""};
					String firstName = names.length > 1 ? names[0] : (names[0].isEmpty() ? "User" : names[0]);
					String lastName = names.length > 1 ? names[1] : "";
					String oauthPw = passwordEncoder.encode("OAUTH_" + providerId + "_" + System.currentTimeMillis());
					Member member = Member.builder()
							.email(email)
							.password(oauthPw)
							.firstName(firstName)
							.lastName(lastName)
							.gender("other")
							.provider(provider)
							.providerId(providerId)
							.build();
					return memberRepository.save(member);
				});
	}

}
/* 1. 비밀번호 일치 검사
if (!req.getPassword().equals(req.getRepeatPassword())) {
    throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
}*/

/* 2. 이메일 중복 검사
memberRepository.findByEmail(req.getEmail())
        .ifPresent(m -> {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
*/