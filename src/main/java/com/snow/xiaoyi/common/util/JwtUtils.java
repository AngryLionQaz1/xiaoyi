package com.snow.xiaoyi.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtUtils {

    private static final String JWT_KEY_ID="JWT_KEY_ID";


    /**
     * 私钥加密token
     *
     * @param str      载荷中的数据
     * @param privateKey    私钥
     * @param expireMinutes 过期时间，单位秒
     * @return
     */
    public static String generateToken(String str, PrivateKey privateKey, int expireMinutes) {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(expireMinutes);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .claim(JWT_KEY_ID, str)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * 私钥加密token
     *
     * @param str      载荷中的数据
     * @param secret    私钥
     * @param expireMinutes 过期时间，单位秒
     * @return
     */
    public static String generateToken(String str, String secret , long expireMinutes) {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(expireMinutes);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .claim(JWT_KEY_ID, str)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 私钥加密token
     *
     * @param str      载荷中的数据
     * @param privateKey    私钥字节数组
     * @param expireMinutes 过期时间，单位秒
     * @return
     * @throws Exception
     */
    public static String generateToken(String str, byte[] privateKey, int expireMinutes) throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(expireMinutes);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .claim(JWT_KEY_ID, str)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.RS256, RsaUtils.getPrivateKey(privateKey))
                .compact();
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) throws Exception {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }
    private static Jws<Claims> parserToken(String token, String secret) throws Exception {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥字节数组
     * @return
     * @throws Exception
     */
    private static Jws<Claims> parserToken(String token, byte[] publicKey) throws Exception {
        return Jwts.parser().setSigningKey(RsaUtils.getPublicKey(publicKey))
                .parseClaimsJws(token);
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     * @throws Exception
     */
    public static String getInfoFromToken(String token, PublicKey publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return  body.get(JWT_KEY_ID).toString();

    }

    public static String getInfoFromToken(String token,  String secret)  {
        Jws<Claims> claimsJws = null;
        try {
            claimsJws = parserToken(token, secret);
        } catch (Exception e) {
            return null;
        }
        Claims body = claimsJws.getBody();
        return  body.get(JWT_KEY_ID).toString();

    }


    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     * @throws Exception
     */
    public static String getInfoFromToken(String token, byte[] publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return  body.get(JWT_KEY_ID).toString();
    }


    public static void main(String[] args) {


        String s = generateToken("1", "1232312313123", 1);
        System.out.println(s);
        String infoFromToken = getInfoFromToken("eyJhbGciOiJIUzUxMiJ9.eyJKV1RfS0VZX0lEIjoiMSIsImV4cCI6MTU1NTgzMzcxM30.jwFGp4RMNNOmyARmUtLTxG21abPFHWzim-xGZoYYtOKmz8TIv2jBMUW50GpVdqzROh0G0m6F6li7x1TSCbSiEw", "1232312313123");
        System.out.println(infoFromToken);


    }




}
