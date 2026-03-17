package org.example.backendpractice.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;

@Component // Spring Bean으로 등록
public class JwtTokenProvider {

    private SecretKey key;
    @PostConstruct // 딱 한번 자동으로 실행
    protected void init() {
        String secretKey = "#john1034811-this-is-my-super-secret-key-for-jwt";
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes()); // 비밀키 byte 배열로 바꿔서, JWT 전용 암호화 키 객체(HMAC-SHA)로 변환
    }

    // Create Token
    public String createToken(String loginId) {
        Date now = new Date();
        long tokenValidTime = 30 * 60 * 1000L; // 30분
        return Jwts.builder()
                .subject(loginId) // 토큰의 주인 = user의 ID
                .issuedAt(now) // 발행 시간 = 현재
                .expiration(new Date(now.getTime() + tokenValidTime))
                .signWith(key) // 암호 키로 도장 찍어 위조 방지
                .compact(); // 정보들 압축해서 하나의 토큰 생성
    }

    public String createRefreshToken(String loginId) {
        Date now = new Date();
        long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L; // 1주
        return Jwts.builder()
                .subject(loginId) // 토큰의 주인 = user의 ID
                .issuedAt(now) // 발행 시간 = 현재
                .expiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(key) // 암호 키로 도장 찍어 위조 방지
                .compact(); // 정보들 압축해서 하나의 토큰 생성
    }

    public String getLoginId(String token) {
        return Jwts.parser() // 해독기 준비
                .verifyWith(key) // 위조되지 않았는지 key로 확인
                .build() // 해독기 완성
                .parseSignedClaims(token) // 토큰 열어서 내용 꺼냄
                .getPayload() // 내용 중 핵심 데이터에 접근
                .getSubject(); // user ID 가져옴
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token); // token 해독
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); // 요청 header 중 "Authorization"이라는 이름의 값 가져옴
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) { // 값이 비어있지 않고, "Bearer "로 시작한다면 앞의 7글자 잘라내고 뒤의 진짜 토큰 문자열만 반환
            return bearerToken.substring(7); // "Bearer " 이후의 진짜 토큰 문자열만 잘라서 반환
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        String loginId = getLoginId(token);
        return new UsernamePasswordAuthenticationToken(loginId, "", Collections.emptyList()); // 아이디와 빈 권한(Collections.emptyList()) 넣어서, 스프링이 인식할 수 있는 공식 인증 명찰 만들어 반환
    }
}
