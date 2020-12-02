package com.ltmap.halobiosmaintain.common.utils.excel_import;

/**
 * @author fjh
 * @date 2020/12/1 17:22
 * @email 1562106220@qq.com
 */
public class ParseConstans {
    /**xml中验证规则的名称name**/
    public static String RULE_NAME_NULLABLE = "nullable";//是否非空
    public static String RULE_NAME_LENGTH = "length";//长度是否超出限制
    public static String RULE_NAME_LINE = "allowLine";//校验是否为横线
    public static String RULE_NAME_UNIQUE = "checkUnique";//是否唯一
    public static String RULE_NAME_STRING = "checkString";//是否是字符型
    public static String RULE_NAME_NUMERIC = "checkNumeric";//是否是数字型
    //public static String RULE_NAME_DOUBLE = "checkDouble";//小数位数是否不大于三位
    public static String RULE_NAME_DATE = "checkDate";	//是否是日期格式
    //public static String RULE_NAME_RANGE = "checkRange";//检查数值的范围
    /*public static String RULE_NAME_GRADE = "checkGrade";//检查牌号是否存在*/
    public static String RULE_NAME_SAMPLE = "checkSample";//校验试样编号
    public static String RULE_NAME_MATCH = "checkMatch";//校验试样编号与委托单位匹配性
    public static String RULE_NAME_EXIST = "existData";//是否与存在的数据相符
    public static String RULE_NAME_DIGIT = "isDigit";//是否为整数

    /**excel 中的模板数据错误**/
    public static String ERROR_EXCEL_NULL="excel中数据为空!<br>";
    public static String ERROR_EXCEL_COLUMN_NOT_EQUAL="xml列数与excel列数不相符<br>";
    public static String ERROR_EXCEL_DATA_TYPE = "数据类型错误";
    public static String ERROR_EXCEL_MATCH_MESSAGE = "试样编号与委托单位不匹配";
}