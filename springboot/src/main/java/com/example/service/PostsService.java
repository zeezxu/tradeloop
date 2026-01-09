package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.StatusEnum;
import com.example.entity.Account;
import com.example.entity.Posts;
import com.example.mapper.PostsMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class PostsService {

    @Resource
    private PostsMapper postsMapper;


    public void add(Posts posts) {
        posts.setTime(DateUtil.now());  //current time
        Account currentUser = TokenUtils.getCurrentUser();
        posts.setUserId(currentUser.getId());
        posts.setStatus("Wait for verification");
        postsMapper.insert(posts);
    }


    public void deleteById(Integer id) {
        postsMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            postsMapper.deleteById(id);
        }
    }


    public void updateById(Posts posts) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            posts.setStatus(StatusEnum.NOT_AUDIT.value);
        }
        postsMapper.updateById(posts);
    }


    public Posts selectById(Integer id) {
        return postsMapper.selectById(id);
    }


    public List<Posts> selectAll(Posts posts) {
        return postsMapper.selectAll(posts);
    }


    public PageInfo<Posts> selectPage(Posts posts, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            posts.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Posts> list = postsMapper.selectAll(posts);
        return PageInfo.of(list);
    }

    public PageInfo<Posts> selectFrontPage(Posts posts, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Posts> list = postsMapper.selectFrontAll(posts);
        return PageInfo.of(list);
    }

    public void updateCount(Integer id) {
        postsMapper.updateCount(id);
    }
}