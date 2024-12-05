package roomit.main.global.config.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

public class CookieUtil {

    public static void addCookie
            (HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from(name, value)
            .httpOnly(true)
            .secure(true) // HTTPS에서만 사용
            .sameSite("None") // Cross-Origin 허용
            .path("/")
            .maxAge(maxAge) // 1일
            .build();
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
}
