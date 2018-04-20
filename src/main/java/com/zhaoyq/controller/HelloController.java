package com.zhaoyq.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhaoyq.entity.UserEntity;
import com.zhaoyq.service.MsgAddRedisList;
import com.zhaoyq.service.UserBootService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/HelloController")
public class HelloController {
    private final  static Logger logger = LoggerFactory.getLogger(HelloController.class);
    @Autowired
    private UserBootService userBootService;
    @Autowired
    private MsgAddRedisList msgAddRedisList;
    @RequestMapping(value="/hello",method= RequestMethod.GET)
    public static String test() {
        return "helloWorld";
    }

    @RequestMapping(value = "/openAccount" ,method = RequestMethod.GET)
    public void openAccount(HttpServletRequest request) {
        String queryString = request.getQueryString();
        Map<String,String>map=new HashMap<String, String>();
        if(queryString !=null){
            map=this.StringtoMap(queryString);
            for(String key:map.keySet()){
                logger.info("map的键为："+key + " , "+"map的值为："+map.get(key));
            }
        }

    }


    /**
     * 将拼接的string类型截取后转化为map
     * @param str
     * @return
     */
    public Map<String,String> StringtoMap(String str){
        Map<String,String> mapStr=new HashMap<String,String>();
        String params[] = str.split("&");
        String param[] = null;
        for(String temp:params) {
            param =temp.split("=");
            mapStr.put(param[0],param[1]);
        }
        return mapStr;
    }

    /**
     *将map<String,String>转为jsonString，然后再转Object
     * @param map
     * @return
     */
    public String mapToString(Map<String,String> map) {
        String paramStr= JSONObject.toJSONString(map);
        UserEntity user = JSONObject.parseObject(paramStr,UserEntity.class);
        logger.info("转化后的user");
        return paramStr;
    }

    @RequestMapping(value = "/addRedisData" ,method = RequestMethod.GET)
    public void testRedisData() {
        msgAddRedisList.addRedis();
    }
}
