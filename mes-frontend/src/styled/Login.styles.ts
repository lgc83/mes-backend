import styled from "styled-components";

export const Wrapper = styled.div`
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%);
`;
export const Card = styled.div`
  width: 400px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06), 0 1px 3px rgba(0, 0, 0, 0.04);
  overflow: hidden;
  display: flex;
`;
export const Right = styled.div`
  width: 100%;
  padding: 2.5rem 2rem;
`;
export const Title = styled.h1`
  font-size: 1.75rem;
  font-weight: 700;
  margin-bottom: 1.75rem;
  color: #1a1a2e;
  letter-spacing: -0.5px;
  text-align: center;
`;
export const Form = styled.form`
  width: 100%;
`;
export const Label = styled.label`
  display: block;
  font-size: 0.875rem;
  font-weight: 500;
  color: #4b5563;
  margin-bottom: 0.5rem;
`;
export const Input = styled.input`
  width: 100%;
  padding: 0.8rem 1rem;
  margin-bottom: 1.25rem;
  border-radius: 10px;
  border: 1px solid #e5e7eb;
  font-size: 0.95rem;
  box-sizing: border-box;
  transition: border-color 0.2s, box-shadow 0.2s;
  &:focus {
    border-color: #4e73df;
    outline: none;
    box-shadow: 0 0 0 3px rgba(78, 115, 223, 0.12);
  }
  &::placeholder {
    color: #9ca3af;
  }
`;

export const CheckboxWrapper = styled.div`
  font-size: 0.875rem;
  margin-bottom: 1.25rem;
  color: #6b7280;
`;
export const Button = styled.button`
  width: 100%;
  padding: 0.9rem;
  border-radius: 10px;
  border: none;
  background: linear-gradient(135deg, #4e73df 0%, #3d5fc7 100%);
  color: #fff;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  margin-top: 0.25rem;
  transition: transform 0.2s, box-shadow 0.2s;
  box-shadow: 0 2px 8px rgba(78, 115, 223, 0.3);
  &:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(78, 115, 223, 0.4);
  }
`;
export const Divider = styled.hr`
  margin: 1.5rem 0;
`;
export const SocialDivider = styled.div`
  font-size: 0.85rem;
  color: #9ca3af;
  text-align: center;
  margin: 1.25rem 0 1rem;
`;
export const SocialButton = styled.button<{ $variant?: string; $disabled?: boolean }>`
  margin-top: 0.6rem;
  width: 100%;
  padding: 0.75rem 1.2rem;
  border-radius: 10px;
  border: none;
  cursor: ${({ $disabled }) => ($disabled ? "not-allowed" : "pointer")};
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.6rem;
  font-size: 0.95rem;
  font-weight: 600;
  transition: transform 0.2s, opacity 0.2s;
  opacity: ${({ $disabled }) => ($disabled ? 0.5 : 1)};
  background: ${({ $variant }) => {
    switch ($variant) {
      case "kakao":
        return "#FEE500";
      case "naver":
        return "#03C75A";
      case "google":
        return "#FFFFFF";
      default:
        return "#e5e7eb";
    }
  }};
  color: ${({ $variant }) => ($variant === "kakao" ? "#000000" : $variant === "google" ? "#3c4043" : "#ffffff")};
  border: ${({ $variant }) => ($variant === "google" ? "1px solid #dadce0" : "none")};
  box-shadow: ${({ $variant }) => ($variant === "google" ? "0 1px 2px rgba(60,64,67,0.3)" : "none")};
  svg {
    flex-shrink: 0;
  }
  &:hover {
    transform: ${({ $disabled }) => ($disabled ? "none" : "translateY(-1px)")};
    opacity: ${({ $disabled }) => ($disabled ? 0.5 : 0.95)};
  }
`;
/*export const SocialButton = styled.button<{bg:string}>`
width:100%; padding:0.65rem; border-radius:10rem; border:none;
color:#fff; font-size:0.85rem; margin-bottom:0.5rem;
background:${({bg}) => bg};
cursor:pointer;
`;*/
export const FooterLinks = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  margin-top: 1.75rem;
  padding-top: 1.25rem;
  border-top: 1px solid #f3f4f6;
`;
export const LinkText = styled.a`
  font-size: 0.9rem;
  color: #4e73df;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s;
  &:hover {
    color: #3d5fc7;
  }
`;
export const LinkSeparator = styled.span`
  width: 1px;
  height: 14px;
  background: #e5e7eb;
`;