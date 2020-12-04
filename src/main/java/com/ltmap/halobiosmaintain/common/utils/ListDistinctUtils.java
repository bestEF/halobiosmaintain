package com.ltmap.halobiosmaintain.common.utils;

import com.ltmap.halobiosmaintain.entity.work.*;
import org.apache.logging.log4j.util.Strings;

import java.util.*;

/**
 * @program: halobiosmaintain
 * @description: List实体类属性去重
 * @author: Niko
 * @create: 2020-11-30 15:32
 **/
public class ListDistinctUtils {

    /**
     * 大型底栖动物定量生物种名称去重</br>
     * @author Niko
     * @modify_by Niko
     * @param oldList
     * @return
     */
    public static <T extends MacrobenthosQuantitative> List<T> distinctMacrobenthosQuantitativeByMap(List<T> oldList) {
        Map<String, Object> existMap = new HashMap<String, Object>();
        List<T> newList = new ArrayList<>();
        for (T t : oldList) {
            String name = null != existMap.get(t.getBiologicalChineseName()) ? existMap.get(t.getBiologicalChineseName()).toString() : "";
            // 如果name为空表示不存在
            if (Strings.isBlank(name)) {
                existMap.put(t.getBiologicalChineseName(), t.getBiologicalChineseName());
                newList.add(t);
            }
        }
        return newList;
    }

    /*
     * @Description:大型底栖动物定性生物种名称去重
     * @Param oldList:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:30
     */
    public static <T extends MacrobenthosQualitative> List<T> distinctMacrobenthosQualitativeByMap(List<T> oldList) {
        Map<String, Object> existMap = new HashMap<String, Object>();
        List<T> newList = new ArrayList<>();
        for (T t : oldList) {
            String name = null != existMap.get(t.getBiologicalChineseName()) ? existMap.get(t.getBiologicalChineseName()).toString() : "";
            // 如果name为空表示不存在
            if (Strings.isBlank(name)) {
                existMap.put(t.getBiologicalChineseName(), t.getBiologicalChineseName());
                newList.add(t);
            }
        }
        return newList;
    }
    /*
     * @Description:鱼卵定量生物种名称去重
     * @Param oldList:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:30
     */
    public static <T extends FisheggQuantitative> List<T> distinctFisheggQuantitativeByMap(List<T> oldList) {
        Map<String, Object> existMap = new HashMap<String, Object>();
        List<T> newList = new ArrayList<>();
        for (T t : oldList) {
            String name = null != existMap.get(t.getBiologicalChineseName()) ? existMap.get(t.getBiologicalChineseName()).toString() : "";
            // 如果name为空表示不存在
            if (Strings.isBlank(name)) {
                existMap.put(t.getBiologicalChineseName(), t.getBiologicalChineseName());
                newList.add(t);
            }
        }
        return newList;
    }

    /*
     * @Description:鱼卵定性生物种名称去重
     * @Param oldList:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:30
     */
    public static <T extends FisheggQualitative> List<T> distinctFisheggQualitativeByMap(List<T> oldList) {
        Map<String, Object> existMap = new HashMap<String, Object>();
        List<T> newList = new ArrayList<>();
        for (T t : oldList) {
            String name = null != existMap.get(t.getBiologicalChineseName()) ? existMap.get(t.getBiologicalChineseName()).toString() : "";
            // 如果name为空表示不存在
            if (Strings.isBlank(name)) {
                existMap.put(t.getBiologicalChineseName(), t.getBiologicalChineseName());
                newList.add(t);
            }
        }
        return newList;
    }

    /*
     * @Description:仔鱼定量生物种名称去重
     * @Param oldList:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:30
     */
    public static <T extends SmallfishQuantitative> List<T> distinctSmallfishQuantitativeByMap(List<T> oldList) {
        Map<String, Object> existMap = new HashMap<String, Object>();
        List<T> newList = new ArrayList<>();
        for (T t : oldList) {
            String name = null != existMap.get(t.getBiologicalChineseName()) ? existMap.get(t.getBiologicalChineseName()).toString() : "";
            // 如果name为空表示不存在
            if (Strings.isBlank(name)) {
                existMap.put(t.getBiologicalChineseName(), t.getBiologicalChineseName());
                newList.add(t);
            }
        }
        return newList;
    }

