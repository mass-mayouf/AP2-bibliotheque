package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class model {

    private Connection con;  // co à la BDD
    private ArrayList<LIVRE> listLivre = new ArrayList<>();       // liste de tous les livres
    private ArrayList<AUTEUR> listAuteur = new ArrayList<>();   
    private ArrayList<ADHERENT> listAdherent = new ArrayList<>(); 
    // infos BDD
    private final String BDD = "ap2_biblio";
    private final String url = "jdbc:mysql://localhost:3306/ap2_biblio?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Europe/Paris" + BDD
            + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Europe/Paris";
    private final String user = "root";
    private final String passwd = "root";

    // constructeur du modèle
    public model() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");  // charge le driver MySQL
        con = DriverManager.getConnection(url, user, passwd); // connexion BDD
        System.out.println("Connexion à la base de données");
    }

    // charge toutes les données depuis la BDD
    public void getall() throws SQLException {
        // vider les listes pour éviter les doublons
        listAuteur.clear();
        listAdherent.clear();
        listLivre.clear();

        Statement stmt = con.createStatement();
        ResultSet rs;

        //chargement des auteurs
        rs = stmt.executeQuery("SELECT * FROM AUTEUR");
        while (rs.next()) {
            int num = Integer.parseInt(rs.getString(1)); // numéro auteur
            AUTEUR a = new AUTEUR(
                    String.valueOf(num),
                    rs.getString(2), // nom
                    rs.getString(3), // prénom
                    rs.getDate(4),   // date naissance
                    rs.getString(5)  // nationalité
            );
            a.setNum(num); // juste pour être sûr que le num est correct
            listAuteur.add(a);
        }

        //chargement des adherents
        rs = stmt.executeQuery("SELECT * FROM ADHERENT");
        while (rs.next()) {
            int num = Integer.parseInt(rs.getString(1));
            ADHERENT ad = new ADHERENT(
                    String.valueOf(num),
                    rs.getString(2), // nom
                    rs.getString(3), // prénom
                    new ArrayList<LIVRE>() // initialiser liste vide pour les emprunts
            );
            ad.setNum(num);
            ad.setEmail(rs.getString(4)); // email
            listAdherent.add(ad);
        }

        //chargement des livres
        rs = stmt.executeQuery("SELECT * FROM LIVRE");
        while (rs.next()) {
            int isbn = Integer.parseInt(rs.getString(1));
            LIVRE l = new LIVRE(
                    String.valueOf(isbn),
                    rs.getString(2), // titre
                    rs.getFloat(3),   // prix
                    null,            // auteur à lier après
                    null             // emprunteur à lier après
            );
            l.setISBN(isbn);

            // si le livre est déjà emprunté
            String numAdherentStr = rs.getString(4);
            if (numAdherentStr != null) {
                int numAdh = Integer.parseInt(numAdherentStr);
                ADHERENT ad = findAdherent(numAdh); // chercher adhérent par numéro
                if (ad != null) {
                    l.setEmprunteur(ad);     // lier le livre à l'adhérent
                    ad.getEmprunts().add(l); // ajouter le livre à sa liste d'emprunts
                }
            }

            // lier l'auteur si présent
            String numAuteurStr = rs.getString(5);
            if (numAuteurStr != null) {
                int numAut = Integer.parseInt(numAuteurStr);
                AUTEUR a = findAuteur(numAut); // chercher auteur
                if (a != null) {
                    l.setAuteur(a); // lier livre à l'auteur
                }
            }

            listLivre.add(l); // ajouter le livre à la liste globale
        }

        rs.close();
        stmt.close();

        System.out.println("Chargement terminé : " + listAuteur.size() + " auteurs, "
                + listAdherent.size() + " adhérents, " + listLivre.size() + " livres.");
    }

    // chercher un auteur par numéro
    public AUTEUR findAuteur(int num) {
        for (AUTEUR a : listAuteur) {
            if (a.getNum() == num) return a;
        }
        return null;
    }

    // chercher un adhérent par numéro
    public ADHERENT findAdherent(int num) {
        for (ADHERENT ad : listAdherent) {
            if (ad.getNum() == num) return ad;
        }
        return null;
    }

    // GETTERS / SETTERS pour les listes
    public ArrayList<LIVRE> getListLivre() { return listLivre; }
    public void setListLivre(ArrayList<LIVRE> listLivre) { this.listLivre = listLivre; }

    public ArrayList<AUTEUR> getListAuteur() { return listAuteur; }
    public void setListAuteur(ArrayList<AUTEUR> listAuteur) { this.listAuteur = listAuteur; }

    public ArrayList<ADHERENT> getListAdherent() { return listAdherent; }
    public void setListAdherent(ArrayList<ADHERENT> listAdherent) { this.listAdherent = listAdherent; }

    // méthodes vides pour futur emprunt/restitution
	public void emprunterLivre(int isbn, int num) {
		// TODO à compléter plus tard
	}

	public void restituerLivre(int isbn) {
		// TODO à compléter plus tard
	}
}
