package frontend.views;

import database.LoginDAO;
import model.User;
import frontend.MainFrame;
import frontend.utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginView extends JPanel {
    private MainFrame mainFrame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private LoginDAO loginDAO;

    public LoginView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.loginDAO = new LoginDAO();

        setLayout(new GridBagLayout());
        setBackground(UITheme.BG_COLOR);

        JPanel card = new JPanel(new GridLayout(0, 1, 10, 10));
        card.setBackground(UITheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 224, 230), 1, true),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel titleLabel = new JLabel("Pet Adoption Platform", SwingConstants.CENTER);
        titleLabel.setFont(UITheme.HEADER_FONT);

        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(UITheme.PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(UITheme.BUTTON_FONT);

        // HERE IS WHERE THE ACTION LISTENER CALLS IT:
        loginButton.addActionListener(e -> handleLogin());

        card.add(titleLabel);
        card.add(new JLabel("Email:"));
        card.add(emailField);
        card.add(new JLabel("Password:"));
        card.add(passwordField);
        card.add(new JSeparator());
        card.add(loginButton);

        add(card);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Validate credentials against DB
            User user = loginDAO.login(email, password);

            // ---> HERE IS THE EXACT BLOCK <---
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + user.getName() + ".");
                
                if ("ADMIN".equalsIgnoreCase(user.getRole()) || "SHELTER".equalsIgnoreCase(user.getRole())) {
                    mainFrame.showScreen("ADMIN_DASHBOARD");
                } else {
                    // Pass the logged-in user to the dashboard view
                    mainFrame.getUserDashboardView().setCurrentUser(user);
                    mainFrame.showScreen("USER_DASHBOARD");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}