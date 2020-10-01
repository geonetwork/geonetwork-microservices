/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.fao.geonet.common;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil implements Serializable {
  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
  private static final long serialVersionUID = -2550185165626007488L;
  @Value("${jwt.secret}")
  private String secret;

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  /**
   * Generate token based on view and edit group.
   */
  public String generateToken(UserDetails userDetails,
                              Set<Integer> viewingGroup,
                              Set<Integer> editingGroup) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("_viewingGroup", viewingGroup);
    claims.put("_editingGroup", editingGroup);
    return doGenerateToken(claims, userDetails.getUsername());
  }

  private String doGenerateToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
      .setClaims(claims)
      .setSubject(subject)
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
      .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  public Boolean validateToken(String token, String userName) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userName) && !isTokenExpired(token));
  }
}
