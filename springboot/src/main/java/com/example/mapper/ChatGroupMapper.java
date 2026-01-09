package com.example.mapper;

import com.example.entity.ChatGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ChatGroupMapper {

    int insert(ChatGroup chatGroup);

    int deleteById(Integer id);

    int updateById(ChatGroup chatGroup);

    ChatGroup selectById(Integer id);

    List<ChatGroup> selectAll(ChatGroup chatGroup);

    @Select("select * from chat_group where user_id = #{userId}")
    List<ChatGroup> selectByUserId(Integer userId);

    @Select("select * from chat_group where chat_user_id = #{chatUserId} and user_id = #{userId}")
    ChatGroup selectByChatUserIdAndUserId(@Param("chatUserId") Integer chatUserId, @Param("userId") Integer userId);

}