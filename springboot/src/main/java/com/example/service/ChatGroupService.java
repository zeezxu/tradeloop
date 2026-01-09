package com.example.service;

import com.example.entity.Account;
import com.example.entity.ChatGroup;
import com.example.entity.User;
import com.example.mapper.ChatGroupMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGroupService {

    @Resource
    private ChatGroupMapper chatGroupMapper;
    @Resource
    ChatInfoService chatInfoService;
    @Resource
    UserService userService;

    public void add(ChatGroup chatGroup) {
        ChatGroup dbChatGroup = chatGroupMapper.selectByChatUserIdAndUserId(chatGroup.getChatUserId(), chatGroup.getUserId());
        if (dbChatGroup == null) {
            chatGroupMapper.insert(chatGroup);
        }
        ChatGroup dbChatGroup1 = chatGroupMapper.selectByChatUserIdAndUserId(chatGroup.getUserId(), chatGroup.getChatUserId());
        if (dbChatGroup1 == null) {
            dbChatGroup1 = new ChatGroup();
            dbChatGroup1.setChatUserId(chatGroup.getUserId());
            dbChatGroup1.setUserId(chatGroup.getChatUserId());
            chatGroupMapper.insert(dbChatGroup1);
        }
    }

    public void deleteById(Integer id) {
        chatGroupMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            chatGroupMapper.deleteById(id);
        }
    }

    public void updateById(ChatGroup chatGroup) {
        chatGroupMapper.updateById(chatGroup);
    }

    public ChatGroup selectById(Integer id) {
        return chatGroupMapper.selectById(id);
    }

    public List<ChatGroup> selectAll(ChatGroup chatGroup) {
        return chatGroupMapper.selectAll(chatGroup);
    }

    public PageInfo<ChatGroup> selectPage(ChatGroup chatGroup, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ChatGroup> list = chatGroupMapper.selectAll(chatGroup);
        return PageInfo.of(list);
    }

    public List<ChatGroup> selectUserGroup() {
        List<ChatGroup> chatGroupList = new ArrayList<>();
        Account currentUser = TokenUtils.getCurrentUser();
        List<ChatGroup> userChatGroupList = chatGroupMapper.selectByUserId(currentUser.getId());
        for (ChatGroup chatGroup : userChatGroupList) {
            Integer chatUserId = chatGroup.getChatUserId();
            Integer num = chatInfoService.selectUnReadChatNum(currentUser.getId(), chatUserId);
            chatGroup.setChatNum(num);
            User chatUser = userService.selectById(chatUserId);
            chatGroup.setChatUserName(chatUser.getName());
            chatGroup.setChatUserAvatar(chatUser.getAvatar());
            chatGroupList.add(chatGroup);
        }
        return chatGroupList;
    }

}