package frontend.components;

import model.Pet;
import frontend.utils.UITheme;

import javax.swing.*;
import java.awt.*;

public class PetCard extends JPanel {
    private Pet pet;

    public PetCard(Pet pet, Runnable onAdoptClicked) {
        this.pet = pet;

        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.CARD_BG);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 224, 230), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Pet Details
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(UITheme.CARD_BG);

        JLabel nameLabel = new JLabel(pet.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setForeground(UITheme.PRIMARY_COLOR);

        // Uses getType() instead of getSpecies() to match your DAO schema
        JLabel detailsLabel = new JLabel(pet.getType() + " • " + pet.getBreed() + " • " + pet.getAge() + " yrs");
        detailsLabel.setFont(UITheme.BODY_FONT);
        detailsLabel.setForeground(Color.GRAY);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(detailsLabel);

        // Adopt Button
        JButton adoptButton = new JButton("Adopt Me");
        adoptButton.setBackground(UITheme.SECONDARY_COLOR);
        adoptButton.setForeground(Color.WHITE);
        adoptButton.setFont(UITheme.BUTTON_FONT);
        adoptButton.setFocusPainted(false);
        adoptButton.addActionListener(e -> onAdoptClicked.run());

        add(infoPanel, BorderLayout.CENTER);
        add(adoptButton, BorderLayout.SOUTH);
    }

    public Pet getPet() {
        return pet;
    }
}