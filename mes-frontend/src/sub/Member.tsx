import { useState, type ChangeEvent, type FormEvent } from "react";
import axios from "axios";
import * as S from "../styled/Member.styles";

// 카카오 로고
const KakaoIcon = () => (
  <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
    <path d="M12 3c5.799 0 10.5 3.664 10.5 8.185 0 4.52-4.701 8.184-10.5 8.184a13.5 13.5 0 0 1-1.727-.11l-4.408 2.883c-.501.265-.678.236-.472-.413l.892-3.678c-2.88-1.46-4.785-3.99-4.785-6.866C1.5 6.665 6.201 3 12 3z" />
  </svg>
);

// 네이버 로고
const NaverIcon = () => (
  <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
    <path d="M16.273 12.845L7.376 0H0v24h7.727V11.155L16.624 24H24V0h-7.727v12.845z" />
  </svg>
);

// 구글 로고
const GoogleIcon = () => (
  <svg width="20" height="20" viewBox="0 0 24 24">
    <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
    <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
    <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
    <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
  </svg>
);

type Gender = "male" | "female" | "other" | "";

interface MemberForm {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  repeatPassword: string;
  gender: Gender;
  companyName: string;
  position: string;
  tel: string;
  address: string;
  detailAddress: string;
}

declare global {
  interface Window {
    daum: any;
  }
}

const BACKEND_BASE_URL = "http://localhost:9500";

