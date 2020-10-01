/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.fao.geonet.searching;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fao.geonet.common.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain)
      throws ServletException, IOException {
    final String requestTokenHeader = request.getHeader("Authorization");
    Claims claims = null;
    String jwtToken = null;

    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      jwtToken = requestTokenHeader.substring(7);
      try {
        claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
        String name = jwtTokenUtil.getUsernameFromToken(jwtToken);
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(name, "password");
        auth.setDetails(claims);
        SecurityContextHolder.getContext().setAuthentication(auth);

      } catch (IllegalArgumentException e) {
        System.out.println("Unable to get JWT Token");
      } catch (ExpiredJwtException e) {
        System.out.println("JWT Token has expired");
      }
    } else {
      logger.warn("JWT Token does not begin with Bearer String");
    }
    chain.doFilter(request, response);
  }
}
