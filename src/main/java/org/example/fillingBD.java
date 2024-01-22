package org.example;

import org.example.model.Catalog;
import org.example.model.Plant;

import java.sql.*;
import java.util.List;

public class fillingBD {
    static void fillingTheDatabase(Catalog catalog, List<Plant> plantList) {
        int catalog_id = 0;
        //Параметры подключения
        String url = "jdbc:postgresql://localhost:5432/plants";
        String user = "postgres";
        String password = "1231234923";
        try (Connection connection = DriverManager.getConnection(url, user, password);) {
            String query = "INSERT INTO public.d_cat_catalog (delivery_date,company,uuid) VALUES (?,?,?) RETURNING id";
            try (PreparedStatement mainTableStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
                mainTableStatement.setTimestamp(1, catalog.getDate());
                mainTableStatement.setString(2, catalog.getCompany());
                mainTableStatement.setString(3, catalog.getUuid());
                mainTableStatement.executeUpdate();
                ResultSet generatedKeys = mainTableStatement.getGeneratedKeys();
                System.out.println("Загружены данные в таблицу public.d_cat_catalog");
                if (generatedKeys.next()) {
                    catalog_id = generatedKeys.getInt(1);
                } else {
                    System.out.println("Не удалось получить вставленный первчный ключ");
                    return;
                }
            } catch (SQLException e) {
                System.out.println("Не удалось вставить данные в таблицу public.d_cat_catalog");
                return;
            }
            String insertChildTableQuery = "INSERT INTO public.f_cat_plants (common, botanical, zone, light, price, availability, catalog_id) VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement childTableStatement = connection.prepareStatement(insertChildTableQuery);) {
                for (int i = 0; i < plantList.size(); i++) {
                    childTableStatement.setString(1, plantList.get(i).getCommon());
                    childTableStatement.setString(2, plantList.get(i).getBotanical());
                    childTableStatement.setInt(3, plantList.get(i).getZone());
                    childTableStatement.setString(4, plantList.get(i).getLight());
                    childTableStatement.setDouble(5, plantList.get(i).getPrice());
                    childTableStatement.setInt(6, plantList.get(i).getAvailability());
                    childTableStatement.setInt(7, catalog_id);
                    childTableStatement.executeUpdate();
                }
                System.out.println("Загружены данные в таблицу public.f_cat_plants");
            } catch (SQLException e) {
                System.out.println("Не удалось вставить данные в таблицу public.f_cat_plants");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
