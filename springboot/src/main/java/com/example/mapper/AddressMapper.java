package com.example.mapper;

import com.example.entity.Address;
import java.util.List;
public interface AddressMapper {

    int insert(Address address);

    int deleteById(Integer id);

    int updateById(Address address);

    Address selectById(Integer id);

    List<Address> selectAll(Address address);

}