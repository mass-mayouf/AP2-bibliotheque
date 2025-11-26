package view;

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.mainMVC;
import model.ADHERENT;
import model.LIVRE;

public class View_Compte {

    private JFrame frame;
    private JTextField textField;

    public View_Compte() throws SQLException, ClassNotFoundException {
        initialize();
        frame.setVisible(true);
    }

    private void initialize() throws SQLException, ClassNotFoundException {
        // FRAME PRINCIPAL
        frame = new JFrame();
        frame.setTitle("Mon Compte");
        frame.setBounds(100, 100, 700, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // PANEL PRINCIPAL AVEC FOND PASTEL
        JPanel panelMain = new JPanel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(235, 215, 255),   // violet pastel clair
                        0, getHeight(), new Color(250, 225, 255) // rose pastel clair
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        panelMain.setLayout(null);
        frame.setContentPane(panelMain);

        // TITRE
        JLabel lblTitre = new JLabel("MON COMPTE");
        lblTitre.setFont(new Font("Book Antiqua", Font.BOLD, 22));
        lblTitre.setForeground(Color.WHITE); // titre en blanc
        lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitre.setBounds(200, 10, 300, 30);
        panelMain.add(lblTitre);

        // N° ADHERENT
        JLabel lblNumAd = new JLabel("n° adhérent :");
        lblNumAd.setForeground(Color.BLACK); // texte normal en noir
        lblNumAd.setBounds(10, 50, 100, 25);
        panelMain.add(lblNumAd);

        textField = new JTextField();
        textField.setBounds(110, 50, 80, 25);
        panelMain.add(textField);

        JButton btnValider = createRoundedButton("Valider", 200, 50, 100, 30);
        panelMain.add(btnValider);

        // TextFields pour infos
        JTextField txtNom = new JTextField();
        txtNom.setBounds(80, 90, 200, 25);
        txtNom.setVisible(false);
        panelMain.add(txtNom);

        JTextField txtPrenom = new JTextField();
        txtPrenom.setBounds(80, 130, 200, 25);
        txtPrenom.setVisible(false);
        panelMain.add(txtPrenom);

        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(80, 170, 200, 25);
        txtEmail.setVisible(false);
        panelMain.add(txtEmail);

        // Labels infos
        JLabel lblNom = new JLabel("Nom :");
        lblNom.setForeground(Color.BLACK);
        lblNom.setBounds(10, 90, 70, 25);
        lblNom.setVisible(false);
        panelMain.add(lblNom);

        JLabel lblPrenom = new JLabel("Prénom :");
        lblPrenom.setForeground(Color.BLACK);
        lblPrenom.setBounds(10, 130, 70, 25);
        lblPrenom.setVisible(false);
        panelMain.add(lblPrenom);

        JLabel lblEmail = new JLabel("Email :");
        lblEmail.setForeground(Color.BLACK);
        lblEmail.setBounds(10, 170, 70, 25);
        lblEmail.setVisible(false);
        panelMain.add(lblEmail);

        // Bouton mettre à jour
        JButton btnMAJ = createRoundedButton("Mettre à jour", 20, 210, 130, 30);
        btnMAJ.setVisible(false);
        panelMain.add(btnMAJ);

        // Label “Mes livres empruntés”
        JLabel lblLivres = new JLabel("Mes livres empruntés :");
        lblLivres.setFont(new Font("Book Antiqua", Font.BOLD, 16));
        lblLivres.setForeground(Color.BLACK); // texte normal en noir
        lblLivres.setBounds(258, 250, 200, 25);
        lblLivres.setVisible(false);
        panelMain.add(lblLivres);

        // Tableau livres
        String[] colonnes = {"ISBN", "Titre", "Auteur"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Book Antiqua", Font.PLAIN, 14));
        table.setRowHeight(22);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(34, 280, 614, 86);
        scrollPane.setVisible(false);
        panelMain.add(scrollPane);

        // Boutons ACCUEIL, EMPRUNT, RESTITUTION
        JButton btnAccueil = createRoundedButton("ACCUEIL", 269, 380, 140, 35);
        btnAccueil.setVisible(false);
        panelMain.add(btnAccueil);

        JButton btnEmprunt = createRoundedButton("EMPRUNTER", 34, 380, 175, 35);
        btnEmprunt.setVisible(false);
        panelMain.add(btnEmprunt);

        JButton btnRestitution = createRoundedButton("RENDRE UN LIVRE", 448, 380, 200, 35);
        btnRestitution.setVisible(false);
        panelMain.add(btnRestitution);

        // LOGIQUE VALIDER
        Runnable afficherInfos = () -> {
            try {
                mainMVC.getM().getall();
                int numAd = Integer.parseInt(textField.getText().trim());
                ADHERENT ad = mainMVC.getM().getListAdherent()
                        .stream()
                        .filter(a -> a.getNum() == numAd)
                        .findFirst()
                        .orElse(null);

                if (ad != null) {
                    txtNom.setText(ad.getNom());
                    txtPrenom.setText(ad.getPrenom());
                    txtEmail.setText(ad.getEmail());

                    model.setRowCount(0);
                    if (ad.getEmprunts() != null && !ad.getEmprunts().isEmpty()) {
                        for (LIVRE l : ad.getEmprunts()) {
                            String auteur = (l.getAuteur() != null) ? l.getAuteur().getNom() : "—";
                            model.addRow(new Object[]{l.getISBN(), l.getTitre(), auteur});
                        }
                    } else {
                        model.addRow(new Object[]{"-", "Aucun livre emprunté", "-"});
                    }

                    // rendre visible
                    lblNom.setVisible(true);
                    lblPrenom.setVisible(true);
                    lblEmail.setVisible(true);
                    txtNom.setVisible(true);
                    txtPrenom.setVisible(true);
                    txtEmail.setVisible(true);
                    lblLivres.setVisible(true);
                    scrollPane.setVisible(true);
                    btnAccueil.setVisible(true);
                    btnEmprunt.setVisible(true);
                    btnRestitution.setVisible(true);
                    btnMAJ.setVisible(true);
                    btnValider.setVisible(false);
                    textField.setVisible(false);
                    lblNumAd.setVisible(false);

                    btnMAJ.addActionListener(ev -> {
                        try {
                            ad.setNom(txtNom.getText().trim());
                            ad.setPrenom(txtPrenom.getText().trim());
                            ad.setEmail(txtEmail.getText().trim());

                            // 🔥 Appel au modèle pour mettre à jour en BDD
                            mainMVC.getM().updateAdherent(ad);

                            JOptionPane.showMessageDialog(frame, "Adhérent mis à jour dans la base !");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Erreur lors de la mise à jour !");
                        }
                    });

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        textField.addActionListener(e -> afficherInfos.run());
        btnValider.addActionListener(e -> afficherInfos.run());

        btnAccueil.addActionListener(e -> {
            try {
                mainMVC.getM().getall();
                frame.dispose();
                new View_Acceuil();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnEmprunt.addActionListener(e -> {
            frame.dispose();
            try {
                new View_Emprunt();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnRestitution.addActionListener(e -> {
            frame.dispose();
            try {
                new View_Restitution();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    // Création bouton violet pastel plus clair
    private JButton createRoundedButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Book Antiqua", Font.BOLD, 16));
        button.setForeground(Color.WHITE); // texte bouton blanc
        button.setBounds(x, y, width, height);

        Color violetBouton = new Color(200, 170, 255); // violet pastel plus clair
        Color violetHover = new Color(180, 150, 245);  // légèrement plus foncé au hover

        button.setBackground(violetBouton);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(violetHover);
            }
           
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(violetBouton);
            }
        });
        return button;
    }
}
