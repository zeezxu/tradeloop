package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.StatusEnum;
import com.example.entity.Account;
import com.example.entity.Help;
import com.example.mapper.HelpMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class HelpService {

    @Resource
    private HelpMapper helpMapper;


    public void add(Help help) {
        Account currentUser = TokenUtils.getCurrentUser();
        help.setUserId(currentUser.getId());
        help.setStatus("wait for verification");
        help.setTime(DateUtil.now());
        helpMapper.insert(help);
    }


    public void deleteById(Integer id) {
        helpMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            helpMapper.deleteById(id);
        }
    }


    public void updateById(Help help) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            help.setStatus(StatusEnum.NOT_AUDIT.value);
        }
        helpMapper.updateById(help);
    }


    public Help selectById(Integer id) {
        return helpMapper.selectById(id);
    }


    public List<Help> selectAll(Help help) {
        return helpMapper.selectAll(help);
    }


    public PageInfo<Help> selectPage(Help help, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            help.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Help> list = helpMapper.selectAll(help);
        return PageInfo.of(list);
    }

    public PageInfo<Help> selectFrontPage(Help help, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Help> list = helpMapper.selectFrontAll(help);
        return PageInfo.of(list);
    }

}