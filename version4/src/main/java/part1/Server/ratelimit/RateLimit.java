package part1.Server.ratelimit;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/7/2 1:43
 */
public interface RateLimit {
    //获取访问许可
    boolean getToken();
}
