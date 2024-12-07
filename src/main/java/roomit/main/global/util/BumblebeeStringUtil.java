package roomit.main.global.util;

import io.netty.util.internal.logging.MessageFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BumblebeeStringUtil {
    public static String format(String format, Object... args) {
        return MessageFormatter.arrayFormat(format, args).getMessage();
    }
}
