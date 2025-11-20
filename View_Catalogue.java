package view;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import controller.mainMVC;
import model.LIVRE;

public class View_Catalogue {

    private JFrame frame;
    private JTable table;

    // constructeur : initialise la vue et l'affiche
    public View_Catalogue() throws SQLException, ClassNotFoundException {
        initialize();
        frame.setVisible(true);
    }

    private void initialize() throws SQLException, ClassNotFoundException {
        // créer la fenêtre
        frame = new JFrame();
        frame.setTitle("Catalogue - Bibliothèque");
        frame.setBounds(100, 100, 800, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // centre la fenêtre

        // panel principal avec fond violet pastel et coins arrondis
        JPanel panelMain = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // fond violet pastel très clair
                g2d.setColor(new Color(220, 190, 255));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        panelMain.setLayout(null);
        frame.setContentPane(panelMain);

        // titre de la page
        JLabel lblTitre = new JLabel("Catalogue");
        lblTitre.setFont(new Font("Book Antiqua", Font.BOLD, 30));
        lblTitre.setForeground(Color.WHITE); // titre en blanc
        lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitre.setBounds(250, 20, 300, 40);
        panelMain.add(lblTitre);

        // créer le tableau pour afficher les livres
        String[] colonnes = {"ISBN", "Titre", "Auteur", "Disponibilité"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0);
        table = new JTable(model);
        table.setFont(new Font("Book Antiqua", Font.PLAIN, 14));
        table.setRowHeight(22);
        table.setGridColor(new Color(150, 90, 200)); // couleur des lignes
        table.setSelectionBackground(new Color(180, 140, 230)); // surbrillance
        table.setSelectionForeground(Color.WHITE); // texte sélectionné

        // remplir le tableau avec les livres de la base
        for (LIVRE l : mainMVC.getM().getListLivre()) {
            String auteur = (l.getAuteur() != null) ? l.getAuteur().getNom() : "—"; // vérifier auteur
            String dispo = (l.getEmprunteur() == null) ? "Disponible" : "Emprunté"; // vérifier si emprunté
            Object[] row = { l.getISBN(), l.getTitre(), auteur, dispo };
            model.addRow(row);
        }

        // mettre le tableau dans un scroll
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(70, 90, 650, 230);
        scrollPane.setBorder(new LineBorder(new Color(180, 130, 240), 2, true));
        panelMain.add(scrollPane);

        // bouton accueil
        JButton btnAccueil = new JButton("Accueil");
        Color violetPastel = new Color(180, 150, 255); // couleur normale
        Color violetHover = new Color(160, 120, 245);  // couleur au survol

        btnAccueil.setBackground(violetPastel);
        btnAccueil.setForeground(Color.WHITE); // texte en blanc
        btnAccueil.setFont(new Font("Book Antiqua", Font.BOLD, 16));
        btnAccueil.setBounds(330, 340, 140, 45);
        btnAccueil.setFocusPainted(false);
        btnAccueil.setBorder(new LineBorder(new Color(230, 210, 255), 2, true));
        btnAccueil.setOpaque(true);

        // effet survol
        btnAccueil.addMouseListener(new MouseAdapter() {
           
            public void mouseEntered(MouseEvent e) {
                btnAccueil.setBackground(violetHover);
            }

         
            public void mouseExited(MouseEvent e) {
                btnAccueil.setBackground(violetPastel);
            }
        });

        // action pour revenir à l'accueil
        btnAccueil.addActionListener(e -> {
            frame.dispose(); // fermer cette vue
            try {
                new View_Acceuil(); // ouvrir accueil
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        panelMain.add(btnAccueil);
    }
}
