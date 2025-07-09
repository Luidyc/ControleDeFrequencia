package Development.team.ControleDeFrequencia.User.Infra.Security;

import Development.team.ControleDeFrequencia.User.Entity.UserEntity;
import Development.team.ControleDeFrequencia.User.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        // Ignora rotas públicas
        if (path.equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);
        if(token != null) {
            var login = tokenService.validateToken(token);
            if(login != null ) {
                UserEntity user = userRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("User Not Found"));
                var authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
                var authentication = new UsernamePasswordAuthenticationToken(user,null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request,response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);
        if(authHeader==null || !authHeader.startsWith("Bearer")) return null;
        return authHeader.replace("Bearer ", "");
    };
}
