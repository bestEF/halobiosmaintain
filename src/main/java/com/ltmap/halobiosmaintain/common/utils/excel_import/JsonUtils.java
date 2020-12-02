package com.ltmap.halobiosmaintain.common.utils.excel_import;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.List;

public class JsonUtils {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     *
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        try {
            String string = MAPPER.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     *
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list = MAPPER.readValue(jsonData, javaType);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //获取json
    public static String getJson(String pathname) {
        String jsonStr = "";
        try {
            File file = new File(pathname);
            FileReader fileReader = new FileReader(file);
            Reader reader = new InputStreamReader(new FileInputStream(file));
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
//        List<GeneralRegion> generalRegions = new ArrayList<>();
//        List<GeneralRegion> provinceRegions = new ArrayList<>();
//        List<GeneralRegion> cityRegions = new ArrayList<>();
//        List<GeneralRegion> areaRegions = new ArrayList<>();
//        // 获取jaon数据
//        String jsonString = getJson("G:\\data.json");
//
//        Map map = JSONObject.parseObject(jsonString, Map.class);
//        Iterator iterator = map.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry entry = (Map.Entry) iterator.next();
//            // System.out.println("key :"+entry.getKey()+"   value: " + entry.getValue());
//            // 去除{}
//            String content = entry.getValue().toString();
//            content = content.substring(1, content.length() - 1);
//            // 去除""
//            content = content.replaceAll("\"", "");
//            // 去除，
//            String[] areas = content.split(",");
//
//            // 循环数组替换":"
//            for (String s : areas) {
//                String[] result = s.split(":");
//                // 创建对象添加到
//                GeneralRegion region = new GeneralRegion(result[0], result[1]);
//                generalRegions.add(region);
//            }
//        }
//
//        // 遍历list进行格式调整
//        for (GeneralRegion region : generalRegions) {
//            String endCode = region.getAllCode().substring(2, 6);
//            String endCode2 = region.getAllCode().substring(region.getAllCode().length() - 2, region.getAllCode().length());
//            System.out.println(endCode);
//            if (endCode.equals("0000")) {
//                region.setLevel("2");
//                region.setCode(region.getAllCode().substring(0, 2));
//                region.setParentCode("01");
//                region.setPaerentId("1");
//                provinceRegions.add(region);
//            }
//            if (region.getAllCode().length() < 6 && !endCode.equals("0000") && endCode2.equals("00")) {
//                region.setLevel("3");
//                region.setCode(region.getAllCode().substring(2, 4));
//                region.setParentCode(region.getAllCode().substring(0, 2));
//                cityRegions.add(region);
//            }
//            if (!endCode.equals("0000") && !endCode2.equals("00")) {
//                region.setLevel("4");
//                region.setParentCode(region.getAllCode().substring(0, 4));
//                if (region.getAllCode().length() > 6) {
//                    region.setCode(region.getAllCode().substring(6, 9));
//                } else {
//                    region.setCode(region.getAllCode().substring(4, 6));
//                }
//                areaRegions.add(region);
//            }
//        }
//        // 排序
//        Collections.sort(provinceRegions);
//        Collections.sort(cityRegions);
//        Collections.sort(areaRegions);
//        // 先添加省
//
//        // 再加添加市
//
//        // 最后添加区
//
//        System.out.println(areaRegions.size());
//        System.out.println(areaRegions.toString());
//        System.out.println(areaRegions.get(areaRegions.size() - 1).toString());
//
    }

}
