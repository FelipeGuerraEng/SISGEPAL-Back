package com.SISGEPAL.filters;

import com.SISGEPAL.entities.Empleado;
import com.SISGEPAL.services.EmpleadoService;
import com.SISGEPAL.services.SessionService;
import com.SISGEPAL.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(requireAuthetication(request)) {
            String token = request.getHeader("Authorization");

            if(token != null){
                if(token.length() > 0){
                    token = token.split(" ")[1];
                    String cedula = jwtUtil.extractUsername(token);
                    Empleado empleado = empleadoService.findEmpleadoByCedula(cedula);

                    List<GrantedAuthority> claims = sessionService.createAuthorities(empleado);
                    setUpSpringAuthentication(claims, cedula);
                }
            }
        }
        filterChain.doFilter(request, response);
    }


    private boolean requireAuthetication(HttpServletRequest request){
        final String uri = request.getRequestURI();
        return !(uri.equals("/session") && request.getMethod().equalsIgnoreCase("post"));
    }

    private void setUpSpringAuthentication(List<GrantedAuthority> claims, String cedula) {
        @SuppressWarnings("unchecked")


        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(cedula,null,
                claims);
        SecurityContextHolder.getContext().setAuthentication(auth);

    }


}
