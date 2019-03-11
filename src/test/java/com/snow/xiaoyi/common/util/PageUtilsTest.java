package com.snow.xiaoyi.common.util;

import com.snow.xiaoyi.common.pojo.User;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PageUtilsTest {

    @Test
    public void page() {

        List<User> users= Arrays.asList(
                User.builder().username("1").id(1L).createTime(LocalDateTime.of(1,2,3,4,5)).build(),
                User.builder().username("2").id(2L).createTime(LocalDateTime.now()).build(),
                User.builder().username("3").id(3L).createTime(LocalDateTime.now()).build(),
                User.builder().username("4").id(4L).createTime(LocalDateTime.now()).build(),
                User.builder().username("5").id(5L).createTime(LocalDateTime.now()).build(),
                User.builder().username("6").id(6L).createTime(LocalDateTime.now()).build(),
                User.builder().username("7").id(7L).createTime(LocalDateTime.now()).build(),
                User.builder().username("8").id(8L).createTime(LocalDateTime.now()).build(),
                User.builder().username("9").id(9L).createTime(LocalDateTime.now()).build(),
                User.builder().username("10").id(10L).createTime(LocalDateTime.now()).build(),
                User.builder().username("11").id(11L).createTime(LocalDateTime.now()).build(),
                User.builder().username("12").id(12L).createTime(LocalDateTime.now()).build(),
                User.builder().username("13").id(13L).createTime(LocalDateTime.now()).build(),
                User.builder().username("14").id(14L).createTime(LocalDateTime.now()).build(),
                User.builder().username("15").id(15L).createTime(LocalDateTime.now()).build(),
                User.builder().username("16").id(16L).createTime(LocalDateTime.now()).build(),
                User.builder().username("17").id(17L).createTime(LocalDateTime.now()).build(),
                User.builder().username("18").id(18L).createTime(LocalDateTime.now()).build(),
                User.builder().username("19").id(19L).createTime(LocalDateTime.now()).build(),
                User.builder().username("20").id(20L).createTime(LocalDateTime.now()).build(),
                User.builder().username("21").id(21L).createTime(LocalDateTime.now()).build(),
                User.builder().username("22").id(22L).createTime(LocalDateTime.now()).build(),
                User.builder().username("23").id(23L).createTime(LocalDateTime.now()).build(),
                User.builder().username("24").id(24L).createTime(LocalDateTime.now()).build(),
                User.builder().username("25").id(25L).createTime(LocalDateTime.now()).build(),
                User.builder().username("26").id(26L).createTime(LocalDateTime.now()).build(),
                User.builder().username("27").id(27L).createTime(LocalDateTime.now()).build(),
                User.builder().username("28").id(28L).createTime(LocalDateTime.now()).build(),
                User.builder().username("29").id(29L).createTime(LocalDateTime.now()).build(),
                User.builder().username("30").id(30L).createTime(LocalDateTime.now()).build(),
                User.builder().username("31").id(31L).createTime(LocalDateTime.now()).build(),
                User.builder().username("32").id(32L).createTime(LocalDateTime.now()).build()
                );


//        //按id从小到大
//        users.stream()
//                .sorted((u1,u2)->u1.getId().compareTo(u2.getId()))
//                .collect(Collectors.toList());
        //按id从大到小
        users=users.stream()
                .sorted((u1,u2)->u2.getId().compareTo(u1.getId()))
                .sorted((u1,u2)->u2.getCreateTime().compareTo(u1.getCreateTime()))
                .collect(Collectors.toList());

        List<User> list=PageUtils.page(users,4,10);
        list.forEach(i->System.out.println(i));





    }
}