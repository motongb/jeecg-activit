package org.jeecg.common.util;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author unknown
 * @Date 2020年3月2日
 * @Time 下午1:41:31
 */
@Slf4j
public class Word {
    /**
     * 占位符初始依赖
     */
    public static final String Symbol = "$";
    /**
     * 验证主体
     */
    public static final Pattern findBody = Pattern.compile("(\\?\\{)([a-zA-Z0-9]+)(})".replace("?", Symbol));
    /**
     * 验证附属主体-后半段
     */
    public static final Pattern surplus = Pattern.compile("(\\{[a-zA-Z0-9]+}|[a-zA-Z0-9]+})");
    /**
     * 文档主体-占位符确定后的截取行为依赖
     */
    public static final Pattern keyInfo = Pattern.compile("[a-zA-Z0-9]+");

    /**
     * 替换文档表格
     *
     * @param xwpfDocument 文档实例
     * @param indexMap     {"表格索引":"表格数据key"}
     * @param params       替换数据
     */
    public static void replaceTable(XWPFDocument xwpfDocument, Map<Integer, String> indexMap, Map<String, Object> params) {
        if (MapUtil.isEmpty(indexMap)) {
            return;
        }
        indexMap.forEach((index, dataKey) -> {
            XWPFTable xwpfTable = null;
            try {
                xwpfTable = xwpfDocument.getTables().get(index);
            } catch (ArrayIndexOutOfBoundsException e) {
                log.error(e.getMessage());
            }
            List<Map<String, Object>> dataMapList = (List<Map<String, Object>>) params.get(dataKey);
            if (xwpfTable != null && !CollectionUtils.isEmpty(dataMapList)) {
                List<XWPFTableRow> rows = xwpfTable.getRows();
                Map<String, Object> map = getTableFields(rows, params);
                int rowIndex = (int) map.get("rowIndex");//寻找字段绑定行索引
                List<String> fields = (List<String>) map.get("fields"); //字段绑定行字段顺序
                insertTable(rowIndex, rows, fields, dataMapList, xwpfTable);
            }
        });
    }

    /**
     * 获取表格字段
     *
     * @param rows
     * @param params
     * @return
     */
    private static Map<String, Object> getTableFields(List<XWPFTableRow> rows, Map<String, Object> params) {
        List<String> fields = null;
        int rowIndex = 0;
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                String key = cell.getText().replaceAll("\\$\\{", "").replaceAll("}", "");
                if (params.get(key) != null) {//找到匹配
                    fields = new ArrayList<>(cells.size());
                    break;
                }
            }
            if (fields != null) {//找到,并获取字段
                fields = cells.stream()
                        .map(cell -> cell
                                .getText()
                                .replaceAll("\\$\\{", "")
                                .replaceAll("}", "")).collect(Collectors.toList());
                break;
            } else {
                rowIndex++;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("rowIndex", rowIndex);
        map.put("fields", fields);
        return map;
    }

    /**
     * 插入表格
     *
     * @param rowIndex
     * @param rows
     * @param fields
     * @param dataMapList
     * @param xwpfTable
     */
    private static void insertTable(int rowIndex, List<XWPFTableRow> rows, List<String> fields, List<Map<String, Object>> dataMapList, XWPFTable xwpfTable) {
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        //从字段绑定行开始插入
        for (Map<String, Object> rowData : dataMapList) {
            XWPFTableRow row = null;
            try {
                row = rows.get(rowIndex);
            } catch (Exception e) {
                row = xwpfTable.createRow();
            }
            if (row != null) {
                List<XWPFTableCell> cells = row.getTableCells();
                int cellIndex = 0;
                for (XWPFTableCell cell : cells) {
                    Object value = rowData.get(fields.get(cellIndex));
                    if (value != null) {
                        cell.removeParagraph(0);
                        XWPFParagraph newPara = cell.addParagraph();
                        XWPFRun run = newPara.createRun();
                        run.setText(value.toString());
                    }
                    cellIndex++;
                }
            }
            rowIndex++;
        }
    }

