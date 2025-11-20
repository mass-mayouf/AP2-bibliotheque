package view;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import controller.mainMVC;
import model.ADHERENT;
import model.LIVRE;

public class View_Emprunt {

    private JFrame frame;
    private JTable table;
    private JTextField txtNumAdherent;
    private JTextField txtISBN;
    private JLabel lblISBN;
    private JButton btnEmprunter;
    private JScrollPane scrollPane;
    private JButton btnAccueil;
    private JLabel lblTitre;
    private DefaultTableModel modelTable;
    private TableRowSorter<DefaultTableModel> sorter;

    
    private final Color violetPastel = new Color(180, 150, 255);
    private final Color violetHover = new Color(160, 120, 245);
    private final Color violetFond = new Color(220, 190, 255);

    public View_Emprunt() throws SQLException, ClassNotFoundException {
        initialize();
        frame.setVisible(true);
    }

    private void initialize() throws SQLException, ClassNotFoundException {

        
        frame = new JFrame();
        frame.setTitle("Emprunt des livres");
        frame.setBounds(100, 100, 750, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(violetFond);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        panel.setLayout(null);
        frame.setContentPane(panel);

        
        lblTitre = new JLabel("EMPRUNT DE LIVRES");
        lblTitre.setFont(new Font("Book Antiqua", Font.BOLD, 28));
        lblTitre.setForeground(Color.WHITE);
        lblTitre.setBounds(200, 20, 400, 35);
        lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitre);

        //Numéro adhérent
        JLabel lblNumAd = new JLabel("Numéro adhérent :");
        lblNumAd.setFont(new Font("Book Antiqua", Font.PLAIN, 16));
        lblNumAd.setBounds(160, 90, 150, 25);
        panel.add(lblNumAd);

        txtNumAdherent = new JTextField();
        txtNumAdherent.setBounds(320, 90, 130, 25);
        panel.add(txtNumAdherent);

        //Bouton valider
        JButton btnValider = new JButton("VALIDER");
        styleButton(btnValider);
        btnValider.setBounds(480, 85, 140, 35);
        panel.add(btnValider);

        //Filtre ISBN
        lblISBN = new JLabel("Filtrer par ISBN :");
        lblISBN.setFont(new Font("Book Antiqua", Font.PLAIN, 16));
        lblISBN.setBounds(80, 90, 150, 30);
        lblISBN.setVisible(false);
        panel.add(lblISBN);

        txtISBN = new JTextField();
        txtISBN.setBounds(220, 90, 150, 30);
        txtISBN.setVisible(false);
        panel.add(txtISBN);

        //tableau
        String[] colonnes = {"ISBN", "Titre", "Auteur"};
        modelTable = new DefaultTableModel(colonnes, 0);
        table = new JTable(modelTable);
        table.setFont(new Font("Book Antiqua", Font.PLAIN, 14));
        table.setRowHeight(22);

        sorter = new TableRowSorter<>(modelTable);
        table.setRowSorter(sorter);

        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(60, 150, 630, 180);
        scrollPane.setVisible(false);
        panel.add(scrollPane);

        //Bouton emprunter
        btnEmprunter = new JButton("EMPRUNTER");
        styleButton(btnEmprunter);
        btnEmprunter.setBounds(500, 350, 150, 40);
        btnEmprunter.setVisible(false);
        panel.add(btnEmprunter);

        
        btnAccueil = new JButton("ACCUEIL");
        styleButton(btnAccueil);
        btnAccueil.setBounds(280, 350, 150, 40);
        btnAccueil.setVisible(false);
        panel.add(btnAccueil);

        //log d'emprunt
        btnValider.addActionListener(e -> {
            try {
                int num = Integer.parseInt(txtNumAdherent.getText());
                ADHERENT ad = mainMVC.getM().findAdherent(num);

                if (ad != null) {

                    lblISBN.setVisible(true);
                    txtISBN.setVisible(true);
                    btnEmprunter.setVisible(true);
                    btnAccueil.setVisible(true);
                    scrollPane.setVisible(true);

                    lblNumAd.setVisible(false);
                    txtNumAdherent.setVisible(false);
                    btnValider.setVisible(false);

                    modelTable.setRowCount(0);
                    for (LIVRE l : mainMVC.getM().getListLivre()) {
                        if (l.getEmprunteur() == null) {
                            String auteur = (l.getAuteur() != null) ? l.getAuteur().getNom() : "—";
                            modelTable.addRow(new Object[]{l.getISBN(), l.getTitre(), auteur});
                        }
                    }

                    txtISBN.addKeyListener(new KeyAdapter() {
                     
                        public void keyReleased(KeyEvent e) {
                            String filter = txtISBN.getText();
                            sorter.setRowFilter(filter.trim().isEmpty() ? null :
                                    RowFilter.regexFilter("(?i)" + filter, 0));
                        }
                    });

                    btnEmprunter.addActionListener(ev -> {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            int modelRow = table.convertRowIndexToModel(selectedRow);
                            int isbn = Integer.parseInt(modelTable.getValueAt(modelRow, 0).toString());

                            try {
                                mainMVC.getM().emprunterLivre(isbn, num);
                                mainMVC.getM().getall();
                                JOptionPane.showMessageDialog(frame, "Livre emprunté !");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    btnAccueil.addActionListener(ev -> {
                        try {
                            mainMVC.getM().getall();
                            frame.dispose();
                            new View_Acceuil();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Aucun adhérent trouvé.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    //style
    private void styleButton(JButton btn) {
        btn.setBackground(violetPastel);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Book Antiqua", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        btn.setOpaque(true);

        btn.addMouseListener(new MouseAdapter() {
            
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(violetHover);
            }

           
            public void mouseExited(MouseEvent e) {
                btn.setBackground(violetPastel);
            }
        });
    }
}
