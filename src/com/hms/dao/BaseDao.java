package com.hms.dao;

import java.sql.*;

/**
 * @Author: 徐苍凉
 * @Date: 2023/1/3 18:01
 * @Description: JDBC工具
 */
public class BaseDao {
    //1.定义数据库连接属性
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:13306/sms?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    //操作数据库使用到的变量在此定义
    public Connection conn = null;//连接数据库
    public PreparedStatement state = null;//发送SQL到数据库
    public ResultSet resultSet = null;//查询执行后的结果

    //2.加载驱动
    static {
        try {
            Class.forName(DRIVER);
            System.out.println("Driver loading succeeded");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver loading failure");
            e.printStackTrace();
        }
    }

    //3.连接数据库
    //此处使用的是java.sql包
    public Connection getConn() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection is successful");
        } catch (SQLException e) {
            System.out.println("Database connect failure");
            e.printStackTrace();
        }
        return conn;
    }

    //4.进行数据库操作

    /**
     * @param sql
     * @param objects
     * @return 0 or 正整数
     * @Description 数据更新(添加 、 修改 、 删除)
     */
    public int dataUpdate(String sql, Object... objects) throws SQLException {
        getConn();//调用及给全局创建了conn对象

        int count = 0;//统计受到SQL语句影响的语句条数

        state = conn.prepareStatement(sql);//把sql语句封装到对象里

        if (objects != null) {
            //传递过来的对象是放在这个obj数组中的
            for (int i = 0; i < objects.length; i++) {
                // System.out.println(objects[i]);
                state.setObject(i + 1, objects[i]);
            }
            count = state.executeUpdate();
        } else {
            count = 0;
        }

        //关闭资源
        closeResource(conn, state, null);

        //获取方法返回值
        return count;
    }


    /**
     * @param sql
     * @param objects
     * @return ResultSet
     * @Description 数据查询
     */
    public ResultSet dataQuery(String sql, Object... objects) throws SQLException {
        getConn();
        state = conn.prepareStatement(sql);
        if (objects != null) {
            for (int i = 0; i < objects.length; i++) {
                state.setObject(i + 1, objects[i]);
            }
            resultSet = state.executeQuery();
        } else {
            return resultSet;
        }
        return resultSet;
    }

    //5.关闭资源
    public void closeResource(Connection conn, Statement state, ResultSet resultSet) throws SQLException {
        if (conn != null) {
            conn.close();
        }
        if (state != null) {
            state.close();
        }
        if (resultSet != null) {
            resultSet.close();
        }
    }
}
