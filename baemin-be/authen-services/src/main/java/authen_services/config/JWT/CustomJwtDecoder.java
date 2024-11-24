package authen_services.config.JWT;
import authen_services.service.AuthenticationService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomJwtDecoder implements JwtDecoder{

    @Value("${SIGNER_KEY}")
    protected String SIGNER_KEY;
    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Autowired
    private AuthenticationService authenticationService;
    @Override
    public Jwt decode(String token) throws JwtException {
        return null;
    }
}
