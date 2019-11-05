package cn.peij.database.doc.gen;

import cn.peij.database.doc.gen.database.AbstractGenerator;
import cn.peij.database.doc.gen.database.Mysql;
import org.nutz.dao.impl.SimpleDataSource;

import java.util.Scanner;

/**
 * @author doujiang
 * @date 2019-10-31 13:38
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("input host (default " + getDefaultHost() + ") :");
        String ip = sc.nextLine();
        if ("".equals(ip)) {
            ip = getDefaultHost();
        }

        System.out.println("input port (default " + getDefaultPort() + ") :");
        String port = sc.nextLine();
        if ("".equals(port)) {
            port = getDefaultPort();
        }

        System.out.println("input username (default " + getDefaultUser() + ") :");
        String username = sc.nextLine();
        if ("".equals(username)) {
            username = getDefaultUser();
        }

        System.out.println("input password (default " + getDefaultPassword() + ") :");
        String password = sc.nextLine();
        if ("".equals(password)) {
            password = getDefaultPassword();
        }

        System.out.println("input database name(default " + getDefaultDatabase() + ") :");
        String dbName = sc.nextLine();
        if ("".equals(dbName)) {
            dbName = getDefaultDatabase();
        }

        SimpleDataSource dataSource = new SimpleDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?useSSL=false");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        AbstractGenerator generator = new Mysql(dbName, dataSource);
        generator.generateDoc();
    }

    private static String getDefaultHost() {
        return "10.1.21.143";
    }

    private static String getDefaultPort() {
        return "3306";
    }

    private static String getDefaultUser() {
        return "root";
    }

    private static String getDefaultPassword() {
        return "Mysql_123";
    }

    private static String getDefaultDatabase() {
        return "ark_manager";
    }
}