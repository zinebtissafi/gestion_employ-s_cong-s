package Controller;
import Model.*;
import View.*;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class EmployeController {
    private final Employe_HolidayView View;
    public static EmployeModel model_employe;
    public static int id = 0;
    public static int oldselectedrow = -1;
    public static boolean test = false;
    String nom = "";
    String prenom = "";
    String email = "";
    String telephone = "";
    double salaire = 0;
    Role role = null;
    Post poste = null;
    int solde = 0;
    boolean updatereussi = false;

    // Constructeur pour initialiser la vue et le modèle, et ajouter les écouteurs d'événements
    public EmployeController(Employe_HolidayView view, EmployeModel model) {
        this.View = view;
        this.model_employe = model;

        // Écouteurs pour les boutons d'ajout, suppression, mise à jour et affichage
        View.getaddButton_employe().addActionListener(e -> addEmploye());
        View.getdeleteButton_employe().addActionListener(e -> deleteEmploye());
        View.getupdateButton_employe().addActionListener(e -> updateEmploye());
        View.getdisplayButton_employe().addActionListener(e -> displayEmploye());
        Employe_HolidayView.Tableau.getSelectionModel().addListSelectionListener(e -> updateEmployebyselect());
    }

    // Afficher tous les employés dans le tableau
    public void displayEmploye() {
        List<Employe> Employes = model_employe.displayEmploye();
        if (Employes.isEmpty()) {
            View.afficherMessageErreur("Aucun employé trouvé.");
        }
        DefaultTableModel tableModel = (DefaultTableModel) Employe_HolidayView.Tableau.getModel();
        tableModel.setRowCount(0); // Réinitialiser le tableau
        for (Employe e : Employes) {
            tableModel.addRow(new Object[]{e.getId(), e.getNom(), e.getPrenom(), e.getEmail(), e.getTelephone(), e.getSalaire(), e.getRole(), e.getPost(), e.getSolde()});
        }
        View.remplaire_les_employes();
    }

    // Ajouter un nouvel employé
    private void addEmploye() {
        // Récupérer les données saisies dans la vue
        String nom = View.getNom();
        String prenom = View.getPrenom();
        String email = View.getEmail();
        String telephone = View.getTelephone();
        double salaire = View.getSalaire();
        Role role = View.getRole();
        Post poste = View.getPoste();

        // Vider les champs de saisie après la récupération des données
        View.viderChamps_em();

        // Ajouter l'employé dans le modèle
        boolean addreussi = model_employe.addEmploye(0, nom, prenom, email, telephone, salaire, role, poste, 25);

        // Afficher un message de succès ou d'erreur
        if (addreussi) {
            View.afficherMessageSucces("Employé ajouté avec succès !");
            displayEmploye();
        } else {
            View.afficherMessageErreur("Échec de l'ajout de l'employé.");
        }
    }
    // Supprimer un employé
    private void deleteEmploye() {
        int selectedrow = Employe_HolidayView.Tableau.getSelectedRow();
        if (selectedrow == -1) {
            View.afficherMessageErreur("Veuillez sélectionner une ligne.");
        } else {
            // Récupérer l'ID de l'employé à supprimer
            int id = (int) Employe_HolidayView.Tableau.getValueAt(selectedrow, 0);
            if (model_employe.deleteEmploye(id)) {
                View.afficherMessageSucces("Employé supprimé avec succès !");
                displayEmploye();
            } else {
                View.afficherMessageErreur("Échec de la suppression de l'employé.");
            }
        }
    }

    // Sélectionner un employé pour mise à jour à partir du tableau
    private void updateEmployebyselect() {
        int selectedrow = Employe_HolidayView.Tableau.getSelectedRow();

        if (selectedrow == -1) {
            return; // Aucun employé sélectionné
        }
        try {
            // Récupérer les données de l'employé sélectionné
            id = (int) Employe_HolidayView.Tableau.getValueAt(selectedrow, 0);
            nom = (String) Employe_HolidayView.Tableau.getValueAt(selectedrow, 1);
            prenom = (String) Employe_HolidayView.Tableau.getValueAt(selectedrow, 2);
            email = (String) Employe_HolidayView.Tableau.getValueAt(selectedrow, 3);
            telephone = (String) Employe_HolidayView.Tableau.getValueAt(selectedrow, 4);
            salaire = (double) Employe_HolidayView.Tableau.getValueAt(selectedrow, 5);
            role = (Role) Employe_HolidayView.Tableau.getValueAt(selectedrow, 6);
            poste = (Post) Employe_HolidayView.Tableau.getValueAt(selectedrow, 7);
            solde = (int) Employe_HolidayView.Tableau.getValueAt(selectedrow, 8);
            View.remplaireChamps_em(id, nom, prenom, email, telephone, salaire, role, poste);
            test = true; // Indiquer qu'une ligne est sélectionnée
        } catch (Exception e) {
            View.afficherMessageErreur("Erreur lors de la récupération des données.");
        }
    }

    // Mettre à jour les informations d'un employé
    private void updateEmploye() {
        if (!test) {
            View.afficherMessageErreur("Veuillez d'abord sélectionner une ligne à modifier.");
            return;
        }
        try {
            // Récupérer les nouvelles données saisies
            nom = View.getNom();
            prenom = View.getPrenom();
            email = View.getEmail();
            telephone = View.getTelephone();
            salaire = View.getSalaire();
            role = View.getRole();
            poste = View.getPoste();

            // Effectuer la mise à jour dans le modèle
            boolean updateSuccessful = model_employe.updateEmploye(id, nom, prenom, email, telephone, salaire, role, poste, solde);

            if (updateSuccessful) {
                test = false; // Réinitialiser l'indicateur
                View.afficherMessageSucces("Employé mis à jour avec succès.");
                displayEmploye();
                View.viderChamps_em(); // Vider les champs
            } else {
                View.afficherMessageErreur("Erreur lors de la mise à jour de l'employé.");
            }
        } catch (Exception e) {
            View.afficherMessageErreur("Erreur lors de la mise à jour.");
        }
    }

    // Réinitialiser le solde des congés de tous les employés au début de l'année
    public void resetSolde() {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.DAY_OF_YEAR) == 1) { // Vérifier si c'est le 1er janvier
            for (Employe employe : model_employe.displayEmploye()) {
                updateSolde(employe.getId(), 25); // Réinitialiser à 25 jours
            }
        }
    }

    // Mettre à jour le solde des congés pour un employé spécifique
    public static void updateSolde(int id, int solde) {
        boolean updateSuccessful = model_employe.updateSolde(id, solde);
    }
}
