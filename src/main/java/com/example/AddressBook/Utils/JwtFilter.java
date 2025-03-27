package com.example.AddressBook.Utils;

import com.example.AddressBook.model.Users;
import com.example.AddressBook.repository.UserRepository;
import com.example.AddressBook.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Configuration
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    Jwt jwtUtil;

    @Autowired
    public JwtFilter(Jwt jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Autowired
    private UserRepository userRepository;


    @Lazy
    @Autowired
    private UserServices userServices;


    protected  void  doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

        String authorizationHeader= request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorizationHeader==null || !authorizationHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }


        String token = authorizationHeader.substring(7);

        if (userServices.isTokenBlacklisted(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token: Logged out.");
            return;
        }



        String email=jwtUtil.extractEmail(token);

        if (email !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            Optional<Users> usersOptional=userRepository.findByEmail(email);

            if (usersOptional.isPresent() && jwtUtil.validateToken(token)){
                Users user=usersOptional.get();

                UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(user,null,null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
