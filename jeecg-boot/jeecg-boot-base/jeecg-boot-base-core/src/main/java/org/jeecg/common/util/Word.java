package org.jeecg.common.util;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.jeecg.common.api.dto.wps.WpsTableStyle;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
            WpsTableStyle tableStyle = null;
            try {
                xwpfTable = xwpfDocument.getTables().get(index);
                XWPFTableRow headerRow = xwpfTable.getRow(1);
                XWPFRun run = headerRow.getTableCells().get(0).getParagraphs().get(0).getRuns().get(0);
                tableStyle = new WpsTableStyle(run.isBold(), run.getFontFamily(), run.getFontSize(), headerRow.getHeight());
            } catch (ArrayIndexOutOfBoundsException e) {
                log.error(e.getMessage());
            }
            List<Map<String, Object>> dataMapList = (List<Map<String, Object>>) params.get(dataKey);
            if (xwpfTable != null && !CollectionUtils.isEmpty(dataMapList)) {
                List<XWPFTableRow> rows = xwpfTable.getRows();
                Map<String, Object> map = getTableFields(rows, dataMapList.get(0));
                int rowIndex = (int) map.get("rowIndex");//寻找字段绑定行索引
                List<String> fields = (List<String>) map.get("fields"); //字段绑定行字段顺序
                insertTable(rowIndex, rows, fields, dataMapList, xwpfTable, tableStyle);
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
        // 开始行索引
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
            //找到,并获取字段
            if (fields != null) {
                fields = cells.stream().map(XWPFTableCell::getText).collect(Collectors.toList());
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
    private static void insertTable(int rowIndex, List<XWPFTableRow> rows, List<String> fields, List<Map<String, Object>> dataMapList, XWPFTable xwpfTable, WpsTableStyle tableStyle) {
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
                row.setHeight(tableStyle.getHeight());
                int cellIndex = 0;
                for (XWPFTableCell cell : cells) {
                    String text = fields.get(cellIndex);
                    Set<String> keys = getMatcherKey(text);
                    if (!keys.isEmpty()) {
                        text = textReplace(keys, text, rowData);
                        XWPFParagraph newPara = cell.getParagraphArray(0);
                        for (int i = newPara.getRuns().size() - 1; i >= 0; i--) {
                            newPara.removeRun(i);
                        }
                        XWPFRun run = newPara.createRun();
                        run.setBold(tableStyle.isBold());
                        run.setFontFamily(tableStyle.getFontFamily());
                        run.setFontSize(tableStyle.getFontSize());
                        run.setText(text);
                    }
                    cellIndex++;
                }
            }
            rowIndex++;
        }
    }


    /**
     * 段落替换
     *
     * @param paragraphs
     * @param map
     */
    public static void paragraphReplace(List<XWPFParagraph> paragraphs, Map<String, Object> map) {
        for (XWPFParagraph paragraph : paragraphs) {
            paragraphReplace(paragraph, map);
        }
    }

    /**
     * 段落替换
     *
     * @param paragraph
     * @param map
     */
    public static void paragraphReplace(XWPFParagraph paragraph, Map<String, Object> map) {
        String text = paragraph.getParagraphText();
        Set<String> keys = getMatcherKey(text);
        if (StringUtils.hasText(text) && !keys.isEmpty()) {
            log.info(text);
            // 获取原样式
            List<XWPFRun> runs = paragraph.getRuns();
            boolean isBold = runs.get(0).isBold();
            String fontFamily = runs.get(0).getFontFamily();
            int fontSize = runs.get(0).getFontSize();
            // 文本替换
            text = textReplace(keys, text, map);
            for (int i = runs.size() - 1; i >= 0; i--) {
                paragraph.removeRun(i);
            }
            XWPFRun createRun = paragraph.insertNewRun(0);
            createRun.setText(text);
            createRun.setBold(isBold);
            createRun.setFontFamily(fontFamily);
            createRun.setFontSize(fontSize);
        }
    }

    /**
     * 文本替换
     *
     * @param keys
     * @param text
     * @param map
     * @return
     */
    private static String textReplace(Set<String> keys, String text, Map<String, Object> map) {
        for (String key : keys) {
            Object value = map.get(key.trim());
            text = text.replaceAll("\\$\\{" + key + "\\}", value == null ? "" : value.toString());
        }
        return text;
    }

    /**
     * 获取keys
     *
     * @param str
     * @return
     */
    private static Set<String> getMatcherKey(String str) {
        Pattern pattern = Pattern.compile("(?<=\\$\\{)(.+?)(?=\\})", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        Set<String> keys = new HashSet<>();
        while (matcher.find()) {
            keys.add(matcher.group());
        }
        return keys;
    }
}