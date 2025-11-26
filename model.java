package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class model {

    private Connection con;
    private ArrayList<LIVRE> listLivre = new ArrayList<>();
    private ArrayList<AUTEUR> listAuteur = new ArrayList<>();
    private ArrayList<ADHERENT> listAdherent = new ArrayList<>();

    private final String BDD = "ap2_biblio";
    private final String url = "jdbc:mysql://localhost:3306/" + BDD + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Europe/Paris";
    private final String user = "root";
    private final String passwd = "root";

    public model() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(url, user, passwd);
        System.out.println("Connexion à la base de données");
    }

    // 🔹 Charger toutes les données
    public void getall() throws SQLException {
        listAuteur.clear();
        listAdherent.clear();
        listLivre.clear();

        Statement stmt = con.createStatement();
        ResultSet rs;

        // auteurs
        rs = stmt.executeQuery("SELECT * FROM AUTEUR");
        while (rs.next()) {
            int num = rs.getInt("num");
            AUTEUR a = new AUTEUR(
                    num,
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getDate("date_naissance"),
                    rs.getString("description") 
            );
            listAuteur.add(a);
        }

        // adherents
        rs = stmt.executeQuery("SELECT * FROM ADHERENT");
        while (rs.next()) {
            int num = rs.getInt("num");
            ADHERENT ad = new ADHERENT(
                    String.valueOf(num),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    new ArrayList<LIVRE>()
            );
            ad.setNum(num);
            ad.setEmail(rs.getString("email"));
            listAdherent.add(ad);
        }

        // livres
        rs = stmt.executeQuery("SELECT * FROM LIVRE");
        while (rs.next()) {
            int isbn = rs.getInt("ISBN");

            LIVRE l = new LIVRE(
                    String.valueOf(isbn),
                    rs.getString("titre"),
                    rs.getFloat("prix"),
                    null,
                    null
            );
            l.setISBN(isbn);

            // colonne adherent
            int numAdh = rs.getInt("adherent");
            if (!rs.wasNull()) {
                ADHERENT ad = findAdherent(numAdh);
                if (ad != null) {
                    l.setEmprunteur(ad);
                    ad.getEmprunts().add(l);
                }
            }

            // colonne auteur
            int numAut = rs.getInt("auteur");
            if (!rs.wasNull()) {
                AUTEUR a = findAuteur(numAut);
                if (a != null) {
                    l.setAuteur(a);
                }
            }

            listLivre.add(l);
        }

        rs.close();
        stmt.close();

        System.out.println("Chargement terminé : " + listAuteur.size() + " auteurs, "
                + listAdherent.size() + " adhérents, " + listLivre.size() + " livres.");
    }

    //recherche
    public AUTEUR findAuteur(int num) {
        for (AUTEUR a : listAuteur) {
            if (a.getNum() == num) return a;
        }
        return null;
    }

    public ADHERENT findAdherent(int num) {
        for (ADHERENT ad : listAdherent) {
            if (ad.getNum() == num) return ad;
        }
        return null;
    }

    public LIVRE findLivre(int isbn) {
        for (LIVRE l : listLivre) {
            if (l.getISBN() == isbn) return l;
        }
        return null;
    }

    public ArrayList<LIVRE> getListLivre() { return listLivre; }
    public ArrayList<AUTEUR> getListAuteur() { return listAuteur; }
    public ArrayList<ADHERENT> getListAdherent() { return listAdherent; }

    //empr
    public void emprunterLivre(int isbn, int numAdherent) throws SQLException {
        LIVRE livre = findLivre(isbn);
        if (livre == null) throw new SQLException("Livre introuvable");
        if (livre.getEmprunteur() != null) throw new SQLException("Livre déjà emprunté");

        ADHERENT adh = findAdherent(numAdherent);
        if (adh == null) throw new SQLException("Adhérent introuvable");

        String req = "UPDATE LIVRE SET adherent = " + numAdherent + " WHERE ISBN = " + isbn;
        Statement stmt = con.createStatement();
        stmt.executeUpdate(req);
        stmt.close();

        livre.setEmprunteur(adh);
        adh.getEmprunts().add(livre);
    }

	//rest
    public void restituerLivre(int isbn) throws SQLException {
        LIVRE livre = findLivre(isbn);
        if (livre == null) throw new SQLException("Livre introuvable");
        if (livre.getEmprunteur() == null) throw new SQLException("Ce livre n'est pas emprunté");

        String req = "UPDATE LIVRE SET adherent = NULL WHERE ISBN = " + isbn;
        Statement stmt = con.createStatement();
        stmt.executeUpdate(req);
        stmt.close();

        ADHERENT ad = livre.getEmprunteur();
        ad.getEmprunts().remove(livre);
        livre.setEmprunteur(null);
    }


	//maj
    public void ajouterAdherent(ADHERENT ad) throws SQLException {
        String req = "INSERT INTO ADHERENT (num, nom, prenom, email) VALUES (" 
                     + ad.getNum() + ", '" + ad.getNom() + "', '" + ad.getPrenom() + "', '" + ad.getEmail() + "')";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(req);
        stmt.close();
        listAdherent.add(ad);
    }

    public void updateAdherent(ADHERENT ad) throws SQLException {
        String req = "UPDATE ADHERENT SET nom='" + ad.getNom() + "', prenom='" + ad.getPrenom() 
                     + "', email='" + ad.getEmail() + "' WHERE num=" + ad.getNum();
        Statement stmt = con.createStatement();
        stmt.executeUpdate(req);
        stmt.close();
    }

    public void deleteAdherent(int num) throws SQLException {
        String req = "DELETE FROM ADHERENT WHERE num=" + num;
        Statement stmt = con.createStatement();
        stmt.executeUpdate(req);
        stmt.close();
        listAdherent.removeIf(a -> a.getNum() == num);
    }

    public void ajouterAuteur(AUTEUR a) throws SQLException {
        String req = "INSERT INTO AUTEUR (num, nom, prenom, date_naissance, description) VALUES (" 
                     + a.getNum() + ", '" + a.getNom() + "', '" + a.getPrenom() + "', '" 
                     + a.getDate_naissance() + "', '" + a.getText() + "')";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(req);
        stmt.close();
        listAuteur.add(a);
    }

    public void updateAuteur(AUTEUR a) throws SQLException {
        String req = "UPDATE AUTEUR SET nom='" + a.getNom() + "', prenom='" + a.getPrenom() 
                     + "', date_naissance='" + a.getDate_naissance() + "', description='" + a.getText() 
                     + "' WHERE num=" + a.getNum();
        Statement stmt = con.createStatement();
        stmt.executeUpdate(req);
        stmt.close();
    }

    public void deleteAuteur(int num) throws SQLException {
        String req = "DELETE FROM AUTEUR WHERE num=" + num;
        Statement stmt = con.createStatement();
        stmt.executeUpdate(req);
        stmt.close();
        listAuteur.removeIf(a -> a.getNum() == num);
    }


    public void ajouterLivre(LIVRE l) throws SQLException {
        String req = "INSERT INTO LIVRE (ISBN, titre, prix, adherent, auteur) VALUES (" 
                     + l.getISBN() + ", '" + l.getTitre() + "', " + l.getPrix() 
                     + ", " + (l.getEmprunteur() != null ? l.getEmprunteur().getNum() : "NULL") 
                     + ", " + (l.getAuteur() != null ? l.getAuteur().getNum() : "NULL") + ")";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(req);
        stmt.close();

        listLivre.add(l); 
    }
}
