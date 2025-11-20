package controller;

import java.sql.SQLException;

import model.model;
import view.View_Acceuil;

public class mainMVC {

    private static model m;

    public static model getM() {
        return m;// retourne l'objet modèle pour pouvoir l'utiliser dans les vues
    }

    public static void main(String[] args) {
        System.out.println("Lancement du programme...");

        try {
            // créer le modèle (connexion BDD, initialisation des listes)
            m = new model();

            // Charger toutes les données depuis la BDD
            m.getall();
            //nombre de
            System.out.println("Données chargées : " 
                + m.getListAuteur().size() + " auteurs, "
                + m.getListAdherent().size() + " adhérents, "
                + m.getListLivre().size() + " livres.");

            // Lancer la vue
            View_Acceuil va = new View_Acceuil(); //lance la vue principal


        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Driver JDBC introuvable !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la connexion ou du chargement des données !");
            e.printStackTrace();
        }
    }
}
