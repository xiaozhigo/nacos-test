package com.hehe.nacosapply.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hehe.nacosapply.dto.Test1;

public interface Test1Mapper extends BaseMapper<Test1> {
    /**
     * 查询 根据主键 id 查询
     * @author 恒果果
     * @date 2023/01/17
     **/
    Test1 loadTest1(int id);
}
