package br.com.cederj.safi.configuration.security;

import br.com.cederj.safi.models.User;
import br.com.cederj.safi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IUserRepository userRepository;

    public AuthenticationTokenFilter(TokenService tokenService, IUserRepository userRepository){
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        boolean isValid = this.tokenService.isValidToken(token);
        //Forcando autenticacao
        if(isValid){
            authenticateClient(token);
        }
        filterChain.doFilter(request, response);
    }

    private void authenticateClient(String token) {
        Long id = tokenService.getUserId(token);
        Optional<User> user = userRepository.findById(id);
        User userPresent = user.orElseThrow(() -> {throw new UsernameNotFoundException("User not found");});
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPresent, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if(token == null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        int length = token.length();
       return token.substring(7, length);
    }
}
