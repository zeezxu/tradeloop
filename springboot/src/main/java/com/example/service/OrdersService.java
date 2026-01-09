package com.example.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import com.example.common.enums.OrderStatusEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Address;
import com.example.entity.Goods;
import com.example.entity.Orders;
import com.example.mapper.OrdersMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    GoodsService goodsService;
    @Resource
    AddressService addressService;


    public void add(Orders orders) {
        Integer goodsId = orders.getGoodsId();
        Goods goods = goodsService.selectById(goodsId);
        orders.setGoodsName(goods.getName());
        orders.setGoodsImg(goods.getImg());
        orders.setSaleId(goods.getUserId());  //seller ID
        orders.setTotal(goods.getPrice());

        Integer addressId = orders.getAddressId();
        Address address = addressService.selectById(addressId);
        orders.setUserName(address.getName());  // user's name
        orders.setAddress(address.getAddress());
        orders.setPhone(address.getPhone());

        Account currentUser = TokenUtils.getCurrentUser();
        orders.setUserId(currentUser.getId());  //buyer ID
        orders.setStatus(OrderStatusEnum.NOTPAY.value); // NOTPAY by default
        orders.setOrderNo(System.currentTimeMillis() + RandomUtil.randomNumbers(7));
        orders.setTime(DateUtil.now());

        ordersMapper.insert(orders);
    }

    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            ordersMapper.deleteById(id);
        }
    }


    public void updateById(Orders orders) {
        ordersMapper.updateById(orders);
    }


    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }


    public List<Orders> selectAll(Orders orders) {
        return ordersMapper.selectAll(orders);
    }


    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            orders.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

    public Orders selectByOrderNo(String orderNo) {
        return ordersMapper.selectByOrderNo(orderNo);
    }

    public PageInfo<Orders> selectSalePage(Orders orders, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            orders.setSaleId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

    public List<Dict> selectLine() {
        List<Dict> dictList = new ArrayList<>();
        // all completed orders
        List<Orders> ordersList = ordersMapper.selectAll(null).stream().filter(orders ->
                OrderStatusEnum.DONE.value.equals(orders.getStatus())).collect(Collectors.toList());
        Date date = new Date();
        DateTime start = DateUtil.offsetDay(date, -8);
        DateTime end = DateUtil.offsetDay(date, -1);
        List<DateTime> dateTimes = DateUtil.rangeToList(start, end, DateField.DAY_OF_YEAR);
        List<String> dateList = dateTimes.stream().map(DateUtil::formatDate).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        for (String day : dateList) {
            BigDecimal total = ordersList.stream().filter(orders -> orders.getTime().contains(day)).map(Orders::getTotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            Dict dict = Dict.create().set("name", day).set("value", total);
            dictList.add(dict);
        }
        return dictList;
    }

    public List<Dict> selectBar() {
        List<Dict> dictList = new ArrayList<>();
        // all completed orders
        List<Orders> ordersList = ordersMapper.selectAll(null).stream().filter(orders ->
                OrderStatusEnum.DONE.value.equals(orders.getStatus())).collect(Collectors.toList());
        Set<String> saleList = ordersList.stream().map(Orders::getSaleName).collect(Collectors.toSet());
        for (String sale : saleList) {
            BigDecimal total = ordersList.stream().filter(orders -> orders.getSaleName().equals(sale)).map(Orders::getTotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            Dict dict = Dict.create().set("name", sale).set("value", total);
            dictList.add(dict);
        }
        return dictList;
    }
}