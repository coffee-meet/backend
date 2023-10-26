package coffeemeet.server.auth.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private static final String EXPIRED_TOKEN_MESSAGE = "토큰이 만료되었습니다.";
  private static final String INVALID_FORMAT_TOKEN_MESSAGE = "토큰의 형식이 적절하지 않습니다.";
  private static final String INVALID_STRUCTURE_TOKEN_MESSAGE = "토큰이 올바르게 구성되지 않았거나, 적절하지 않게 수정되었습니다.";
  private final Key key;

  public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generate(String subject, Date expiredAt) {
    return Jwts.builder()
        .setSubject(subject)
        .setExpiration(expiredAt)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
  }

  public Long extractUserId(String token) {
    Claims claims = parseClaims(token);
    return Long.parseLong(claims.getSubject());
  }

  public boolean isExpiredRefreshToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      return false;
    } catch (Exception e) {
      return true;
    }
  }

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(accessToken)
          .getBody();
    } catch (ExpiredJwtException e) {
      throw new IllegalArgumentException(EXPIRED_TOKEN_MESSAGE);
    } catch (UnsupportedJwtException e) {
      throw new IllegalArgumentException(INVALID_FORMAT_TOKEN_MESSAGE);
    } catch (MalformedJwtException e) {
      throw new IllegalArgumentException(INVALID_STRUCTURE_TOKEN_MESSAGE);
    }
  }

}
