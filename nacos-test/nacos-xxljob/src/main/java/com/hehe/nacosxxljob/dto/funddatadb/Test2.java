package com.hehe.nacosxxljob.dto.funddatadb;

import lombok.Data;

import java.io.Serializable;

/**
 * @description test1
 * @author 恒果果
 * @date 2023-01-17 10:55:06
 */
@Data
public class Test2 implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * name
     */
    private String name;

    public Test2() {
    }
}
