package frontend.views;

import database.PetDAO;
import model.Pet;
import model.User;
import frontend.MainFrame;
import frontend.components.PetCard;
import frontend.utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UserDashboardView extends JPanel {
    private PetDAO petDAO;
    private JPanel petGridPanel;
    private User currentUser;

    public UserDashboardView(MainFrame mainFrame) {
        this.petDAO = new PetDAO();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG_COLOR);

        // Header Navigation Bar
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(UITheme.PRIMARY_COLOR);
        navBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Adoptable Pets", SwingConstants.LEFT);
        titleLabel.setFont(UITheme.HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(UITheme.BUTTON_FONT);
        logoutButton.addActionListener(e -> mainFrame.showScreen("LOGIN"));

        navBar.add(titleLabel, BorderLayout.WEST);
        navBar.add(logoutButton, BorderLayout.EAST);

        add(navBar, BorderLayout.NORTH);

        // Main Grid Panel for Cards
        petGridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        petGridPanel.setBackground(UITheme.BG_COLOR);
        petGridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(petGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        loadPets();
    }

    // Set active session user when logging in
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void loadPets() {
        petGridPanel.removeAll();

        try {
            List<Pet> petList = petDAO.getAvailablePets();

            if (petList == null || petList.isEmpty()) {
                JLabel emptyLabel = new JLabel("No pets available for adoption right now.", SwingConstants.CENTER);
                emptyLabel.setFont(UITheme.BODY_FONT);
                petGridPanel.setLayout(new BorderLayout());
                petGridPanel.add(emptyLabel, BorderLayout.CENTER);
            } else {
                petGridPanel.setLayout(new GridLayout(0, 3, 20, 20));
                for (Pet pet : petList) {
                    petGridPanel.add(new PetCard(pet, () -> openAdoptionDialog(pet)));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching pets: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        petGridPanel.revalidate();
        petGridPanel.repaint();
    }

    private void openAdoptionDialog(Pet pet) {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Frame topFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        // Passes the actual logged-in user's ID
        ApplicationFormDialog dialog = new ApplicationFormDialog(topFrame, pet, currentUser.getUserId());
        dialog.setVisible(true);
    }
}
