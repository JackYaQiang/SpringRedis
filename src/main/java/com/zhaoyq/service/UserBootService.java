package com.zhaoyq.service;

import com.alibaba.fastjson.JSONObject;
import com.zhaoyq.entity.UserEntity;
import com.zhaoyq.utils.DataState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserBootService {
    private  final  static Logger logger = LoggerFactory.getLogger(UserBootService.class);
    @Value("${user.name}")
    private  String name;
    @Value("${user.age}")
    private  String age;
    private static String EnglishName ="zhangsan";
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MsgAddRedisList msgAddRedisList;
    private Set<UserEntity> userEntitySet =new HashSet<UserEntity>();

    /**
     * 设置List类型的数据缓存到redis中。
     */
    public void ListFindSy(){
        String msg="赵亚强，"+"男，"+"来自贵州省"+"安顺市,"+"是一个正在奋斗的青年，为了梦想不断进步和前进的优秀青年。";
        List<String> list =new ArrayList<String>();
        list.add(msg);
        addMSgRedisList(list);
        getMsgRedisList();
    }

    /**
     * @PostConstruct注解，1：初始化配置文件的value值，2：在服务启动的时候
     * 初始化运行run方法。
     *
     */
    @PostConstruct
    public void  init() {
        ExecutorService executorService= Executors.newFixedThreadPool(5);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                int count =0;
                while (true) {
                  // String msg=getMsgRedisList();
                    String msg =msgAddRedisList.GetDataRedisUser("waitting_queue");
                    if (msg ==null) {
                        logger.info("GetDataRedisUser IS empty !!");
                        continue;
                    }
                    JSONObject obj = JSONObject.parseObject(msg);
                    UserEntity userEntity = JSONObject.parseObject(msg,UserEntity.class);
                    //获取年龄,此种情况下默认我已经处理成功的情况。
                    String age = (String) obj.get("age");
                    if(Integer.parseInt(age) % 2 ==0) {
                        logger.info("数据校验成功，加入到成功处理缓存中！","{缓存数据为}"+obj);
                        msgAddRedisList.addMsgRedisByState(userEntity, DataState.FINISH_QUEREU);
                        count ++;
                    }else{
                        logger.info("数据校验失败，放入待处理队列缓存中！","{缓存数据为}"+obj);
                        msgAddRedisList.addMsgRedisByState(userEntity,DataState.TASK_QUEUE);
                    }
                    logger.info("getMsgReidsData="+msg);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 以list的形式往缓存中写数据
     * @param list
     */
    public void addMSgRedisList(List<String> list) {
        ListOperations<String,List>listOperations =redisTemplate.opsForList();
        listOperations.leftPush("msg",list);
    }

    /**
     * 从缓存中read取list的数据
     * @return
     */
    public String getMsgRedisList() {
        ListOperations<String,String> listOperations =redisTemplate.opsForList();
        listOperations.getOperations();
        List<String> list =listOperations.range("msg",0,-1);
       for(int i=0;i<list.size();i++){
           Object obj=list.get(i);
           logger.info("缓存中取到的list数据为"+obj);
        }
        return null;
    }
}