const Member = () => {
  const [form, setForm] = useState<MemberForm>({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    repeatPassword: "",
    gender: "",
    companyName: "",
    position: "",
    tel: "",
    address: "",
    detailAddress: "",
  });

  const handleChange = (e: ChangeEvent<HTMLInputElement & HTMLSelectElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleGenderChange = (e: ChangeEvent<HTMLInputElement>) => {
    setForm((prev) => ({ ...prev, gender: e.target.value as Gender }));
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (form.password !== form.repeatPassword) {
      alert("비밀번호와 비밀번호 확인이 일치하지 않습니다");
      return;
    }
    try {
      await axios.post(`${BACKEND_BASE_URL}/members/register`, form);
      alert("회원가입이 완료되었습니다.");
      window.location.href = "/";
    } catch (error: any) {
      console.error(error);
      alert("회원가입 중 오류가 발생했습니다.");
    }
  };

  const handleAddressSearch = () => {
    if (!window.daum?.postcode) {
      alert("주소 검색 스크립트 로딩 중입니다. 잠시 후 다시 시도해 주세요.");
      return;
    }
    new window.daum.Postcode({
      oncomplete: (data: any) => {
        setForm((prev) => ({ ...prev, address: data.address }));
      },
    }).open();
  };

  const handleGoogleSignup = () => {
    window.location.href = `${BACKEND_BASE_URL}/oauth2/authorization/google`;
  };
  const handleKakaoSignup = () => {
    window.location.href = `${BACKEND_BASE_URL}/oauth2/authorization/kakao`;
  };
  const handleNaverSignup = () => {
    window.location.href = `${BACKEND_BASE_URL}/oauth2/authorization/naver`;
  };

  return (
    <>
      <script
        src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"
        async
      />
      <S.Wrapper>
        <S.Card>
          <S.Right>
            <S.Title>회원가입</S.Title>

            <S.Form onSubmit={handleSubmit}>
              <S.InputRow>
                <div>
                  <S.Label>이름</S.Label>
                  <S.Input
                    type="text"
                    placeholder="이름을 입력하세요"
                    name="firstName"
                    value={form.firstName}
                    onChange={handleChange}
                  />
                </div>
                <div>
                  <S.Label>성</S.Label>
                  <S.Input
                    type="text"
                    placeholder="성을 입력하세요"
                    name="lastName"
                    value={form.lastName}
                    onChange={handleChange}
                  />
                </div>
              </S.InputRow>

              <S.Label>이메일</S.Label>
              <S.Input
                type="email"
                placeholder="이메일을 입력하세요"
                name="email"
                value={form.email}
                onChange={handleChange}
              />

              <S.InputRow>
                <div>
                  <S.Label>비밀번호</S.Label>
                  <S.Input
                    type="password"
                    placeholder="비밀번호를 입력하세요"
                    name="password"
                    value={form.password}
                    onChange={handleChange}
                  />
                </div>
                <div>
                  <S.Label>비밀번호 확인</S.Label>
                  <S.Input
                    type="password"
                    placeholder="비밀번호를 다시 입력하세요"
                    name="repeatPassword"
                    value={form.repeatPassword}
                    onChange={handleChange}
                  />
                </div>
              </S.InputRow>

              <S.Label>성별</S.Label>
              <S.GenderGroup>
                <S.GenderLabel>
                  <input
                    type="radio"
                    name="gender"
                    value="male"
                    checked={form.gender === "male"}
                    onChange={handleGenderChange}
                  />
                  {" "}남성
                </S.GenderLabel>
                <S.GenderLabel>
                  <input
                    type="radio"
                    name="gender"
                    value="female"
                    checked={form.gender === "female"}
                    onChange={handleGenderChange}
                  />
                  {" "}여성
                </S.GenderLabel>
                <S.GenderLabel>
                  <input
                    type="radio"
                    name="gender"
                    value="other"
                    checked={form.gender === "other"}
                    onChange={handleGenderChange}
                  />
                  {" "}기타
                </S.GenderLabel>
              </S.GenderGroup>

              <S.InputRow>
                <div>
                  <S.Label>회사명</S.Label>
                  <S.Input
                    type="text"
                    placeholder="회사명을 입력하세요"
                    name="companyName"
                    value={form.companyName}
                    onChange={handleChange}
                  />
                </div>
                <div>
                  <S.Label>직급</S.Label>
                  <S.Input
                    type="text"
                    placeholder="직급을 입력하세요"
                    name="position"
                    value={form.position}
                    onChange={handleChange}
                  />
                </div>
              </S.InputRow>

              <S.Label>전화번호</S.Label>
              <S.Input
                type="text"
                placeholder="전화번호를 입력하세요"
                name="tel"
                value={form.tel}
                onChange={handleChange}
              />

              <S.Label>주소</S.Label>
              <S.AddressRow>
                <S.Input
                  type="text"
                  readOnly
                  placeholder="주소 검색을 클릭하세요"
                  name="address"
                  value={form.address}
                  onChange={handleChange}
                />
                <S.AddressButton type="button" onClick={handleAddressSearch}>
                  주소 검색
                </S.AddressButton>
              </S.AddressRow>
              <S.Input
                type="text"
                placeholder="상세주소를 입력하세요"
                name="detailAddress"
                value={form.detailAddress}
                onChange={handleChange}
              />

              <S.Button type="submit">회원가입</S.Button>

              <S.SocialDivider>또는</S.SocialDivider>

              <S.SocialButton $variant="kakao" type="button" onClick={handleKakaoSignup}>
                <KakaoIcon />
                카카오로 회원가입
              </S.SocialButton>
              <S.SocialButton $variant="naver" type="button" onClick={handleNaverSignup}>
                <NaverIcon />
                네이버로 회원가입
              </S.SocialButton>
              <S.SocialButton $variant="google" type="button" onClick={handleGoogleSignup}>
                <GoogleIcon />
                구글로 회원가입
              </S.SocialButton>
            </S.Form>

            <S.FooterLinks>
              <S.LinkText href="/">로그인</S.LinkText>
              <S.LinkSeparator />
              <S.LinkText href="/forgot">아이디/비밀번호 찾기</S.LinkText>
            </S.FooterLinks>
          </S.Right>
        </S.Card>
      </S.Wrapper>
    </>
  );
};

export default Member;
