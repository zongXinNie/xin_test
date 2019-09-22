package com.leyou.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("auth-service")
public interface AuthClient {

    @GetMapping("authorization")
    String authoriz(@RequestParam("id") Long id, @RequestParam("secret") String secret);
}
