package com.hehe.nacosxxljob.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hehe.nacosxxljob.dto.orderdb.Test1;


public interface Test1Mapper  extends BaseMapper<Test1> {
    /**
     * 查询 根据主键 id 查询
     * @author 恒果果
     * @date 2023/01/17
     **/
    Test1 loadTest1(int id);
}
