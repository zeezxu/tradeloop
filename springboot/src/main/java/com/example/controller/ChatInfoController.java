package com.example.controller;

import com.example.common.Result;
import com.example.entity.ChatInfo;
import com.example.service.ChatInfoService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/chatInfo")
public class ChatInfoController {

    @Resource
    private ChatInfoService chatInfoService;


    @PostMapping("/add")
    public Result add(@RequestBody ChatInfo chatInfo) {
        chatInfoService.add(chatInfo);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        chatInfoService.deleteById(id);
        return Result.success();
    }


    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        chatInfoService.deleteBatch(ids);
        return Result.success();
    }


    @PutMapping("/update")
    public Result updateById(@RequestBody ChatInfo chatInfo) {
        chatInfoService.updateById(chatInfo);
        return Result.success();
    }

    @PutMapping("/updateRead/{chatUserId}")
    public Result updateRead(@PathVariable Integer chatUserId) {
        chatInfoService.updateRead(chatUserId);
        return Result.success();
    }


    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ChatInfo chatInfo = chatInfoService.selectById(id);
        return Result.success(chatInfo);
    }


    @GetMapping("/selectAll")
    public Result selectAll(ChatInfo chatInfo) {
        List<ChatInfo> list = chatInfoService.selectAll(chatInfo);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(ChatInfo chatInfo,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ChatInfo> page = chatInfoService.selectPage(chatInfo, pageNum, pageSize);
        return Result.success(page);
    }


    @GetMapping("/selectUserChat/{chatUserId}")
    public Result selectUserChat(@PathVariable Integer chatUserId) {
        List<ChatInfo> chatInfoList = chatInfoService.selectUserChat(chatUserId);
        return Result.success(chatInfoList);
    }

}