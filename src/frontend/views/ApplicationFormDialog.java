package frontend.views;

import database.AdoptionApplicationDAO;
import model.Pet;
import frontend.utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ApplicationFormDialog extends JDialog {
    private Pet pet;
    private int userId;
    private JTextArea reasonArea;
    private AdoptionApplicationDAO applicationDAO;

    public ApplicationFormDialog(Frame owner, Pet pet, int userId) {
        super(owner, "Adopt " + pet.getName(), true);
        this.pet = pet;
        this.userId = userId;
        this.applicationDAO = new AdoptionApplicationDAO();

        setSize(450, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(UITheme.PRIMARY_COLOR);
        JLabel titleLabel = new JLabel("Application for " + pet.getName());
        titleLabel.setFont(UITheme.HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form Body
        JPanel bodyPanel = new JPanel(new BorderLayout(5, 5));
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel promptLabel = new JLabel("Why would you like to adopt " + pet.getName() + "?");
        promptLabel.setFont(UITheme.BODY_FONT);

        reasonArea = new JTextArea(6, 20);
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(reasonArea);

        bodyPanel.add(promptLabel, BorderLayout.NORTH);
        bodyPanel.add(scrollPane, BorderLayout.CENTER);

        add(bodyPanel, BorderLayout.CENTER);

        // Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Submit Application");
        submitButton.setBackground(UITheme.SECONDARY_COLOR);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(UITheme.BUTTON_FONT);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(UITheme.BUTTON_FONT);

        submitButton.addActionListener(e -> handleSubmit());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(submitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleSubmit() {
        String reason = reasonArea.getText().trim();

        if (reason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please explain why you want to adopt this pet.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Calls addApplication which returns the generated app ID (> 0) or -1 on failure
            int appId = applicationDAO.addApplication(userId, pet.getPetId(), reason);

            if (appId != -1) {
                JOptionPane.showMessageDialog(this, "Application submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit application.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}