package com.hehe.nacosxxljob.mapper.funddata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hehe.nacosxxljob.dto.funddatadb.Test2;

public interface Test2Mapper extends BaseMapper<Test2> {
    /**
     * 查询 根据主键 id 查询
     * @author 恒果果
     * @date 2023/01/17
     **/
    Test2 loadTest2(int id);
}
