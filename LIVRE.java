package model;

public class LIVRE {
    private int ISBN;
    private String titre; 
    private float prix;
    private AUTEUR Auteur; 
    private ADHERENT Emprunteur; 

    // Constructeur 
    public LIVRE(int ISBN, String titre, float prix, AUTEUR Auteur, ADHERENT Emprunteur) {
        this.ISBN = ISBN;
        this.titre = titre;
        this.prix = prix;
        this.Auteur = Auteur;
        this.Emprunteur = Emprunteur;
    }

    public LIVRE(String ISBN, String titre, float prix, AUTEUR Auteur, ADHERENT Emprunteur) {
        this.ISBN = Integer.parseInt(ISBN);
        this.titre = titre;
        this.prix = prix;
        this.Auteur = Auteur;
        this.Emprunteur = Emprunteur;
    }

    public LIVRE(String ISBN, String titre, float prix) {
        this.ISBN = Integer.parseInt(ISBN);
        this.titre = titre;
        this.prix = prix;
        this.Auteur = null;
        this.Emprunteur = null;
    }

    public int getISBN() { return ISBN; }
    public void setISBN(int ISBN) { this.ISBN = ISBN; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public float getPrix() { return prix; }
    public void setPrix(float prix) { this.prix = prix; }

    public AUTEUR getAuteur() { return Auteur; }
    public void setAuteur(AUTEUR Auteur) { this.Auteur = Auteur; }

    public ADHERENT getEmprunteur() { return Emprunteur; }
    public void setEmprunteur(ADHERENT Emprunteur) { this.Emprunteur = Emprunteur; }

    // Méthode d'affichage pratique
    
    public String toString() {
        String auteurNom = (Auteur != null) ? Auteur.getNom() : "Inconnu";
        String emprunteurNom = (Emprunteur != null) ? Emprunteur.getNom() : "Disponible";
        return titre + " - " + auteurNom + " (Emprunté par : " + emprunteurNom + ")";
    }
}
