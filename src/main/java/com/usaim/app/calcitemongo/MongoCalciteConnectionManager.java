package com.usaim.app.calcitemongo;

import org.apache.calcite.jdbc.CalciteConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.StringJoiner;

public class MongoCalciteConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(MongoCalciteConnectionManager.class.getName());

    public static void main(String[] args) {
        String modelPath = MongoCalciteConnectionManager.class.getResource("/mongo-model.json").getPath();
        logger.info("Model Path = {}", modelPath);

        try(Connection connection = DriverManager.getConnection("jdbc:calcite:model=" + modelPath);
            CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
            Statement statement = calciteConnection.createStatement()) {
            String query = "select * from zips";
            ResultSet result = statement.executeQuery(query);
            ResultSetMetaData resultSetMetaData = result.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            long startTime = System.currentTimeMillis();

            int count = 1;
            while (result.next()) {
                if (count == 1) {
                    StringJoiner joiner = new StringJoiner(", ");
                    for (int i = 1; i <= columnCount; i ++) {
                        joiner.add(resultSetMetaData.getColumnName(i));
                    }
                    System.out.println(joiner);

                }
                StringJoiner joiner = new StringJoiner(", ");
                for (int i = 1; i <= columnCount; i ++) {
                    joiner.add(result.getString(i));
                }
                System.out.println(joiner);
                count++;
            }
            logger.info("Total time took {} ms", System.currentTimeMillis() - startTime);
            logger.info("Total of {} records read", count);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
