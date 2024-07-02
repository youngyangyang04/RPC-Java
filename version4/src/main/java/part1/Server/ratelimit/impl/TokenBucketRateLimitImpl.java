package part1.Server.ratelimit.impl;

import part1.Server.ratelimit.RateLimit;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/7/2 1:44
 */
public class TokenBucketRateLimitImpl implements RateLimit {
    //令牌产生速率（单位为ms）
    private static  int RATE;
    //桶容量
    private static  int CAPACITY;
    //当前桶容量
    private volatile int curCapcity;
    //时间戳
    private volatile long timeStamp=System.currentTimeMillis();
    public TokenBucketRateLimitImpl(int rate,int capacity){
        RATE=rate;
        CAPACITY=capacity;
        curCapcity=capacity;
    }
    @Override
    public synchronized boolean getToken() {
        //如果当前桶还有剩余，就直接返回
        if(curCapcity>0){
            curCapcity--;
            return true;
        }
        //如果桶无剩余，
        long current=System.currentTimeMillis();
        //如果距离上一次的请求的时间大于RATE的时间
        if(current-timeStamp>=RATE){
            //计算这段时间间隔中生成的令牌，如果>2,桶容量加上（计算的令牌-1）
            //如果==1，就不做操作（因为这一次操作要消耗一个令牌）
            if((current-timeStamp)/RATE>=2){
                curCapcity+=(int)(current-timeStamp)/RATE-1;
            }
            //保持桶内令牌容量<=10
            if(curCapcity>CAPACITY) curCapcity=CAPACITY;
            //刷新时间戳为本次请求
            timeStamp=current;
            return true;
        }
        //获得不到，返回false
        return false;
    }
}
