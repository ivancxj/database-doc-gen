package cn.peij.database.doc.gen.bean;

import java.util.List;

/**
 * @author doujiang
 * @date 2019-10-31 13:40
 */
public class TableVo {
    private String table;
    private String comment;
    private List<ColumnVo> columns;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ColumnVo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnVo> columns) {
        this.columns = columns;
    }
}
