package com.SISGEPAL.filters;

import com.SISGEPAL.DTO.session.ErrorResponse;
import com.SISGEPAL.entities.Empleado;
import com.SISGEPAL.services.EmpleadoService;
import com.SISGEPAL.services.SessionService;
import com.SISGEPAL.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private SessionService sessionService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(requireAuthetication(request)) {
            String token = request.getHeader("Authorization");

            if(token != null){
                if(token.length() > 0){
                    token = token.split(" ")[1];
                    try {
                        jwtUtil.isTokenExpired(token);
                        String cedula = jwtUtil.extractUsername(token);
                        Empleado empleado = empleadoService.findEmpleadoByCedula(cedula);

                        List<GrantedAuthority> claims = sessionService.createAuthorities(empleado);
                        setUpSpringAuthentication(claims, cedula);
                    }catch(Exception ex) {
                        doNoTokenResponse(response);
                        return;
                    }

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

    private void doNoTokenResponse(HttpServletResponse response) throws IOException {
        ErrorResponse error = new ErrorResponse();
        error.setError("Token no válido");
        ObjectMapper objectMapper = new ObjectMapper();
        String errorJson = objectMapper.writeValueAsString(error);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(403);
        out.print(errorJson);
        out.flush();
    }
}
