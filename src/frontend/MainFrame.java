package frontend;

import frontend.views.AdminDashboardView;
import frontend.views.LoginView;
import frontend.views.UserDashboardView;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private UserDashboardView userDashboardView;

    public MainFrame() {
        setTitle("Online Pet Adoption Platform");
        setSize(1024, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        userDashboardView = new UserDashboardView(this);

        // Register screens
        mainContainer.add(new LoginView(this), "LOGIN");
        mainContainer.add(userDashboardView, "USER_DASHBOARD");
        mainContainer.add(new AdminDashboardView(this), "ADMIN_DASHBOARD");

        add(mainContainer);
        showScreen("LOGIN");
    }

    public UserDashboardView getUserDashboardView() {
        return userDashboardView;
    }

    public void showScreen(String screenName) {
        cardLayout.show(mainContainer, screenName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}