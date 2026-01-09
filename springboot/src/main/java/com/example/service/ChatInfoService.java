package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.ChatInfo;
import com.example.mapper.ChatInfoMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChatInfoService {

    @Resource
    private ChatInfoMapper chatInfoMapper;

    public void add(ChatInfo chatInfo) {
        chatInfo.setTime(DateUtil.now());
        chatInfoMapper.insert(chatInfo);
    }

    public void deleteById(Integer id) {
        chatInfoMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            chatInfoMapper.deleteById(id);
        }
    }
    public void updateById(ChatInfo chatInfo) {
        chatInfoMapper.updateById(chatInfo);
    }


    public ChatInfo selectById(Integer id) {
        return chatInfoMapper.selectById(id);
    }


    public List<ChatInfo> selectAll(ChatInfo chatInfo) {
        return chatInfoMapper.selectAll(chatInfo);
    }


    public PageInfo<ChatInfo> selectPage(ChatInfo chatInfo, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ChatInfo> list = chatInfoMapper.selectAll(chatInfo);
        return PageInfo.of(list);
    }

    public Integer selectUnReadChatNum(Integer userId, Integer chatUserId) {
        return chatInfoMapper.selectUnReadChatNum(userId, chatUserId);
    }

    public List<ChatInfo> selectUserChat(Integer chatUserId) {
        Account currentUser = TokenUtils.getCurrentUser();
        Integer userId = currentUser.getId();
        return chatInfoMapper.selectUserChat(userId, chatUserId);
    }

    public void updateRead(Integer chatUserId) {
        Account currentUser = TokenUtils.getCurrentUser();
        chatInfoMapper.updateRead(currentUser.getId(), chatUserId);
    }

}