package DAO;
import Model.Holiday;
import Model.Type_holiday;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HolidayDAOimpl implements GenericDAOI<Holiday>{

    @Override
    public void add(Holiday e) {
        String sql = "INSERT INTO holiday (id_employe, startdate, enddate, type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, e.getId_employe());
            stmt.setDate(2, e.getStartDate());
            stmt.setDate(3, e.getEndDate());
            stmt.setString(4, e.getType().name());

            stmt.executeUpdate();
        } catch (SQLException exception) {
        	exception.printStackTrace();
            
           
        } catch (ClassNotFoundException ex) {
            System.err.println("failed connexion with data base");
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM holiday WHERE id = ?";
        try (PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1,id);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            System.err.println("failed of delete holiday");
        } catch (ClassNotFoundException ex) {
            System.err.println("failed connexion with data base");
        }
    }

    @Override
    public void update(Holiday e) {
        String sql = "UPDATE holiday SET id_employe = ?, startdate = ?, enddate = ?, type = ? WHERE id = ?";
        try (PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)){
            stmt.setInt(1, e.getId_employe());
            stmt.setDate(2, e.getStartDate());
            stmt.setDate(3, e.getEndDate());
            stmt.setString(4, e.getType().name());
            stmt.setInt(5,e.getId_holiday());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            System.err.println("failed of update holiday");
        } catch (ClassNotFoundException ex) {
            System.err.println("failed connexion with data base");
        }
    }

    @Override
    public List<Holiday> display() {
        String sql = "SELECT * FROM holiday";
        List<Holiday> Holidays = new ArrayList<>();
        try (PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)) {
            ResultSet re = stmt.executeQuery();
            while (re.next()) {
                int id = re.getInt("id");
                int id_employe = re.getInt("id_employe");
                Date startdate = re.getDate("startdate");
                Date enddate = re.getDate("enddate");
                String type = re.getString("type");
                Holiday e = new Holiday(id, id_employe, startdate, enddate, Type_holiday.valueOf(type));
                Holidays.add(e);
            }
        } catch (ClassNotFoundException ex) {
            System.err.println("Failed to connect with database: " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("Failed to fetch holidays: " + ex.getMessage());
        }
        return Holidays; // Retourne une liste vide si une erreur se produit
    }

}