    /*
     * @Description:仔鱼定性生物种名称去重
     * @Param oldList:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:30
     */
    public static <T extends SmallfishQualitative> List<T> distinctSmallfishQualitativeByMap(List<T> oldList) {
        Map<String, Object> existMap = new HashMap<String, Object>();
        List<T> newList = new ArrayList<>();
        for (T t : oldList) {
            String name = null != existMap.get(t.getBiologicalChineseName()) ? existMap.get(t.getBiologicalChineseName()).toString() : "";
            // 如果name为空表示不存在
            if (Strings.isBlank(name)) {
                existMap.put(t.getBiologicalChineseName(), t.getBiologicalChineseName());
                newList.add(t);
            }
        }
        return newList;
    }
    /*
     * @Description:游泳动物种名称去重
     * @Param oldList:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:30
     */
    public static <T extends SwimminganimalIdentification> List<T> distinctSwimminganimalIdentificationByMap(List<T> oldList) {
        Map<String, Object> existMap = new HashMap<String, Object>();
        List<T> newList = new ArrayList<>();
        for (T t : oldList) {
            String name = null != existMap.get(t.getSpecificName()) ? existMap.get(t.getSpecificName()).toString() : "";
            // 如果name为空表示不存在
            if (Strings.isBlank(name)) {
                existMap.put(t.getSpecificName(), t.getSpecificName());
                newList.add(t);
            }
        }
        return newList;
    }
    /*
     * @Description:潮间带生物种名称去重
     * @Param oldList:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:30
     */
    public static <T extends IntertidalzonebiologicalQuantitative> List<T> distinctIntertidalzonebiologicalQuantitativeByMap(List<T> oldList) {
        Map<String, Object> existMap = new HashMap<String, Object>();
        List<T> newList = new ArrayList<>();
        for (T t : oldList) {
            String name = null != existMap.get(t.getBiologicalChineseName()) ? existMap.get(t.getBiologicalChineseName()).toString() : "";
            // 如果name为空表示不存在
            if (Strings.isBlank(name)) {
                existMap.put(t.getBiologicalChineseName(), t.getBiologicalChineseName());
                newList.add(t);
            }
        }
        return newList;
    }
    /*
     * @Description:浮游植物种名称去重
     * @Param oldList:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:30
     */
    public static <T extends Phytoplankton> List<T> distinctPhytoplanktonByMap(List<T> oldList) {
        Map<String, Object> existMap = new HashMap<String, Object>();
        List<T> newList = new ArrayList<>();
        for (T t : oldList) {
            String name = null != existMap.get(t.getCategory()) ? existMap.get(t.getCategory()).toString() : "";
            // 如果name为空表示不存在
            if (Strings.isBlank(name)) {
                existMap.put(t.getCategory(), t.getCategory());
                newList.add(t);
            }
        }
        return newList;
    }

    /*
     * @Description:大型浮游动物一型网生物种名称去重
     * @Param oldList:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:30
     */
    public static <T extends LargezooplanktonInet> List<T> distinctLargezooplanktonInetByMap(List<T> oldList) {
        Map<String, Object> existMap = new HashMap<String, Object>();
        List<T> newList = new ArrayList<>();
        for (T t : oldList) {
            String name = null != existMap.get(t.getCategory()) ? existMap.get(t.getCategory()).toString() : "";
            // 如果name为空表示不存在
            if (Strings.isBlank(name)) {
                existMap.put(t.getCategory(), t.getCategory());
                newList.add(t);
            }
        }
        return newList;
    }
    /*
     * @Description:小型浮游动物二型网生物种名称去重
     * @Param oldList:
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/30 16:30
     */
    public static <T extends SmallzooplanktonIinet> List<T> distinctSmallzooplanktonIinetByMap(List<T> oldList) {
        Map<String, Object> existMap = new HashMap<String, Object>();
        List<T> newList = new ArrayList<>();
        for (T t : oldList) {
            String name = null != existMap.get(t.getCategory()) ? existMap.get(t.getCategory()).toString() : "";
            // 如果name为空表示不存在
            if (Strings.isBlank(name)) {
                existMap.put(t.getCategory(), t.getCategory());
                newList.add(t);
            }
        }
        return newList;
    }

}
