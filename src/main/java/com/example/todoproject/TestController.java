package com.example.todoproject;

import com.example.todoproject.common.dto.CommonResponse;
import com.example.todoproject.common.dto.EmptyDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class TestController {

    @GetMapping("/illegal")
    public CommonResponse<EmptyDto> illegal() {
        throw new IllegalArgumentException("illegal");
    }


    @GetMapping("/null-pointer")
    public CommonResponse<EmptyDto> nullPointer() {
        throw new NullPointerException("nullPointer");
    }

}
