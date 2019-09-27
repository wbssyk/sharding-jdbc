package com.example.shardingjdbc.config;

import com.example.shardingjdbc.util.DatetimeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.ShardingValue;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: sharding-jdbc
 * @description: 日志表分表算法
 * @author: yaKun.shi
 * @create: 2019-09-26 09:06
 **/
@Component
@Log4j2
public class LogTableShardIngAlgorithm implements PreciseShardingAlgorithm<Date> {

    @Autowired
    @Qualifier("dataSource0")
    private DataSource dataSource0;

    @Autowired
    @Qualifier("dataSource1")
    private DataSource dataSource1;

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
        String yyyymm = DatetimeUtil.formatDate(shardingValue.getValue(), "YYYYMM");
        // 需要生成的表名  log_201909
        String tableName = "log_" + yyyymm;
        try {
            // 动态创建日志表(根据月份来分)
            String sql = " CREATE TABLE IF NOT EXISTS" + " " + tableName +
                    "(id VARCHAR(64) not null,message VARCHAR(64),createtime datetime,PRIMARY KEY (id))";

            Connection connection = dataSource0.getConnection();
            Statement stmt = connection.createStatement();
            if (0 == stmt.executeUpdate(sql)) {
                log.info("成功创建表！");
            } else {
                log.info("创建表失败！");
            }
            stmt.close();
            connection.close();
            log.info("//关闭资源");
        } catch (SQLException e) {
        }
        Date value = shardingValue.getValue();
        for (String s : availableTargetNames) {
            String substring = s.substring(s.length() - 1);
            if (yyyymm.equals(substring)) {
                tableName = getRoutTable(shardingValue.getLogicTableName(), value);
            } else {
                continue;
            }
            break;
        }

        return tableName;


    }

    private String getRoutTable(String logicTable, Date keyValue) {
        if (keyValue != null) {
            String formatDate = DatetimeUtil.formatDate(keyValue, "YYYYMM");
            return logicTable + formatDate;
        }
        return null;

    }



    private Collection<String> getRoutTable(String logicTable, Date lowerEnd, Date upperEnd) {
        Set<String> routTables = new HashSet<String>();
        if (lowerEnd != null && upperEnd != null) {
            List<String> rangeNameList = getRangeNameList(lowerEnd, upperEnd);
            for (String string : rangeNameList) {
                routTables.add(logicTable + string);
            }
        }
        return routTables;
    }
    private static List<String> getRangeNameList(Date start, Date end) {
        List<String> result = Lists.newArrayList();
        // 定义日期实例
        Calendar dd = Calendar.getInstance();
        // 设置日期起始时间
        dd.setTime(start);
        // 判断是否到结束日期
        while (dd.getTime().before(end)) {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMM");
            String str = sdf.format(dd.getTime());
            result.add(str);
            // 进行当前日期月份加1
            dd.add(Calendar.MONTH, 1);
        }
        return result;
    }

}
