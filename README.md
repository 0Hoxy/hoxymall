# 개인 프로젝트 (쇼핑몰 혹시몰)

## 제작 상황

**2024-10-13**
- CRUD
- SKU 8~12자리 까지 가능한 난수 생성

**2024-10-14**
- 카테고리 엔티티 생성
- 카테고리와 상품 연관관계 매핑
- 카테고리 CRUD

**2024-10-15**
- 제품 이미지 및 상세 이미지 업로드 기능
- 이미지 수정 및 삭제 (해당 기능 사용시 저장 경로에 있는 이미지 교체 및 삭제)
- 이미지 업로드 속도 개선(정적 리소스 처리 최적화)

**2024-10-18**
- SpringSecurity 의존성 주입
- 회원 CRUD기능 구현
- SecurityConfig 및 loadUser를 위한 CustomUserDetailsService 구현
- 해당 기능은 ResponseBody를 이용하여 REST API로 만들었음

**2024-10-21**
- OAuth2를 이용한 Google, Naver, Kakao 로그인
- 플렛폼 끼리 이메일이 같을 경우 소셜로그인 유저에 예외가 필요할 것으로 보임

## 개선해야 할 사항
- 이미지 업로드 개선(업로드 진행 표시기 등)
- 회원가입 페이지, 유저 정보수정 및 전체 유저 조회(관리자) 페이지, 회원탈퇴 버튼or회원탈퇴 페이지 (우선)
- 고객 페이지와 관리자 페이지 분리시켜 제작
- 소셜 로그인(권한 검증 및 코드 개선)(우선)
- 장바구니 및 결제 시스템
- 상품 CRUD도 추후 REST API로 변경
