package be.bstorm.chesstt_springapi.configs;

import be.bstorm.chesstt_springapi.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils utils;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtils utils, UserDetailsService userDetailsService) {
        this.utils = utils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if(authorization != null){
            String[] authorizations = authorization.split(" ");
            String type = authorizations[0];
            String token = authorizations[1];
            if(type.equals("Bearer") && !token.equals("")){
                if(utils.isValid(token)){
                    String email = utils.getEmail(token);
                    UserDetails user = userDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken upt = new UsernamePasswordAuthenticationToken(user,token,user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(upt);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
