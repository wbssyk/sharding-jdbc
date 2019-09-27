package com.example.shardingjdbc.config;


import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @program: sharding-jdbc
 * @description: 分表算法
 * @author: yaKun.shi
 * @create: 2019-09-20 09:38
 **/
public class TableShardingAlgorithm implements PreciseShardingAlgorithm<Integer> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {
        int value = (shardingValue.getValue() % 2);
        String tableName = null ;
        for (String s:availableTargetNames){
            String substring = s.substring(s.length() - 1);
            if(String.valueOf(value).equals(substring)){
                tableName = s;
            }else {
                continue;
            }
            break;
        }
        return tableName;
    }
}