    /**
     * poi 查找word段落中占位符并替换
     *
     * @param itPara
     * @param map
     * @param empty
     * @param ignorecase
     * @author unknown
     * @Date 2020年3月2日
     * @Time 下午2:02:18
     */
    public static final void ParagraphSearchAndReplace(Iterator<XWPFParagraph> itPara, Map<String, Object> map, String empty, boolean ignorecase) {
        //循环文本主体,文本支值循环一遍
        while (itPara.hasNext()) {
            // 获取一行文本结构主体
            XWPFParagraph tmpBody = itPara.next();
            // 获取一行的文本结构数组
            List<XWPFRun> run = tmpBody.getRuns();
            // 循环文本(每一次循环确定一个占位符)
            for (int runIndex = 0; runIndex < run.size(); runIndex++) {
                // 获取到占位符文本深度(即文本从左到右的非可用长度),避免处理过多的文本
                int runDepth = 0;
                //记录总共深度(即在for里边用了几个runIndex)
                int j = 0;
                //最终确认的文本位置
                int findIndex = runIndex;
                //数据最终的key
                String DivText = null;
                //获取文本节点的第一个
                String NowRunText = run.get(runIndex).getText(run.get(runIndex).getTextPosition());
                if (NowRunText == null) {
                    continue;
                }
                //第一次查找
                Matcher DivFind = findBody.matcher(NowRunText);
                //全文本节点
                String AllRunText = NowRunText;
                //查找到符号位置
                if (NowRunText.contains(Symbol)) {
                    //
                    j = runIndex;
                    //一直循环知道处理完成,直到所有文本全部找到
                    while (!DivFind.find()) {
                        //继续深度处理,记录处理深度
                        runDepth++;
                        j++;
                        //当前文本
                        String NewRunText = run.get(j).getText(run.get(j).getTextPosition());
                        //存在文本
                        if (NewRunText != null) {
                            //拼接全部文本
                            AllRunText += NewRunText;
                        }
                        //查找到符号位置(原位优化程序用的,但是在处理上存在问题所以注释掉)
                        //if(NewRunText.contains(Symbol)){
                        //重置第一个确认字
                        //NowRunText=NewRunText;
                        //重置第一个确认位
                        //findIndex=runDepth;
                        //}
                        //继续深度获取文本
                        DivFind = findBody.matcher(AllRunText);
                    }
                    //重置查找,避免过多运行find找不到参数
                    DivFind = findBody.matcher(AllRunText);
                    //只处理占位符位置,可能存在其他文本,所以使用find
                    if (DivFind.find()) {//之查找一个多余的不动
                        //直接拉取字符
                        DivText = new StringBuffer(AllRunText).substring(DivFind.start(2), DivFind.end(2)).toString();
                        // 忽略大小写
                        if (ignorecase) {
                            //全部置为小写
                            DivText = DivText.toLowerCase();
                        }
//                        if(DivText.contains("sampledate")) {
//                            System.out.println("");
//                        }
                        //判断是否存在文本,是否存在数据
                        if (DivText != null && map.containsKey(DivText)) {
                            //获取数据
                            Object _value = map.get(DivText);
                            //数据转String
                            String value = _value != null ? _value.toString() : empty;
                            //直接替换占位符开始的部位
                            String rText = new StringBuffer(NowRunText).replace(DivFind.start(), DivFind.end(), value).toString();
                            //对单行中存在多个的进行再次捕捉,正则
                            Matcher mg = findBody.matcher(rText);
                            //查找新的占位符
                            while (mg.find()) {
                                //查找新的key
                                String newKey = new StringBuffer(rText).substring(mg.start(2), mg.end(2)).toString();
                                //忽略大小写
                                if (ignorecase) {
                                    //全部置为小写
                                    newKey = newKey.toLowerCase();
                                }
                                //查找新的数据
                                if (newKey != null && map.containsKey(newKey)) {
                                    //获取新的数据
                                    Object _value1 = map.get(newKey);
                                    //数据转String
                                    String value1 = _value1 != null ? _value1.toString() : empty;
                                    //覆盖赋值
                                    rText = new StringBuffer(rText).replace(mg.start(), mg.end(), value1).toString();
                                }
                                //替换word文本
                                mg = findBody.matcher(rText);
                            }
                            //将文本置换
                            run.get(findIndex).setText(rText, 0);
                        } else {
                            //查找不到为空
                            run.get(findIndex).setText(empty, 0);
                        }
                        //清空剩余项(对深度处理的数据进行清理)
                        int g = runIndex;
                        //对深度处理的数据进行清除占位符
                        for (int i = 0; i < runDepth; i++) {
                            g++;
                            //获取要清理的文本
                            String ClearText = run.get(g).getText(run.get(g).getTextPosition());
                            //获取要清理的正则规则
                            Matcher ClearFind = surplus.matcher(ClearText);
                            Matcher ClearKey = keyInfo.matcher(ClearText);
                            //寻找与规则相同的文本
                            if (ClearFind.find()) {
                                //清空规则内的信息(只清除第一个存在的规则文本)
                                ClearText = new StringBuffer(ClearText).replace(ClearFind.start(), ClearFind.end(), "").toString();
                            }
                            //完整规则,不进行查找,确保值只是英文以及数字
                            else if (ClearKey.matches()) {
                                ClearText = new StringBuffer(ClearText).replace(ClearKey.start(), ClearKey.end(), "").toString();
                            }
                            //不存在的直接删除文本开始的第一个字符
                            else {
                                ClearText = ClearText.substring(1);
                            }
                            //重新赋值
                            run.get(g).setText(ClearText, 0);

                            //如果文本中存在占位符头,将深度减去1在次使用
                            if (ClearText.contains(Symbol)) {
                                j--;
                            }
                        }
                        //跳过已经使用的深度循环
                        runIndex = j;
                    }
                }
            }
        }
    }

    /**
     * @param paragraph
     * @param map
     * @param empty
     * @param ignorecase
     * @author unknown
     * @Date 2020年3月2日
     * @Time 下午3:51:17
     */
    public static void ParagraphSearchAndReplace(List<XWPFParagraph> paragraph, Map<String, Object> map, String empty, boolean ignorecase) {
        Word.ParagraphSearchAndReplace(paragraph.iterator(), map, empty, ignorecase);
    }
}