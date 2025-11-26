package model;

import java.util.ArrayList;

public class ADHERENT {
    private int num;                
    private String nom;             
    private String prenom;          
    private String email;           
    private ArrayList<LIVRE> emprunts; // liste des livres empruntés

    public ADHERENT(int num, String nom, String prenom, String email) {
        this.num = num;             
        this.nom = nom;             
        this.prenom = prenom;       
        this.email = email;         
        this.emprunts = new ArrayList<>(); // initialise liste vide
    }

    public ADHERENT(String numStr, String nom, String prenom, ArrayList<LIVRE> emprunts) {
        this.num = Integer.parseInt(numStr); // convertit String -> int
        this.nom = nom;
        this.prenom = prenom;
        this.email = "";                    
        this.emprunts = emprunts != null ? emprunts : new ArrayList<>(); // si null, crée liste vide
    }

    public ADHERENT(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.num = 0;                        
        this.email = "";                     
        this.emprunts = new ArrayList<>();   
    }

    public int getNum() { return num; }
    public void setNum(int num) { this.num = num; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public ArrayList<LIVRE> getEmprunts() { return emprunts; } 
    public void setEmprunts(ArrayList<LIVRE> emprunts) { this.emprunts = emprunts; }

    public void afficherListeLivre() {
        if (emprunts.isEmpty()) {
            System.out.println(nom + " n’a emprunté aucun livre."); 
        } else {
            System.out.println("Livres empruntés par " + nom + " :");
            for (LIVRE l : emprunts) { 
                System.out.println(l.toString()); 
            }
        }
    }

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + email + ")"; // pour afficher l'objet
    }
}
