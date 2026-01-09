package com.example.mapper;

import com.example.entity.Help;
import java.util.List;

public interface HelpMapper {

    int insert(Help help);

    int deleteById(Integer id);

    int updateById(Help help);

    Help selectById(Integer id);

    List<Help> selectAll(Help help);

    List<Help> selectFrontAll(Help help);

}