package com.hehe.nacoscommon.utils;

import com.hehe.nacoscommon.constant.BizCode;
import com.hehe.nacoscommon.exception.CommonException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 类 名 称：AssertUtil
 * 类 描 述：TODO
 */
public class AssertUtil {
    /**
     * 判断是否为空
     * @param str
     * @param msg
     */
    public static void asser(String str,String msg){
        if (StringUtils.isBlank(str)){
            // 示范 抛出自定义异常
            throw new CommonException(BizCode.COMMON_REQUEST,msg);
        }
    }

    /**
     * 判断是否为空 自定义错误
     * @param str
     * @param bizCode
     */
    public static void asserBiz(String str,BizCode bizCode){
        if (StringUtils.isBlank(str)){
            // 示范 抛出自定义异常
            throw new CommonException(bizCode,bizCode.getMessage());
        }
    }
    /**
     * 判断是否为空
     * @param msg
     */
    public static void asserObj(Object obj,String msg){
        if (null == obj){
            // 示范 抛出自定义异常
            throw new CommonException(BizCode.COMMON_REQUEST,msg);
        }
    }
    /**
     * 判断是否为空
     * @param msg
     */
    public static void asserNull(Integer id,String msg){
        if (null == id){
            // 示范 抛出自定义异常
            throw new CommonException(BizCode.COMMON_REQUEST,msg);
        }
    }

    /**
     * 判断文件是否为空
     * @param file
     * @param msg
     */
  public static void asserFileNull(MultipartFile file, String msg){
        if (FileUtils.isEmpty(file)){
            // 示范 抛出自定义异常
            throw new CommonException(BizCode.COMMON_REQUEST,msg);
        }
    }

    /**
     * 判断是否为空  如果是空则返回true
     * @param str
     */
    public static boolean asserbol(String str){
        if (StringUtils.isBlank(str)){
            // 示范 抛出自定义异常
          return true;
        }
        return false;
    }
}
