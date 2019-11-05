package cn.peij.database.doc.gen.database;

import cn.peij.database.doc.gen.bean.ColumnVo;
import cn.peij.database.doc.gen.bean.TableVo;
import cn.peij.database.doc.gen.doc.WordGenerator;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * @author doujiang
 * @date 2019-10-31 13:40
 */
public abstract class AbstractGenerator {
    protected Dao dao;
    protected String dbName;
    protected String docPath;

    public AbstractGenerator(String dbName, SimpleDataSource dataSource) {
        this.dao = new NutDao(dataSource);
        this.dbName = dbName;
        this.docPath = dbName + "-doc";
    }

    /**
     * 获取表结构数据
     *
     * @return 结构数据
     */
    public abstract List<TableVo> getTableData();

    public void generateDoc() {
        File docDir = new File(docPath);
        if (docDir.exists()) {
            String str = "\n【温馨提示】 - 文件夹" + docPath + "已存在。 是否删除?(y 默认删除)\n";
            System.out.print(str);

            Scanner sc = new Scanner(System.in);
            String dbType = sc.nextLine();
            if ("y".equals(dbType) || "".equals(dbType)) {
                docDir.delete();
            } else {
                return;
            }
        } else {
            docDir.mkdirs();
        }
        List<TableVo> list = getTableData();
        save2File(list);
        //保存html
        WordGenerator.createDoc(dbName, list);

    }

    public void save2File(List<TableVo> tables) {
        saveSummary(tables);
        saveReadme(tables);
        for (TableVo tableVo : tables) {
            saveTableFile(tableVo);
        }
    }

    private void saveSummary(List<TableVo> tables) {
        StringBuilder builder = new StringBuilder("# Summary").append("\r\n").append("* [Introduction](README.md)").append("\r\n");
        for (TableVo tableVo : tables) {
            String name = tableVo.getTable();
            builder.append("* [" + name + "](" + tableVo.getTable() + ".md)").append("\r\n");
        }

        try {
            Files.write(new File(docPath + File.separator + "SUMMARY.md"), builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveReadme(List<TableVo> tables) {
        StringBuilder builder = new StringBuilder("# " + dbName + "数据库文档").append("\r\n");
        for (TableVo tableVo : tables) {
            builder.append("- [" + getTableDisplay(tableVo) + "]" + "(" + tableVo.getTable() + ".md)")
                    .append("\r\n");
        }
        try {
            Files.write(new File(docPath + File.separator + "README.md"), builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveTableFile(TableVo table) {
        StringBuilder builder = new StringBuilder("# " + getTableDisplay(table)).append("\r\n");
        builder.append("| 列名   | 类型   | KEY  | 可否为空 | 注释   |").append("\r\n");
        builder.append("| ---- | ---- | ---- | ---- | ---- |").append("\r\n");
        List<ColumnVo> columnVos = table.getColumns();
        boolean isLast = false;
        for (int i = 0; i < columnVos.size(); i++) {
            ColumnVo column = columnVos.get(i);
            if (i == columnVos.size() - 1) {
                isLast = true;
            }
            builder.append("|").append(column.getName())
                    .append("|").append(column.getType())
                    .append("|").append(Strings.sNull(column.getKey()))
                    .append("|").append(column.getIsNullable())
                    .append("|").append(replaceComment(column.getComment(), isLast)).append("|\r\n");
        }
        try {
            Files.write(new File(docPath + File.separator + table.getTable() + ".md"), builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Record> getList(String sqlStr) {
        Sql sql = Sqls.create(sqlStr);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        return sql.getList(Record.class);
    }

    private String getTableDisplay(TableVo table) {
        if (Strings.isBlank(table.getComment())) {
            return table.getTable();
        } else {
            return table.getTable() + "(" + table.getComment() + ")";
        }
    }

    private String replaceComment(String comment, boolean isLast) {
        // 最后一行的空格用 &nbsp; 代替
        if (isLast && Strings.isBlank(comment)) {
            return "&nbsp;";
        }

        // 换行符用 <br> 代替
        // 竖线用 &#124; 代替
        return comment.replaceAll("[\\r\\n]", "<br>").replaceAll("[\\|]", "&#124;");
    }
}