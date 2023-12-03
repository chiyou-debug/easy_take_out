package com.easy.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * Generate JWT
     * Using HS256 algorithm, private key uses a fixed secret key
     *
     * @param secretKey JWT secret key
     * @param ttlMillis JWT expiration time (in milliseconds)
     * @param claims    Information to be set
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // Specify the signing algorithm to use as part of the signature
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // Generate JWT time
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // Set the body of the JWT
        JwtBuilder builder = Jwts.builder()
                // If you have private claims, you must first set these private claims, which assigns values to builder's claim. Once written after the standard claims, it overrides them.
                .setClaims(claims)
                // Set the signing algorithm and the key used for the signature
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // Set the expiration time
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * Decrypt Token
     *
     * @param secretKey JWT secret key. This key must be well kept on the server side and not exposed, otherwise the sign can be forged. If integrating with multiple clients, consider using multiple keys.
     * @param token     Encrypted token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        // Get DefaultJwtParser
        Claims claims = Jwts.parser()
                // Set the secret key for the signature
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // Set the jwt to be parsed
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
