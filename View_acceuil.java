package view;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.LineBorder;

import controller.mainMVC;

public class View_Acceuil {

    private JFrame frame;

    public View_Acceuil() throws SQLException, ClassNotFoundException {
        mainMVC.getM().getall();
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        // FRAME PRINCIPAL
        frame = new JFrame();
        frame.setTitle("Accueil - Bibliothèque");
        frame.setBounds(100, 100, 800, 450);
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

                // 🌸 Dégradé pastel lavande → rose lavande clair
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(235, 215, 255),   // lavande très clair
                        0, getHeight(), new Color(250, 225, 255) // rose-violet pastel
                );

                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };

        panelMain.setLayout(null);
        frame.setContentPane(panelMain);

        // TITRE
        JLabel lblTitre = new JLabel("ACCUEIL");
        lblTitre.setFont(new Font("Book Antiqua", Font.BOLD, 32));
        lblTitre.setForeground(Color.WHITE);
        lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitre.setBounds(250, 20, 300, 50);
        panelMain.add(lblTitre);

        // BOUTONS
        JButton btnCatalogue = createRoundedButton("CATALOGUE", 50, 150, 150, 180);
        JButton btnCompte = createRoundedButton("MON COMPTE", 230, 150, 150, 180);
        JButton btnEmprunt = createRoundedButton("EMPRUNT", 410, 150, 150, 180);
        JButton btnRestitution = createRoundedButton("RESTITUTION", 590, 150, 150, 180);

        panelMain.add(btnCatalogue);
        panelMain.add(btnCompte);
        panelMain.add(btnEmprunt);
        panelMain.add(btnRestitution);

        // ACTIONS
        btnCatalogue.addActionListener(e -> openView(() -> {
			try {
				new View_Catalogue();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}));
        
        btnCompte.addActionListener(e -> openView(() -> {
			try {
				new View_Compte();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}));
        btnEmprunt.addActionListener(e -> openView(() -> {
			try {
				new View_Emprunt();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}));
        
        btnRestitution.addActionListener(e -> openView(() -> {
			try {
				new View_Restitution();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}));

    }

    private JButton createRoundedButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Book Antiqua", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBounds(x, y, width, height);

        // 🌼 Couleur bouton pastel violet
        Color violetPastel = new Color(180, 150, 255);   // violet doux
        Color violetHover = new Color(160, 120, 245);    // hover plus foncé

        button.setBackground(violetPastel);
        button.setOpaque(true);

        button.setFocusPainted(false);
        button.setBorder(new LineBorder(new Color(230, 210, 255), 2, true));  // bord pastel arrondi


        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(violetHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(violetPastel);
            }
        });

        return button;
    }

    private void openView(Runnable view) {
        frame.dispose();
        try {
            view.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
