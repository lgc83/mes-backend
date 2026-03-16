import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import * as S from "../styled/Login.styles";
import "@fortawesome/fontawesome-free/css/all.min.css";
import SimpleModal from "../commons/SimpleModal";

const BACKEND_BASE_URL = "http://localhost:9500";

// 카카오 로고 (노란 말풍선)
const KakaoIcon = () => (
  <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
    <path d="M12 3c5.799 0 10.5 3.664 10.5 8.185 0 4.52-4.701 8.184-10.5 8.184a13.5 13.5 0 0 1-1.727-.11l-4.408 2.883c-.501.265-.678.236-.472-.413l.892-3.678c-2.88-1.46-4.785-3.99-4.785-6.866C1.5 6.665 6.201 3 12 3z" />
  </svg>
);

// 네이버 로고 (초록 N)
const NaverIcon = () => (
  <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
    <path d="M16.273 12.845L7.376 0H0v24h7.727V11.155L16.624 24H24V0h-7.727v12.845z" />
  </svg>
);

// 구글 로고 (실제 브랜드 컬러: 파랑, 초록, 노랑, 빨강)
const GoogleIcon = () => (
  <svg width="20" height="20" viewBox="0 0 24 24">
    <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
    <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
    <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
    <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
  </svg>
);

type OAuthStatus = { google: boolean; kakao: boolean; naver: boolean };

const Login = () => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [rememberMe, setRememberMe] = useState<boolean>(false);
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [oauthStatus, setOauthStatus] = useState<OAuthStatus | null>(null);

  const navigate = useNavigate();

  useEffect(() => {
    const savedEmail = localStorage.getItem("rememberEmail");
    if (savedEmail) {
      setEmail(savedEmail);
      setRememberMe(true);
    }
  }, []);

  // OAuth 설정 여부 확인 (미설정 시 버튼 비활성화)
  useEffect(() => {
    axios.get<OAuthStatus>(`${BACKEND_BASE_URL}/api/oauth/status`).then((res) => {
      setOauthStatus(res.data);
    }).catch(() => setOauthStatus({ google: false, kakao: false, naver: false }));
  }, []);

  // OAuth2 소셜 로그인 성공/실패 처리
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");
    const error = params.get("error");
    if (token) {
      localStorage.setItem("token", token);
      const fn = params.get("firstName");
      const ln = params.get("lastName");
      if (fn) localStorage.setItem("firstName", fn);
      if (ln) localStorage.setItem("lastName", ln);
      window.dispatchEvent(new Event("storage"));
      navigate("/admin", { replace: true });
    } else if (error === "oauth_failed") {
      const msg = params.get("message") || "소셜 로그인에 실패했습니다.";
      alert(decodeURIComponent(msg) + "\n\n※ Client ID/Secret이 설정되지 않았거나, Redirect URI가 일치하는지 확인해 주세요.");
      window.history.replaceState({}, "", "/");
    }
  }, [navigate]);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!email || !password) {
      alert("이메일과 비밀번호를 입력해 주세요!");
      return;
    }

    try {
      const res = await axios.post("http://localhost:9500/members/login", {
        email,
        password,
      });

      // ✅ 여기서 백엔드가 토큰을 내려준다고 가정: res.data.token
      const token = res.data?.token;

      if (!token) {
        alert("로그인 응답에 token이 없습니다. 백엔드 응답을 확인하세요!");
        console.log("login response:", res.data);
        return;
      }

      if (rememberMe) {
        localStorage.setItem("rememberEmail", email);
      } else {
        localStorage.removeItem("rememberEmail");
      }

      // ✅ 1) 토큰 저장 (ProtectedRoute가 읽는 키 = "token" 맞춰야 함)
      localStorage.setItem("token", res.data.token);
      localStorage.setItem("lastName", res.data.lastName);
      localStorage.setItem("firstName", res.data.firstName);

      window.dispatchEvent(new Event("storage"));


      setIsModalOpen(true);

    } catch (err: any) {
      console.error(err);
      const msg = err?.response?.data?.message;
      alert(msg || "로그인 실패! 이메일 또는 비밀번호를 확인해 주세요");
    }
  };

  const handleModalClose = () => {
    setIsModalOpen(false); // 모달 닫기
    navigate("/admin", { replace: true }); // 메인 페이지로 이동
  };

  const handleGoogleLogin = () => {
    if (oauthStatus?.google) window.location.href = `${BACKEND_BASE_URL}/oauth2/authorization/google`;
    else alert("구글 로그인은 application-oauth.yml에 Client ID/Secret을 설정한 후 사용할 수 있습니다.");
  };
  const handleKakaoLogin = () => {
    // 카카오 설정 완료 시 바로 시도 (status API 실패해도 동작)
    window.location.href = `${BACKEND_BASE_URL}/oauth2/authorization/kakao`;
  };
  const handleNaverLogin = () => {
    if (oauthStatus?.naver) window.location.href = `${BACKEND_BASE_URL}/oauth2/authorization/naver`;
    else alert("네이버 로그인은 application-oauth.yml에 Client ID/Secret을 설정한 후 사용할 수 있습니다.");
  };

  return (
    <>
    <S.Wrapper>
      <S.Card>
          <S.Right>
            <S.Title>로그인</S.Title>

             <S.Form onSubmit={handleSubmit}>
                        <S.Label>아이디(이메일)</S.Label>
                        <S.Input
                            type="email"
                            placeholder="이메일을 입력하세요"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                          />

                          <S.Label>비밀번호</S.Label>
                          <S.Input
                            type="password"
                            placeholder="비밀번호를 입력하세요"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                          />

                          <S.CheckboxWrapper>
                            <input
                              type="checkbox"
                              id="remember"
                              checked={rememberMe}
                              onChange={(e) => setRememberMe(e.target.checked)}
                            />{" "}
                            <label htmlFor="remember">아이디 저장</label>
                          </S.CheckboxWrapper>

                        <S.Button type="submit">로그인</S.Button>

                        <S.SocialDivider>또는</S.SocialDivider>

                        <S.SocialButton $variant="kakao" type="button" onClick={handleKakaoLogin}>
                          <KakaoIcon />
                          카카오로 로그인
                        </S.SocialButton>
                        <S.SocialButton $variant="naver" type="button" $disabled={!oauthStatus || !oauthStatus.naver} onClick={handleNaverLogin} title={oauthStatus && !oauthStatus.naver ? "설정 필요" : undefined}>
                          <NaverIcon />
                          네이버로 로그인
                        </S.SocialButton>
                        <S.SocialButton $variant="google" type="button" $disabled={!oauthStatus || !oauthStatus.google} onClick={handleGoogleLogin} title={oauthStatus && !oauthStatus.google ? "설정 필요" : undefined}>
                          <GoogleIcon />
                          구글로 로그인
                        </S.SocialButton>
                      </S.Form>

                      <S.FooterLinks>
                        <S.LinkText href="/member">회원가입</S.LinkText>
                        <S.LinkSeparator />
                        <S.LinkText href="/forgot">아이디/비밀번호 찾기</S.LinkText>
                      </S.FooterLinks>
          </S.Right>
      </S.Card>
    </S.Wrapper>
    <SimpleModal 
        open={isModalOpen} 
        message={`${localStorage.getItem("lastName")}${localStorage.getItem("firstName")}님, 환영합니다!`} 
        onClose={handleModalClose} 
      />
    </>
  );
};

export default Login;