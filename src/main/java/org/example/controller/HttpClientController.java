package org.example.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class HttpClientController {

    @RequestMapping("/getKey")
    public Object getMethod(){
        return "value";
    }

    @RequestMapping("/getParamKey")
    public Object getMethod(String key){
        if(StringUtils.isNotBlank(key)){
            if("key".equals(key)){
                return key + "-- value";
            }
        }
        return "key not value";
    }

}
