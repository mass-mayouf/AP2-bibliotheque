package model;

import java.sql.Date;
import java.time.LocalDate;

public class AUTEUR {
    private int num;
    private String nom;
    private String prenom;
    private Date date_naissance; //compatibilité BDD
    private String text; // description

    // Constructeur
    public AUTEUR(int num, String nom, String prenom, Date date_naissance, String text) {
        this.num = num;
        this.nom = nom;
        this.prenom = prenom;
        this.date_naissance = date_naissance;
        this.text = text;
    }

    // Constructeur avec num en String
    public AUTEUR(String numStr, String nom, String prenom, Date date_naissance, String text) {
        this.num = Integer.parseInt(numStr);
        this.nom = nom;
        this.prenom = prenom;
        this.date_naissance = date_naissance;
        this.text = text;
    }

    public AUTEUR(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.num = 0;
        this.date_naissance = null;
        this.text = "";
    }


    public int getNum() { return num; }
    public void setNum(int num) { this.num = num; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public Date getDate_naissance() { return date_naissance; }
    public void setDate_naissance(Date date_naissance) { this.date_naissance = date_naissance; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    // Pour affichage pratique
    public String toString() {
        String dateStr = (date_naissance != null) ? date_naissance.toString() : "Inconnue";
        return nom + " " + prenom + " (" + text + ") - Né(e) le : " + dateStr;
    }

    // Conversion vers LocalDate si besoin
    public LocalDate getDateNaissanceAsLocalDate() {
        return (date_naissance != null) ? date_naissance.toLocalDate() : null;
    }
}
