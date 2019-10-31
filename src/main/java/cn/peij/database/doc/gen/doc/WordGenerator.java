package cn.peij.database.doc.gen.doc;

import cn.peij.database.doc.gen.bean.ColumnVo;
import cn.peij.database.doc.gen.bean.TableVo;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author doujiang
 * @date 2019-10-31 13:43
 */
public class WordGenerator {
    private static Configuration configuration;

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setDefaultEncoding("utf-8");
        ClassTemplateLoader loader = new ClassTemplateLoader(WordGenerator.class, "/");
        configuration.setTemplateLoader(loader);
    }

    private WordGenerator() {
        throw new AssertionError();
    }

    public static void createDoc(String dbName, List<TableVo> list) {
        Map map = new HashMap(2);
        map.put("dbName", dbName);
        map.put("tables", list);
        try {
            Template template = configuration.getTemplate("database.html");
            String name = dbName + "-doc" + File.separator + dbName + ".html";
            File f = new File(name);
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            template.process(map, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }


    public static void main(String[] args) {
        List<TableVo> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TableVo tableVo = new TableVo();
            tableVo.setTable("表" + i);
            tableVo.setComment("注释" + i);
            List<ColumnVo> columns = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                ColumnVo columnVo = new ColumnVo();
                columnVo.setName("name" + j);
                columnVo.setComment("注释" + j);
                columnVo.setKey("PRI");
                columnVo.setIsNullable("是");
                columnVo.setType("varchar(2");
                columns.add(columnVo);

            }
            tableVo.setColumns(columns);
            list.add(tableVo);
        }

        createDoc("test", list);

    }

}