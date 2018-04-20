package com.zhaoyq.service;

import com.alibaba.fastjson.JSONObject;
import com.zhaoyq.entity.UserEntity;
import com.zhaoyq.utils.DataState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 数据存入到缓存队列中
 * @author yaqiangzhao
 * 2018/4/20
 */
@Service
public class MsgAddRedisList {
    private final static Logger logger =Logger.getLogger(MsgAddRedisList.class);
    @Autowired
    private RedisTemplate redisTemplate =null;

    public void addRedis() {
        UserEntity userEntity =new UserEntity();
        userEntity.setName("张三");
        Random random =new Random();
        int i = random.nextInt();
        String age = String.valueOf(i);
        userEntity.setAge(age);
        userEntity.setId("2121212");
        addMsgRedisByState(userEntity,DataState.TASK_QUEUE);
    }
    /**
     *  往缓存中写数据，此处的缓存配置是利用springboot的autoConfigure的自动注解
     *  @param user
      */
    public void addMsgRedisByState(UserEntity user,String keyType) {
        if (StringUtils.isEmpty(user)){
            throw new NullPointerException("用户信息为空!请确认需要缓存的数据"+"{user}");
        }
        if(user.getId()==null && user.getAge()==null){
            logger.info("用户信息为空！请检查用户信息。{用户id}："+user.getId());
        }
        String type =GetRedisKey(keyType);
        if (type .equals("waitting_queue")) {
            ValueOperations<String,Object> vo = redisTemplate.opsForValue();
            vo.set("waitting_queue",user);
        }else{
            ValueOperations<String,Object> vo = redisTemplate.opsForValue();
            vo.set("finish_success",user);
        }
        logger.info("加入到缓存中的数据为！"+user);

    }
    public String GetRedisKey(String queueKey){
        if (queueKey.equals(DataState.TASK_QUEUE)){
            return "waitting_queue";
        }else{
            return "finish_success";
        }
    }
    public String GetDataRedisUser(String type) {
        String str=null;
        if(type == null) {
            logger.info("type参数为空！");
        }
        String typeKey = GetRedisKey(type);
        if (typeKey.equals("waitting_queue")) {
            ValueOperations <String,Object>vo =redisTemplate.opsForValue();
            Object obj = vo.get("waitting_queue");
             str = JSONObject.toJSONString(obj);
        }
        if(typeKey .equals("finish_success")){
            ValueOperations <String,Object>vo =redisTemplate.opsForValue();
            Object obj = vo.get("finish_success");
            str = JSONObject.toJSONString(obj);
        }
        return str;
    }

    @PostConstruct
    public void init(){
        ThreadPoolExecutor threadPoolExecutor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    addRedis();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
