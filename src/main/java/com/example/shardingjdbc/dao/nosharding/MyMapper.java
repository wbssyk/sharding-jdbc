package com.example.shardingjdbc.dao.nosharding;

import com.example.shardingjdbc.entity.My;
import com.example.shardingjdbc.entity.TestDb;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MyMapper {
    List<Map<String,Object>> getDb();

    Map<String,Object> selectOne(Integer id);
    void insert(My my);
}
