import javax.swing.*;
import java.awt.*;

class MenuItemRenderer extends JPanel implements ListCellRenderer<MenuItem> {
    private JLabel imageLabel;
    private JTextArea textArea;

    public MenuItemRenderer() {
        setLayout(new BorderLayout(5, 5));
        imageLabel = new JLabel();
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        add(imageLabel, BorderLayout.WEST);
        add(textArea, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends MenuItem> list, MenuItem value, int index, boolean isSelected, boolean cellHasFocus) {
        // Determine the type from the DreamMenuItem's filters
        MenuType type = (MenuType) value.getDreamMenuItem().getFilter(Filter.TYPE);
        String imagePath;

        // Arrays of images for burgers and salads
        String[] burgers = {
                "10895.png", "10922.png", "11706.png", "11786.png", "12005.png",
                "12675.png", "15564.png", "20309.png", "20339.png", "23563.png",
                "23564.png", "24453.png", "26632.png", "32231.png"
        };

        String[] salads = {
                "80443.png", "80943.png", "90132.png", "92332.png", "93444.png",
                "98232.png", "98233.png"
        };

        // Check if the item type is Salad or Burger, and load a random image
        if (type == MenuType.SALAD) {
            imagePath = "./images/" + salads[index % salads.length];  // Load random salad image
        } else {
            imagePath = "./images/" + burgers[index % burgers.length]; // Load random burger image
        }

        imageLabel.setIcon(new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(105, 210, Image.SCALE_SMOOTH)));

        // Set the text details
        textArea.setText(value.getMenuItemInformation());

        // Highlight selection
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }

    // Method to get the image label for use in other components
    public JLabel getImageLabel(MenuItem menuItem) {
        String imagePath;
        // Determine the type from the DreamMenuItem's filters
        MenuType type = (MenuType) menuItem.getDreamMenuItem().getFilter(Filter.TYPE);

        // Check if the item type is Salad or Burger
        if (type == MenuType.SALAD) {
            imagePath = "./images/s" + (menuItem.getMenuItemIdentifier() % 7 + 1) + ".png";  // Salad images s1.png to s7.png
        } else {
            imagePath = "./images/b" + (menuItem.getMenuItemIdentifier() % 10 + 1) + ".png"; // Burger images b1.png to b14.png
        }

        imageLabel.setIcon(new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(105, 210, Image.SCALE_SMOOTH)));
        return imageLabel;
    }

    // Method to get the text area with item details for use in other components
    public JTextArea getTextArea(MenuItem menuItem) {
        textArea.setText(menuItem.getMenuItemInformation());
        return textArea;
    }
}
