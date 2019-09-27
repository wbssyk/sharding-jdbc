package com.example.shardingjdbc;

import com.example.shardingjdbc.dao.nosharding.MyMapper;
import com.example.shardingjdbc.dao.sharding.LogMapper;
import com.example.shardingjdbc.dao.sharding.TestDbMapper;
import com.example.shardingjdbc.entity.Log;
import com.example.shardingjdbc.entity.My;
import com.example.shardingjdbc.entity.TestDb;
import com.example.shardingjdbc.util.DatetimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShardingJdbcApplicationTests {

    @Autowired
    private TestDbMapper testDbMapper;
    @Autowired
    private MyMapper myMapper;
    @Autowired
    private LogMapper logMapper;


    @Test
    public void logTest() {
        Log log = new Log();
        log.setCreatetime(new Date());
        log.setMessage("99999");

        Log log1 = new Log();
        log1.setCreatetime(DatetimeUtil.getDate("2019-11-10", "yyyy-MM-dd"));
        log1.setMessage("777");

        logMapper.insert(log);
        logMapper.insert(log1);
    }



    @Test
    public void getTest() {
        List<Map<String, Object>> db = testDbMapper.getDb();
        System.out.println(db);
    }

    @Test
    public void myTest() {
        Map<String, Object> stringObjectMap = testDbMapper.selectOne(1);
        My my = new My();
        my.setNum(1);
        myMapper.insert(my);
    }

    @Test
    public void contextLoads() {
        int year = 2000;
        for (int i = 0; i <10 ; i++) {
            TestDb testDb = new TestDb();
            testDb.setMessage("1"+i);
            testDb.setYear(year+i);
            testDbMapper.insert(testDb);
        }
        for (int i = 10; i <20 ; i++) {
            TestDb testDb = new TestDb();
            testDb.setMessage("1"+i);
            testDb.setYear(year+i);
            testDbMapper.insert(testDb);
        }
//        List<Map<String, Object>> db = testDbMapper.getDb();
//        int size = db.size();
//        System.out.println(size);
//        System.out.println(db);
//        Map<String, Object> stringObjectMap = testDbMapper.selectOne(1);
//        My my = new My();
//        my.setNum(1);
//        myMapper.insert(my);

    }

}
