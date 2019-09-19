package com.leyou.service.impl;

import com.leyou.LyPageApplication;
import com.leyou.service.PageService;
import com.leyou.utils.RegexUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PageServiceImplTest {
    @Autowired
    private PageService pageService;

    @Test
    public void createItemHtml() throws InterruptedException {
        Long[] arr = {2L, 3L, 4L, 5L};
        for (Long aLong : arr) {
            pageService.createItemHtml(aLong);
            Thread.sleep(500);
        }
    }

    @Test
    public void test() {
        for (int i=0;i<1000;i++) {
            StringBuilder json = new StringBuilder();
            try {
                URL urlObject = new URL("http://192.168.44.68:8081/brand/page?key=&page=1&rows=1000&sortBy=id&desc=false");
                URLConnection uc = urlObject.openConnection();
    // 设置为utf-8的编码 才不会中文乱码
                BufferedReader in = new BufferedReader(new InputStreamReader(uc
                        .getInputStream(), "utf-8"));
                String inputLine = null;
                while ((inputLine = in.readLine()) != null) {
                    json.append(inputLine);
                }
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("------------------"+json.toString());
    }

}