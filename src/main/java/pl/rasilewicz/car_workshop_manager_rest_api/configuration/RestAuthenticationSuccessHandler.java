package pl.rasilewicz.car_workshop_manager_rest_api.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.rasilewicz.car_workshop_manager_rest_api.services.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final long expirationTime;
    private final UserServiceImpl userService;
    private final String secret;

    public RestAuthenticationSuccessHandler(
            @Value("${jwt.expirationTime}") long expirationTime, UserServiceImpl userService,
            @Value("${jwt.secret}") String secret) {
        this.expirationTime = expirationTime;
        this.userService = userService;
        this.secret = secret;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Integer userId = userService.findByUsername(principal.getUsername()).getId();
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withClaim("userId", userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));
        response.addHeader("Authorization", "Bearer " + token);
    }
}
