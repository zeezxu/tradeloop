package com.example.mapper;

import com.example.entity.Circles;
import java.util.List;

public interface CirclesMapper {

    int insert(Circles circles);

    int deleteById(Integer id);

    int updateById(Circles circles);

    Circles selectById(Integer id);

    List<Circles> selectAll(Circles circles);

}