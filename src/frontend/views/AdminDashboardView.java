package frontend.views;

import database.AdoptionApplicationDAO;
import database.PetDAO;
import model.AdoptionApplication;
import model.Pet;
import frontend.MainFrame;
import frontend.utils.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AdminDashboardView extends JPanel {
    private PetDAO petDAO;
    private AdoptionApplicationDAO applicationDAO;

    private JTable petsTable;
    private DefaultTableModel petsTableModel;

    private JTable applicationsTable;
    private DefaultTableModel applicationsTableModel;

    public AdminDashboardView(MainFrame mainFrame) {
        this.petDAO = new PetDAO();
        this.applicationDAO = new AdoptionApplicationDAO();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG_COLOR);

        // Header Navigation Bar
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(UITheme.PRIMARY_COLOR);
        navBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.LEFT);
        titleLabel.setFont(UITheme.HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(UITheme.BUTTON_FONT);
        logoutButton.addActionListener(e -> mainFrame.showScreen("LOGIN"));

        navBar.add(titleLabel, BorderLayout.WEST);
        navBar.add(logoutButton, BorderLayout.EAST);

        add(navBar, BorderLayout.NORTH);

        // Tabbed Pane for Admin Tasks
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.BODY_FONT);

        tabbedPane.addTab("Manage Pets", createPetManagementPanel());
        tabbedPane.addTab("Review Applications", createApplicationReviewPanel());

        add(tabbedPane, BorderLayout.CENTER);

        loadAllPets();
        loadAllApplications();
    }

    private JPanel createPetManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(UITheme.BG_COLOR);

        String[] columnNames = {"Pet ID", "Name", "Type", "Breed", "Age", "Listing Status", "Pet Status"};
        petsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        petsTable = new JTable(petsTableModel);
        petsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(petsTable);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(UITheme.BG_COLOR);

        JButton approveBtn = new JButton("Approve Listing");
        approveBtn.setBackground(UITheme.SECONDARY_COLOR);
        approveBtn.setForeground(Color.WHITE);

        JButton rejectBtn = new JButton("Reject Listing");
        JButton refreshBtn = new JButton("Refresh List");

        approveBtn.addActionListener(e -> updateSelectedPetListingStatus("Approved"));
        rejectBtn.addActionListener(e -> updateSelectedPetListingStatus("Rejected"));
        refreshBtn.addActionListener(e -> loadAllPets());

        actionPanel.add(approveBtn);
        actionPanel.add(rejectBtn);
        actionPanel.add(refreshBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createApplicationReviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(UITheme.BG_COLOR);

        String[] columnNames = {"App ID", "User ID", "Pet ID", "Reason", "Status"};
        applicationsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        applicationsTable = new JTable(applicationsTableModel);
        applicationsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(applicationsTable);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(UITheme.BG_COLOR);

        JButton approveAppBtn = new JButton("Approve Application");
        approveAppBtn.setBackground(UITheme.SECONDARY_COLOR);
        approveAppBtn.setForeground(Color.WHITE);

        JButton rejectAppBtn = new JButton("Reject Application");
        JButton refreshAppsBtn = new JButton("Refresh Applications");

        approveAppBtn.addActionListener(e -> updateSelectedApplicationStatus("Approved"));
        rejectAppBtn.addActionListener(e -> updateSelectedApplicationStatus("Rejected"));
        refreshAppsBtn.addActionListener(e -> loadAllApplications());

        actionPanel.add(approveAppBtn);
        actionPanel.add(rejectAppBtn);
        actionPanel.add(refreshAppsBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadAllPets() {
        petsTableModel.setRowCount(0);
        try {
            List<Pet> pets = petDAO.getAllPets();
            for (Pet pet : pets) {
                petsTableModel.addRow(new Object[]{
                    pet.getPetId(),
                    pet.getName(),
                    pet.getType(),
                    pet.getBreed(),
                    pet.getAge(),
                    pet.getListingStatus(),
                    pet.getPetStatus()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading pets: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadAllApplications() {
        applicationsTableModel.setRowCount(0);
        try {
            List<AdoptionApplication> apps = applicationDAO.getAllApplications(); 
            for (AdoptionApplication app : apps) {
                applicationsTableModel.addRow(new Object[]{
                    app.getApplicationId(),
                    app.getAdopterId(),          // Replaced getUserId()
                    app.getPetId(),
                    app.getApplicationDetails(), // Replaced getReason()
                    app.getStatus()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading applications: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateSelectedPetListingStatus(String newStatus) {
        int selectedRow = petsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a pet from the table.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int petId = (int) petsTableModel.getValueAt(selectedRow, 0);

        try {
            boolean success = petDAO.updatePetListingStatus(petId, newStatus);
            if (success) {
                JOptionPane.showMessageDialog(this, "Listing status updated to: " + newStatus, "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllPets();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update listing status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateSelectedApplicationStatus(String newStatus) {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an application from the table.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int appId = (int) applicationsTableModel.getValueAt(selectedRow, 0);

        try {
            boolean success = applicationDAO.updateApplicationStatus(appId, newStatus);
            if (success) {
                JOptionPane.showMessageDialog(this, "Application status updated to: " + newStatus, "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllApplications();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update application status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}