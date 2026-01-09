package com.example.controller;

import com.example.common.Result;
import com.example.entity.ChatGroup;
import com.example.service.ChatGroupService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/chatGroup")
public class ChatGroupController {

    @Resource
    private ChatGroupService chatGroupService;


    @PostMapping("/add")
    public Result add(@RequestBody ChatGroup chatGroup) {
        chatGroupService.add(chatGroup);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        chatGroupService.deleteById(id);
        return Result.success();
    }


    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        chatGroupService.deleteBatch(ids);
        return Result.success();
    }


    @PutMapping("/update")
    public Result updateById(@RequestBody ChatGroup chatGroup) {
        chatGroupService.updateById(chatGroup);
        return Result.success();
    }


    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ChatGroup chatGroup = chatGroupService.selectById(id);
        return Result.success(chatGroup);
    }


    @GetMapping("/selectAll")
    public Result selectAll(ChatGroup chatGroup) {
        List<ChatGroup> list = chatGroupService.selectAll(chatGroup);
        return Result.success(list);
    }


    @GetMapping("/selectPage")
    public Result selectPage(ChatGroup chatGroup,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ChatGroup> page = chatGroupService.selectPage(chatGroup, pageNum, pageSize);
        return Result.success(page);
    }


    @GetMapping("/selectUserGroup")
    public Result selectUserGroup() {
        List<ChatGroup> chatGroupList = chatGroupService.selectUserGroup();
        return Result.success(chatGroupList);
    }

}