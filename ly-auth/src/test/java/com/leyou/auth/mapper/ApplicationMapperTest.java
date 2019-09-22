package com.leyou.auth.mapper;


import com.leyou.auth.entity.Application;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.service.impl.AuthServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationMapperTest {
    @Autowired
    private ApplicationMapper applicationMapper;
    @Autowired
    private AuthService authService;

    @Test
    public void findTargetIdList() {
//        authService.login("123","123",null);
        String authoriz = authService.authoriz(1L, "user-service");
        System.out.println("authoriz = " + authoriz);
    }
    @Test
    public void findTargetIdList1() {
        Application application = applicationMapper.selectByPrimaryKey(1L);
        System.out.println("application = " + application);
    }
}