package com.example.controller;

import com.example.common.Result;
import com.example.entity.Help;
import com.example.service.HelpService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/help")
public class HelpController {

    @Resource
    private HelpService helpService;


    @PostMapping("/add")
    public Result add(@RequestBody Help help) {
        helpService.add(help);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        helpService.deleteById(id);
        return Result.success();
    }


    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        helpService.deleteBatch(ids);
        return Result.success();
    }


    @PutMapping("/update")
    public Result updateById(@RequestBody Help help) {
        helpService.updateById(help);
        return Result.success();
    }


    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Help help = helpService.selectById(id);
        return Result.success(help);
    }


    @GetMapping("/selectAll")
    public Result selectAll(Help help) {
        List<Help> list = helpService.selectAll(help);
        return Result.success(list);
    }


    @GetMapping("/selectPage")
    public Result selectPage(Help help,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Help> page = helpService.selectPage(help, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectFrontPage")
    public Result selectFrontPage(Help help,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Help> page = helpService.selectFrontPage(help, pageNum, pageSize);
        return Result.success(page);
    }

}