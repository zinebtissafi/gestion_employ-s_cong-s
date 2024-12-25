package DAO;

import java.sql.*;

class DBConnexion {
	 // Déclaration des informations nécessaires pour établir une connexion à la base de données
    public static final String url = "jdbc:mysql://localhost:3306/tp?useSSL=false&serverTimezone=UTC";
    public static final String user = "root";
    public static final String password = "";
    public static Connection conn = null;

    public static Connection getConnexion() throws ClassNotFoundException {
    	// Si une connexion existe déjà, retourner cette connexion
        if (conn != null) {
            return conn;
        }
        try {
          
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("connexion réussie");
        } catch (SQLException e) {
         // Si une erreur se produit lors de la connexion, une exception RuntimeException est lancée
            throw new RuntimeException("Error de connexion", e);
        }
        return conn;
    }
}