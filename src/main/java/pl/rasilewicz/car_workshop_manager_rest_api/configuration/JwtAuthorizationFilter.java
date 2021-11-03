package pl.rasilewicz.car_workshop_manager_rest_api.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.services.UserDetailsServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.UserServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final UserDetailsService userDetailsService;
    private final UserServiceImpl userService;
    private final String secret;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserDetailsService userDetailsService,
                                  UserServiceImpl userService,
                                  String secret) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication == null){
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        String token = request.getHeader(TOKEN_HEADER);
        if (token != null && token.startsWith(TOKEN_PREFIX)){
            String userName = JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();
            if (userName != null){
                User user = userService.findByUsername(userName);
                UserDetailsServiceImpl userDetailsSImpl = new UserDetailsServiceImpl(user);
                return new UsernamePasswordAuthenticationToken(userDetailsSImpl.getUsername(), null, userDetailsSImpl.getAuthorities());
            }
        }
        return null;
    }

}
