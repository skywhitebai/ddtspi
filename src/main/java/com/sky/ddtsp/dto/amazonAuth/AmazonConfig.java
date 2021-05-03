package com.sky.ddtsp.dto.amazonAuth;

import com.sky.ddtsp.dao.custom.CustomSysConfigMapper;
import com.sky.ddtsp.entity.SysConfig;
import com.sky.ddtsp.util.SpringUtil;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * 亚马逊配置信息
 */
@Data
public class AmazonConfig {
    public static AmazonConfig INSTANCE  = new AmazonConfig();
    String amazonIamUserAccessKeyId;
    String amazonIamUserSecretKey;
    String amazonIamRoleArn;
    String amazonIamRoleSessionName;
    String amazonAppClientId;
    String amazonAppClientSecret;
    private AmazonConfig() {


    }
    static {
        CustomSysConfigMapper customSysConfigMapper= SpringUtil.getBean(CustomSysConfigMapper.class);
        List<SysConfig> list=customSysConfigMapper.selectByExample(null);
        INSTANCE.amazonIamUserAccessKeyId=getConfig(list,"amazonIamUserAccessKeyId");
        INSTANCE.amazonIamUserSecretKey=getConfig(list,"amazonIamUserSecretKey");
        INSTANCE.amazonIamRoleArn=getConfig(list,"amazonIamRoleArn");
        INSTANCE.amazonIamRoleSessionName=getConfig(list,"amazonIamRoleSessionName");
        INSTANCE.amazonAppClientId=getConfig(list,"amazonAppClientId");
        INSTANCE.amazonAppClientSecret=getConfig(list,"amazonAppClientSecret");
    }

    private static String getConfig(List<SysConfig> list,String keyName) {
       Optional<SysConfig> firstFind=list.stream().filter(item->item.getKeyName().equals(keyName)).findFirst();
       if(!firstFind.isPresent()){
           return null;
       }
       return firstFind.get().getKeyValue();
    }

    public static AmazonConfig getInstance(){
        return INSTANCE;
    }

}
