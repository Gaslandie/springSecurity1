package com.gaslandie.springsecurity;

public class JWTUtil {
    public static final String SECRET = "vieprivee";
    public static final String AUTH_STRING = "Authorization";
    public static final long EXPIRE_ACCESS_TOKEN = 300000;
    public static final Object REFRESHPATH = "/refresh";
    public static final String PREFIX_BEARER = "Bearer ";
}
