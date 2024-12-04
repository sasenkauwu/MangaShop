package com.SemestralnaPraca.MangaShop.config;

import com.SemestralnaPraca.MangaShop.entity.User;
import com.SemestralnaPraca.MangaShop.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomJwtUtil jwtUtil;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = extractTokenFromCookie(request);

        if (jwt != null) {
            if (!processJwtToken(jwt, request, response)) {
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // Helper methods

    private boolean processJwtToken(String jwt, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String username = jwtUtil.extractUsername(jwt);
            if (username != null && jwtUtil.validateToken(jwt, username)) {
                User user = userRepository.findUserByEmail(username);
                if (user != null) {
                    setAuthentication(user, request, username);
                }
            } else {
                handleInvalidToken(response, "Token is expired or invalid");
                return false;
            }
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            handleInvalidToken(response, "Token has expired");
            return false;
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            handleInvalidToken(response, "Invalid token signature");
            return false;
        }
        return true;
    }

    private void setAuthentication(User user, HttpServletRequest request, String username) {
        List<SimpleGrantedAuthority> authorities = Arrays.stream(user.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username, user.getPassword(), authorities);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void handleInvalidToken(HttpServletResponse response, String message) throws IOException {
        clearCookie(response, "jwtToken");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }

    private void clearCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "jwtToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
