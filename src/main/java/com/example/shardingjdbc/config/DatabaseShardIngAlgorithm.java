package com.example.shardingjdbc.config;


import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @program: sharding-jdbc
 * @description: 自定义分库算法
 * @author: yaKun.shi
 * @create: 2019-09-19 17:51
 **/
public class DatabaseShardIngAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        int value = (int) (shardingValue.getValue() % 2);
        String dataBaseName =null;
        for (String s:availableTargetNames){
            String substring = s.substring(s.length() - 1);
            if(String.valueOf(value).equals(substring)){
                dataBaseName = s;
            }else {
                continue;
            }
            break;
        }
        return dataBaseName;
    }
}
