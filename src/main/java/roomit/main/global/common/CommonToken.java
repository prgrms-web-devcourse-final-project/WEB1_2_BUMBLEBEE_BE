package roomit.main.global.common;

public class CommonToken {
    public final static long JWT_ACCESS_TOKEN_EXPIRED_TIME= 1000 * 60 * 15L; //15분
    public final static long JWT_REFRESH_TOKEN_EXPIRED_TIME= 1000 * 60 * 60 * 24L; //1일
    public final static int JWT_COOKIE_REFRESH_TOKEN_EXPIRED_TIME = 60 * 60 * 24; //1일

    public final static String JWT_ACCESS_TOKEN_NAME = "access";
    public final static String JWT_REFRESH_TOKEN_NAME = "refresh";
    public final static String JWT_HEADER = "Authorization";

}
