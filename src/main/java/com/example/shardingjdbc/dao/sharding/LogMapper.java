package com.example.shardingjdbc.dao.sharding;

import com.example.shardingjdbc.entity.Log;
import com.example.shardingjdbc.entity.TestDb;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LogMapper {
    List<Map<String,Object>> getDb();
    Map<String,Object> selectOne(Integer id);
    void insert(Log log);

    void createTmpTable(@Param("tableName") String tableName);

    String findAllTableNames();
}
