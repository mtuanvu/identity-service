package com.mtuanvu.identityservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration  //Đánh dấu nơi định nghĩa các bean thông qua annotation
                // @Bean (các thành phần cấu hình và đối tượng quản lý vởi IoC container)
@EnableWebSecurity  //Để kích hoạt tính năng bảo mật web trong ứng dụng spring.
                    // Đảm bảo ứng dụng web có khả năng bảo vệ api và yêu cầu bảo mật
public class SecurityConfig {

    @Value("${jwt.signerKey}") //lấy giá trị từ file yml
    private String signerKey;

    private final String[] PUBLIC_ENDPOINTS = {"/users/create", "/auth/token", "/auth/introspect"};

    //định nghĩa SecurityFilterChain để cấu hình bảo mật endpoint của ứng dụng
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(requests ->
                //Các endpoint này không yêu cầu xác thực
                requests.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        //các yêu cầu còn lại đều yêu cầu xác thực
                        .anyRequest().authenticated());


        //Cấu hình để yêu cầu JWT hợp lệ để truy cập các api
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder()))
        );

        //tắt csrf (cross-site Request forgery) vì không cần thiết cho REST API thông thường
        httpSecurity.csrf(AbstractHttpConfigurer::disable); //sort hand lambda method (viết ngắn)

        //Trả về object đã cấu hình
        return httpSecurity.build();
    }

    //giải mã Jwt, sử dụng secret key. Sử dụng cho oauth2 ở trên
    @Bean
    JwtDecoder jwtDecoder(){
        //Tạo secretkey từ chuỗi bí mật (signerKey)
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        // Sử dụng NimbusJwtDecoder để cấu hình giải mã JWT với thuật toán HS512 và secret key
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)  // Gắn secret key vào decoder
                .macAlgorithm(MacAlgorithm.HS512) // Sử dụng thuật toán HS512
                .build(); // Xây dựng JwtDecoder
    }
}
