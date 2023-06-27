package com.hehe.nacoscommon.utils;

import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 文件工具类
 * @Description: TODO
 * @create: 2023-04-07 10:51
 **/


public class FileUtils {

    /**
     * 判断文件是否为空
     * @param file
     * @return
     */
    public static Boolean isEmpty(MultipartFile file) {
        if (null == file) {
            return true;
        } else if (file.isEmpty()) {
            return true;
        } else if (file.getSize() == 0) {
            return true;
        }else {
            return false;
        }
    }
}
