package authen_services.service;
import authen_services.dto.request.AuthenticationRequest;
import authen_services.dto.request.IntrospectRequest;
import authen_services.dto.response.ApiResponse;
import authen_services.dto.response.AuthenticationResponse;
import authen_services.dto.response.IntrospectResponse;
import authen_services.exception.AuthenException;
import authen_services.exception.ErrorCode;
import authen_services.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.channels.Channel;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
//@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService {
    @NotNull
    @Value("${SIGNER_KEY}")
    protected String SIGNER_KEY;
    @NotNull
    @Value("${valid-duration}")
    protected long VALID_DURATION;

    @NotNull
    @Value("${refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    final UserRepository userRepository;

    final PasswordEncoder passwordEncoder;



    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public AuthenticationResponse authenticate(AuthenticationRequest req) {
        if (req == null || req.getEmail() == null) {
            throw new IllegalArgumentException("Request or email cannot be null");
        }

        var user = userRepository.findByEmail(req.getEmail()).orElse(null);
        if (user == null) {
            throw new AuthenException(ErrorCode.USER_NOT_EXISTED);
        }
        boolean authenticated = passwordEncoder.matches(req.getPassword(), user.getPassword());
        if(!authenticated){
            throw new AuthenException(ErrorCode.UNAUTHENTICATED);
        }

        String token = generateToken(user.getEmail());

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest req)  {
        var token = req.getToken();
        boolean isValid = true;

        try {
            SignedJWT signedJWT = verifyToken(token, false);
        } catch (Exception e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .token(token)
                .valid(isValid)
                .build();
    }


    public AuthenticationResponse refreshToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(token, true);

        String newToken = generateToken(signedJWT.getJWTClaimsSet().getSubject());
        return AuthenticationResponse.builder()
                .token(newToken)
                .authenticated(true)
                .build();
    }

    //! Private
    private String generateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("tgkhanh")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("userId", "Custom")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        // TODO: có hai loại encrypt: symmetric, asymmetric
        //TODO: Symmetric: chung loại encode và decode
        //TODO: Asymmetric: public encode, private decode
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // Check is token correct?
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Check is token expire?
        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) {
            throw new AuthenException(ErrorCode.UNAUTHENTICATED);
        }

//        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
//            throw new AuthenException(ErrorCode.UNAUTHENTICATED);
//        }

        return signedJWT;
    }

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
