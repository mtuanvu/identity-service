package com.mtuanvu.identityservice.service;

import com.mtuanvu.identityservice.dto.request.AuthenticationRequest;
import com.mtuanvu.identityservice.dto.request.IntrospectRequest;
import com.mtuanvu.identityservice.dto.response.AuthenticationResponse;
import com.mtuanvu.identityservice.dto.response.IntrospectResponse;
import com.mtuanvu.identityservice.entities.User;
import com.mtuanvu.identityservice.exception.AppException;
import com.mtuanvu.identityservice.exception.ErrorCode;
import com.mtuanvu.identityservice.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;


    @NonFinal //Đánh dấu tránh inject
    @Value("${jwt.signerKey}") //Lấy từ yml
    protected String SIGNER_KEY;

    //Hàm thực thi generate token
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        //Kiểm tra xem username có tồn tại không
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));

        //Kiểm tra password user có khớp với trong db không
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = generateToken(request.getUsername());

        return AuthenticationResponse.builder()
                .token(token).authenticated(true)
                .build();

    }

    //Hàm generate token
    private String generateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512); //Header
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject(username) //Đại diện cho user đăng nhập
                .issuer("mtuanvu.com") //xác định token này được issuer từ ai (người phát hành)
                .issueTime(new Date()).expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())) //Xác địn thời hạn của token
                .claim("customClaim", "Custom").build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        //đây là signature
        JWSObject jwsObject = new JWSObject(header, payload);


        //Ký
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }


    //Hàm verified token
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        //lấy token
        String token = request.getToken();

        // Tạo đối tượng JWSVerifier sử dụng SIGNER_KEY để xác minh chữ ký của token
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // Phân tích token và chuyển đổi nó thành đối tượng SignedJWT để truy cập các thông tin bên trong token
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Lấy thời gian hết hạn của token từ các claims trong token
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        //Kiểm tra chữ ký của token có hợp lệ hay không
        boolean verified = signedJWT.verify(verifier);

        //Trả về IntrospectResponse với trường valid xác định tính hợp lệ của token
        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date())) // Kiểm tra cả chữ ký và thời gian hết hạn
                .build();

    }
}
