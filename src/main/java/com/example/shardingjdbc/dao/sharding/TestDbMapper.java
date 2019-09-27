package com.example.shardingjdbc.dao.sharding;

import com.example.shardingjdbc.entity.TestDb;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TestDbMapper {
    List<Map<String,Object>> getDb();

    Map<String,Object> selectOne(Integer id);
    void insert(TestDb testDb);



    void insertAll(@Param("request") List<TestDb> testDbs);
}
