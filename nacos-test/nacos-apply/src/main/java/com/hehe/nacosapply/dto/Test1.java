package com.hehe.nacosapply.dto;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class Test1 implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * name
     */
    private String name;

    public Test1() {
    }
}
