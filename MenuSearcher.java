import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicReference;


public class MenuSearcher extends JFrame {
    private static final String filePath = "./menu.txt";
    private static final Icon icon = new ImageIcon("./gobbledy_geek_graphic_small.png");
    private static Menu menu;
    private static final String appName = "Eets 4 Gobbledy-Geeks";

    // UI Components
    private JComboBox<MenuType> typeComboBox;
    private JComboBox<String> bunComboBox;
    private JRadioButton tomatoYes, tomatoNo, tomatoDontMind;
    private JRadioButton picklesYes, picklesNo, picklesDontMind;
    private JCheckBox cheeseCheckBox;
    private JComboBox<Meat> meatComboBox;
    private JTextField minPriceField;
    private JTextField maxPriceField;
    private JButton searchButton;
    private JPanel dynamicContentPanel; // Panel for dynamic content switching

    private JLabel errorLabel;
    private JList sauceList;
    private JList<String> leafyGreensList;

    private JComboBox<Dressing> dressingComboBox;

    private JPanel cucumberPanel ;
    private JRadioButton cucumberYes ;
    private JRadioButton cucumberNo ;
    private JRadioButton cucumberDontKnow;
    private ButtonGroup cucumberGroup ;

    public MenuSearcher() {
        // Set up JFrame properties
        setTitle(appName);
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true); // Allow resizing of the window
        setMinimumSize(new Dimension(500, 400)); // Set minimum size

        // Load the menu data
        menu = loadMenu(filePath);

        // Set up the main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
// L     //leftImage Panel
        JLabel leftImageLabel = new JLabel(new ImageIcon(new ImageIcon("./gobbledy_geek_graphic.png").getImage().getScaledInstance(260, 356, Image.SCALE_SMOOTH))); // Increase size to cover the left area
        mainPanel.add(leftImageLabel, BorderLayout.WEST);

        // Center Panel for Search Controls
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7,7,7,7);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ComboBox for Select Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        typeComboBox = new JComboBox<>(MenuType.values());
        searchPanel.add(typeComboBox, gbc);

        // Dynamic Content Panel
        dynamicContentPanel = new JPanel(new CardLayout());
        setupInitialImagePanel();
        setupBurgerPanel();
        setupSaladPanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH; // Stretch both horizontally and vertically
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Allow the panel to stretch as the window resizes
        searchPanel.add(dynamicContentPanel, gbc);

        // Add other static components (Tomato, Pickles, Meat, etc.)
        addStaticComponents(gbc, searchPanel);

        mainPanel.add(searchPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Add Action Listener for Type ComboBox
        typeComboBox.addActionListener(e -> handleTypeSelectionChange());

        // Action Listener for search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DreamMenuItem dreamMenuItem = getFilters();
                if (dreamMenuItem != null) {
                    processSearchResults(dreamMenuItem);
                }
            }
        });
    }

    private void setupInitialImagePanel() {
        JPanel initialImagePanel = new JPanel(new GridLayout(1, 4, 5, 5));
        initialImagePanel.add(new JLabel(new ImageIcon(new ImageIcon("./images/10895.png").getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH))));
        initialImagePanel.add(new JLabel(new ImageIcon(new ImageIcon("./images/10922.png").getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH))));
        initialImagePanel.add(new JLabel(new ImageIcon(new ImageIcon("./images/90132.png").getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH))));
        initialImagePanel.add(new JLabel(new ImageIcon(new ImageIcon("./images/93444.png").getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH))));
        dynamicContentPanel.add(initialImagePanel, "SELECTTYPE");
    }

    private void setupBurgerPanel() {
        JPanel burgerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST; // Align components to the left

        // Load bun types from menu
        bunComboBox = new JComboBox<>(menu.getAllIngredientTypes(Filter.BUN).toArray(new String[0]));
        JLabel bunLabel = new JLabel("Preferred bun type?");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        burgerPanel.add(bunLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        burgerPanel.add(bunComboBox, gbc);

        // Load sauces from enum
        this.sauceList = new JList<>(Sauce.values());
        sauceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane sauceScrollPane = new JScrollPane(sauceList);
        JLabel sauceLabel = new JLabel("Please choose one or more sauces (To multi-select, hold Ctrl)");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Span two columns
        burgerPanel.add(sauceLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH; // Allow the list to stretch vertically
        burgerPanel.add(sauceScrollPane, gbc);

        dynamicContentPanel.add(burgerPanel, "BURGER");
    }

    private void setupSaladPanel() {
        JPanel saladPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST; // Align components to the left

        // Load dressing from enum
        dressingComboBox = new JComboBox<>(Dressing.values());
        JLabel dressingLabel = new JLabel("Preferred dressing?");
        gbc.gridx = 0;
        gbc.gridy = 0;
        saladPanel.add(dressingLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        saladPanel.add(dressingComboBox, gbc);

        // Radio buttons for cucumber
        JLabel cucumberLabel = new JLabel("Cucumber?");
        gbc.gridx = 0;
        gbc.gridy = 2;
        saladPanel.add(cucumberLabel, gbc);

        cucumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        cucumberYes = new JRadioButton("Yes");
        cucumberNo = new JRadioButton("No");
        cucumberDontKnow = new JRadioButton("I don't mind");
        cucumberGroup = new ButtonGroup();
        cucumberGroup.add(cucumberYes);
        cucumberGroup.add(cucumberNo);
        cucumberGroup.add(cucumberDontKnow);
        cucumberPanel.add(cucumberYes);
        cucumberPanel.add(cucumberNo);
        cucumberPanel.add(cucumberDontKnow);
        gbc.gridx = 0;
        gbc.gridy = 3;
        saladPanel.add(cucumberPanel, gbc);

        // Load leafy greens from menu - make the list taller and broader
        leafyGreensList = new JList<>(menu.getAllIngredientTypes(Filter.LEAFY_GREENS).toArray(new String[0]));
        leafyGreensList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane leafyGreensScrollPane = new JScrollPane(leafyGreensList);
        JLabel leafyGreensLabel = new JLabel("Please choose one or more leafy greens (To multi-select, hold Ctrl)");

        gbc.gridx = 1;
        gbc.gridy = 0;
        saladPanel.add(leafyGreensLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 3; // Span vertically for the list
        gbc.fill = GridBagConstraints.BOTH; // Stretch both horizontally and vertically
        saladPanel.add(leafyGreensScrollPane, gbc);

        // Reset grid constraints for next components
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        // Add salad panel to dynamic content
        dynamicContentPanel.add(saladPanel, "SALAD");
    }

    private void handleTypeSelectionChange() {
        MenuType selectedType = (MenuType) typeComboBox.getSelectedItem();
        CardLayout cl = (CardLayout) (dynamicContentPanel.getLayout());
        switch (selectedType) {
            case SELECTTYPE:
                cl.show(dynamicContentPanel, "SELECTTYPE");
                break;
            case BURGER:
                cl.show(dynamicContentPanel, "BURGER");
                break;
            case SALAD:
                cl.show(dynamicContentPanel, "SALAD");
                break;
        }
    }

    private void addStaticComponents(GridBagConstraints gbc, JPanel searchPanel) {
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;

        // Options for Tomatoes
        gbc.anchor = GridBagConstraints.CENTER; // Align labels to the left
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel lblTomato = new JLabel("Tomato?");
        searchPanel.add(lblTomato, gbc);

        JPanel tomatoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0)); // Small gap between radio buttons
        tomatoYes = new JRadioButton("Yes");
        tomatoNo = new JRadioButton("No");
        tomatoDontMind = new JRadioButton("I don't mind");
        ButtonGroup tomatoGroup = new ButtonGroup();
        tomatoGroup.add(tomatoYes);
        tomatoGroup.add(tomatoNo);
        tomatoGroup.add(tomatoDontMind);
        tomatoPanel.add(tomatoYes);
        tomatoPanel.add(tomatoNo);
        tomatoPanel.add(tomatoDontMind);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        searchPanel.add(tomatoPanel, gbc);

        // Options for Pickles
        gbc.gridx = 1;
        gbc.gridy = 3;
        JLabel lblPickles = new JLabel("Pickles?");
        searchPanel.add(lblPickles, gbc);

        JPanel picklesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0)); // Small gap between radio buttons
        picklesYes = new JRadioButton("Yes");
        picklesNo = new JRadioButton("No");
        picklesDontMind = new JRadioButton("I don't mind");
        ButtonGroup picklesGroup = new ButtonGroup();
        picklesGroup.add(picklesYes);
        picklesGroup.add(picklesNo);
        picklesGroup.add(picklesDontMind);
        picklesPanel.add(picklesYes);
        picklesPanel.add(picklesNo);
        picklesPanel.add(picklesDontMind);
        gbc.gridx = 1;
        gbc.gridy = 3;
        searchPanel.add(picklesPanel, gbc);

        // Cheese Option
        gbc.gridx = 1;
        gbc.gridy = 4;
        JLabel lblCheese = new JLabel("Cheese?");
        searchPanel.add(lblCheese, gbc);

        cheeseCheckBox = new JCheckBox();
        gbc.gridx = 2;
        gbc.gridy = 4;
        searchPanel.add(cheeseCheckBox, gbc);

        // Meat ComboBox
        gbc.gridx = 1;
        gbc.gridy = 5;
        JLabel lblMeat = new JLabel("Meat:");
        searchPanel.add(lblMeat, gbc);

        meatComboBox = new JComboBox<>(Meat.values());
        meatComboBox.setPreferredSize(new Dimension(100, 25)); // Adjust width to prevent overflow
        gbc.gridx = 2;
        gbc.gridy = 5;
        searchPanel.add(meatComboBox, gbc);

        // Center-aligned Price Range Fields
        gbc.anchor = GridBagConstraints.CENTER; // Center-align the price components

        // Min Price Label and Text Field
        gbc.gridx = 1;
        gbc.gridy = 6;
        JLabel lblMinPrice = new JLabel("Min. price");
        searchPanel.add(lblMinPrice, gbc);

        minPriceField = new JTextField(3); // Small text field
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE; // Keep size fixed
        searchPanel.add(minPriceField, gbc);

        // Max Price Label and Text Field
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        JLabel lblMaxPrice = new JLabel("Max. price");
        searchPanel.add(lblMaxPrice, gbc);

        maxPriceField = new JTextField(3); // Small text field
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE; // Keep size fixed
        searchPanel.add(maxPriceField, gbc);

        // Search Button
        searchButton = new JButton("Search");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 4; // Span the button across four columns
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch button horizontally
        searchPanel.add(searchButton, gbc);

        // Error Label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED); // Set text color to red for errors
        gbc.gridx = 0;
        gbc.gridy = 9; // Position below the search button
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER; // Center the error message
        searchPanel.add(errorLabel, gbc);
    }





    // Corrected getFilters() method (remove static keyword)
    private DreamMenuItem getFilters() {
        errorLabel.setText(""); // Clear any previous error message
        Map<Filter, Object> filterMap = new LinkedHashMap<>();

        // Get the selected type (Burger, Salad, etc.)
        MenuType type = (MenuType) typeComboBox.getSelectedItem();
        if (type == MenuType.SELECTTYPE) {
            errorLabel.setText("Please select a menu type.");
            return null;
        }
        filterMap.put(Filter.TYPE, type);
        System.out.println("Selected Menu Type: " + type);

        // Handle specific options for BURGER type
        if (type == MenuType.BURGER) {
            // Get the bun type
            String bunType = (String) bunComboBox.getSelectedItem();
            if ("I don't mind".equals(bunType)) {
                // Add all bun types except "I don't mind"
                Object[] allBuns = menu.getAllIngredientTypes(Filter.BUN).toArray();
                Set<String> buns = new HashSet<>();
                for (Object bun : allBuns) {
                    if (!bun.equals("I don't mind")) {
                        buns.add((String) bun);
                    }
                }
                filterMap.put(Filter.BUN, buns);
            } else if (bunType != null && !bunType.isEmpty()) {
                filterMap.put(Filter.BUN, bunType);
            }

            // Get selected sauces
            List<Sauce> selectedSauces = sauceList.getSelectedValuesList();
            if (!selectedSauces.isEmpty() && !selectedSauces.contains(Sauce.NA)) {
                filterMap.put(Filter.SAUCE_S, new HashSet<>(selectedSauces));
            } else if (selectedSauces.contains(Sauce.NA)) {
                // Add all sauces except "Any sauce will do..." (NA)
                Set<Sauce> allSauces = new HashSet<>(Arrays.asList(Sauce.values()));
                allSauces.remove(Sauce.NA);
                filterMap.put(Filter.SAUCE_S, allSauces);
            }
        } else if (type == MenuType.SALAD) {
            // Handle specific options for SALAD type

            // Get selected leafy greens
            List<String> selectedLeafyGreens = leafyGreensList.getSelectedValuesList();
            if (!selectedLeafyGreens.isEmpty()) {
                filterMap.put(Filter.LEAFY_GREENS, new HashSet<>(selectedLeafyGreens));
            }

            // Get cucumber preference
            if (cucumberYes.isSelected()) {
                filterMap.put(Filter.CUCUMBER, true); // Yes
            } else if (cucumberNo.isSelected()) {
                filterMap.put(Filter.CUCUMBER, false); // No
            } else if (cucumberDontKnow.isSelected()) {
                filterMap.put(Filter.CUCUMBER, Arrays.asList(true, false)); // Both Yes and No are acceptable
            } else {
                errorLabel.setText("Please select an option for Cucumber.");
                return null;
            }

            // Get dressing preference
            Dressing dressing = (Dressing) dressingComboBox.getSelectedItem();
            if (dressing != null && dressing != Dressing.NA) {
                filterMap.put(Filter.DRESSING, dressing);
            } else if (dressing == Dressing.NA) {
                // Add all dressings except "Any dressing will do..." (NA)
                Set<Dressing> allDressings = new HashSet<>(Arrays.asList(Dressing.values()));
                allDressings.remove(Dressing.NA);
                filterMap.put(Filter.DRESSING, allDressings);
            }
        }

        // Rest of the filters (Tomato, Pickles, Cheese, Meat) follow similar logic
        // Collect options for tomato preference
        if (tomatoYes.isSelected()) {
            filterMap.put(Filter.TOMATO, true); // Yes
            System.out.println("Selected Tomato: Yes");
        } else if (tomatoNo.isSelected()) {
            filterMap.put(Filter.TOMATO, false); // No
            System.out.println("Selected Tomato: No");
        } else if (tomatoDontMind.isSelected()) {
            filterMap.put(Filter.TOMATO, Arrays.asList(true, false)); // Both Yes and No are acceptable
            System.out.println("Selected Tomato: I don't mind");
        } else {
            errorLabel.setText("Please select an option for Tomato.");
            return null;
        }

        // Collect options for pickles preference
        if (picklesYes.isSelected()) {
            filterMap.put(Filter.PICKLES, true); // Yes
            System.out.println("Selected Pickles: Yes");
        } else if (picklesNo.isSelected()) {
            filterMap.put(Filter.PICKLES, false); // No
            System.out.println("Selected Pickles: No");
        } else if (picklesDontMind.isSelected()) {
            filterMap.put(Filter.PICKLES, Arrays.asList(true, false)); // Both Yes and No are acceptable
            System.out.println("Selected Pickles: I don't mind");
        } else {
            errorLabel.setText("Please select an option for Pickles.");
            return null;
        }

        // Collect cheese preference
        boolean cheeseSelected = cheeseCheckBox.isSelected();
        filterMap.put(Filter.CHEESE, cheeseSelected);
        System.out.println("Selected Cheese: " + (cheeseSelected ? "Yes" : "No"));

        // Get the selected meat type
        Meat meat = (Meat) meatComboBox.getSelectedItem();
        if (meat != null && meat != Meat.NA) {
            filterMap.put(Filter.MEAT, meat);
        } else if (meat == Meat.NA) {
            // Add all meats except "Any meat will do..." (NA)
            Set<Meat> allMeats = new HashSet<>(Arrays.asList(Meat.values()));
            allMeats.remove(Meat.NA);
            filterMap.put(Filter.MEAT, allMeats);
        }

        // Parse and validate minimum and maximum prices
        double minPrice = 0.0;
        double maxPrice = 0.0;
        try {
            minPrice = Double.parseDouble(minPriceField.getText().trim());
            maxPrice = Double.parseDouble(maxPriceField.getText().trim());
            if (minPrice < 0 || maxPrice < minPrice) {
                errorLabel.setText("Invalid entry: Min price must be less than Max price.");
                return null;
            }
            System.out.println("Selected Price Range: Min = " + minPrice + ", Max = " + maxPrice);
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid entry: Only numbers are allowed for price.");
            return null;
        }

        // Create the DreamMenuItem with the collected filters and price range
        System.out.println("Filters Applied: " + filterMap);
        return new DreamMenuItem(filterMap, minPrice, maxPrice);
    }

    // Ensure main method is launching the application properly
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuSearcher app = new MenuSearcher();
            app.setVisible(true);
        });
    }
    public static void processSearchResults(DreamMenuItem dreamMenuItem) {
        List<MenuItem> matching = menu.findMatch(dreamMenuItem);
        AtomicReference<MenuItem> chosenItem = new AtomicReference<>(null);

        JFrame resultFrame = new JFrame("Search Results - " + appName);
        resultFrame.setSize(800, 600);
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultFrame.setLayout(new BorderLayout());

        if (matching.size() > 0) {
            Map<String, MenuItem> options = new HashMap<>();
            for (MenuItem match : matching) {
                options.put(match.getMenuItemName(), match);
            }
            // Create the main panel to hold the results
            JPanel resultPanel = new JPanel(new BorderLayout());

            // Label above the list view
            JLabel listLabel = new JLabel("Following are the results matching from the selected option:");
            resultPanel.add(listLabel, BorderLayout.NORTH);

            // Create a list model to store matching items
            DefaultListModel<MenuItem> listModel = new DefaultListModel<>();
            for (MenuItem match : matching) {
                listModel.addElement(match);
            }

            // Create the JList to display items
            JList<MenuItem> resultList = new JList<>(listModel);
            resultList.setCellRenderer(new MenuItemRenderer());
            JScrollPane scrollPane = new JScrollPane(resultList);
            resultPanel.add(scrollPane, BorderLayout.CENTER);

            // Create a panel for buttons and dropdown
            JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));

            // Label above the dropdown and search again button
            JLabel comboBoxLabel = new JLabel("Please select an option from dropdown to order:");
            buttonPanel.add(comboBoxLabel);

            // Create a JComboBox with MenuItem objects
            JComboBox<MenuItem> itemComboBox = new JComboBox<>(matching.toArray(new MenuItem[0]));

// Custom renderer to display the item name and identifier
            itemComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
                JLabel label = new JLabel();
                label.setOpaque(true);

                // Format the display as "Name (Identifier)"
                String displayText = value.getMenuItemName() + " (" + value.getMenuItemIdentifier() + ")";
                label.setText(displayText);

                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                } else {
                    label.setBackground(list.getBackground());
                    label.setForeground(list.getForeground());
                }

                return label;
            });

// Add the JComboBox to the panel
            buttonPanel.add(itemComboBox);


            // "Search Again" button to go back to the search screen
            JButton searchAgainButton = new JButton("Search Again");
            searchAgainButton.addActionListener(e -> {
                resultFrame.dispose(); // Close the current results window
                new MenuSearcher(); // Replace with your method to re-show the search screen
            });
            buttonPanel.add(searchAgainButton);

            // Add button panel to the bottom of the frame
            resultPanel.add(buttonPanel, BorderLayout.SOUTH);

            // Add the result panel to the frame
            resultFrame.add(resultPanel, BorderLayout.CENTER);

            resultFrame.setVisible(true);

            // Event listener to handle the selection of an item
            itemComboBox.addActionListener(e -> {
                chosenItem.set((MenuItem) itemComboBox.getSelectedItem());
                submitOrder(getUserContactInfo(chosenItem.get()), chosenItem.get());
                JOptionPane.showMessageDialog(resultFrame, "Thank you! Your order has been submitted. Please wait for your name to be called out...", appName, JOptionPane.INFORMATION_MESSAGE);
                resultFrame.dispose(); // Close after order submission
            });

        } else {
            // Handle case when no matches found
            int custom = JOptionPane.showConfirmDialog(null, """
                Unfortunately, none of our items meet your criteria :(
                Would you like to place a custom order?
                **Price to be calculated at checkout and may exceed your chosen range**.""", appName, JOptionPane.YES_NO_OPTION);
//            if (custom == 0) {
//                chosenItem.set(new MenuItem(dreamMenuItem));
//                submitOrder(Objects.requireNonNull(getUserContactInfo(chosenItem.get())), chosenItem.get());
//                JOptionPane.showMessageDialog(resultFrame, "Thank you! Your order has been submitted. Please wait for your name to be called out...", appName, JOptionPane.INFORMATION_MESSAGE);
//            } else {
//                System.exit(0);
//            }
        }
    }

    public static Geek getUserContactInfo(MenuItem selectedItem) {
        // Create the frame for the form
        JFrame frame = new JFrame("Order Information - " + appName);
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Main panel to hold all components
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // Left panel for the form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Set margins around components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // Top label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        JLabel topLabel = new JLabel("To place an order for " + selectedItem.getMenuItemName() + ", fill in the form below:");
        formPanel.add(topLabel, gbc);

        // Name field
        gbc.gridy++;
        gbc.gridwidth = 1; // Reset to one column
        JLabel nameLabel = new JLabel("Name:");
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2; // Span across two columns for error message
        JLabel nameError = new JLabel();
        nameError.setForeground(Color.RED);
        formPanel.add(nameError, gbc);

        // Phone number field
        gbc.gridy++;
        gbc.gridwidth = 1; // Reset to one column
        gbc.gridx = 0;
        JLabel phoneLabel = new JLabel("Phone Number:");
        formPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(15);
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2; // Span across two columns for error message
        JLabel phoneError = new JLabel();
        phoneError.setForeground(Color.RED);
        formPanel.add(phoneError, gbc);

        // Customisation text area
        gbc.gridy++;
        gbc.gridwidth = 2; // Span across two columns
        JLabel customisationLabel = new JLabel("Specify any customisation below:");
        formPanel.add(customisationLabel, gbc);

        gbc.gridy++;
        JTextArea customisationArea = new JTextArea(5, 15);
        customisationArea.setLineWrap(true);
        customisationArea.setWrapStyleWord(true);
        JScrollPane customisationScrollPane = new JScrollPane(customisationArea);
        formPanel.add(customisationScrollPane, gbc);

        // Add form panel to main panel
        mainPanel.add(formPanel);

        // Right panel for item information using MenuItemRenderer
        // Right panel for item information using MenuItemRenderer
        JPanel itemInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.insets = new Insets(5, 5, 5, 5); // Set margins around components
        gbcRight.fill = GridBagConstraints.BOTH;
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.gridwidth = 2;
        gbcRight.anchor = GridBagConstraints.NORTHWEST;

// Item Information Label at the top
        JLabel itemInfoLabel = new JLabel("Item Information:");
        itemInfoPanel.add(itemInfoLabel, gbcRight);

// Display the image in a 200x200 area
        MenuItemRenderer renderer = new MenuItemRenderer();
        gbcRight.gridy = 1;
        gbcRight.gridwidth = 1; // Reset to one column
        gbcRight.weightx = 0.5;
        gbcRight.weighty = 0.5;
        JLabel itemImageLabel = renderer.getImageLabel(selectedItem); // Use MenuItemRenderer's method to get the image label
        itemImageLabel.setPreferredSize(new Dimension(200, 200)); // Ensure image size
        itemInfoPanel.add(itemImageLabel, gbcRight);

// Display the text area in the next 200x200 area
        gbcRight.gridx = 1;
        JTextArea itemInfoArea = renderer.getTextArea(selectedItem); // Use MenuItemRenderer's method to get the text area
        itemInfoArea.setPreferredSize(new Dimension(200, 200)); // Ensure text area size
        itemInfoPanel.add(itemInfoArea, gbcRight);


        // Add item info panel to main panel
        mainPanel.add(itemInfoPanel);

        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // Submit button
        JButton submitButton = new JButton("Submit Order");
        submitButton.addActionListener(e -> {
            boolean valid = true;

            // Validate name
            String name = nameField.getText().trim();
            if (!name.matches("^[A-Za-z]+\\s+[A-Za-z]+(\\s*[A-Za-z]*)*$")) {
                nameError.setText("Invalid name. Please enter a first and last name (alphabets only).");
                valid = false;
            } else {
                nameError.setText("");
            }

            // Validate phone number
            String phone = phoneField.getText().trim();
            if (!phone.matches("^04\\d{8}$")) {
                phoneError.setText("Invalid phone number. Must be 10 digits and start with 04.");
                valid = false;
            } else {
                phoneError.setText("");
            }

            if (valid) {
                selectedItem.setCustomisationMessage(customisationArea.getText());
                // Gather information and create Geek object
                Geek geek = new Geek(name, Long.parseLong(phone));
                frame.dispose(); // Close the form
                JOptionPane.showMessageDialog(null, "Order Submitted!", appName, JOptionPane.INFORMATION_MESSAGE);
                submitOrder(geek, selectedItem ); // Submit order with selected item
            }
        });

        frame.add(submitButton, BorderLayout.SOUTH);

        // Show the frame
        frame.setVisible(true);
        return null; // Adjust return type as needed
    }

    public static void submitOrder(Geek geek, MenuItem menuItem) {
        // Define the file path for saving the order details
        String filePath = geek.getName().replace(" ", "_") + "_" + menuItem.getMenuItemIdentifier() + ".txt";
        Path path = Path.of(filePath);

        // Prepare the order details in the desired format
        StringBuilder lineToWrite = new StringBuilder();
        lineToWrite.append("Order details:\n")
                .append("    Name: ").append(geek.getName())
                .append(" (0").append(geek.getOrderNumber()).append(")\n")
                .append("    Item: ").append(menuItem.getMenuItemName())
                .append(" (").append(menuItem.getMenuItemIdentifier()).append(")\n")
                .append("\nCustomisation:\n")
                .append(menuItem.getCustomisationMessage());

        // Write the order details to the specified file
        try {
            Files.writeString(path, lineToWrite.toString());
        } catch (IOException io) {
            System.out.println("Order could not be placed. \nError message: " + io.getMessage());
            System.exit(0);
        }
    }

    public static Menu loadMenu(String filePath) {
        Menu menu = new Menu();
        Path path = Path.of(filePath);
        List<String> fileContents = null;
        try {
            fileContents = Files.readAllLines(path);
        }catch (IOException io){
            System.out.println("File could not be found");
            System.exit(0);
        }

        for(int i=1;i<fileContents.size();i++){

            String[] info = fileContents.get(i).split("\\[");
            String[] singularInfo = info[0].split(",");

            String leafyGreensRaw = info[1].replace("]","");
            String saucesRaw = info[2].replace("]","");
            String description = info[3].replace("]","");

            long menuItemIdentifier = 0;
            try{
                menuItemIdentifier = Long.parseLong(singularInfo[0]);
            }catch (NumberFormatException n) {
                System.out.println("Error in file. Menu item identifier could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
                System.exit(0);
            }

            MenuType type = null;
            try{
                type = MenuType.valueOf(singularInfo[1].toUpperCase().strip());
            }catch (IllegalArgumentException e){
                System.out.println("Error in file. Type data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                System.exit(0);
            }

            String menuItemName = singularInfo[2];

            double price = 0;
            try{
                price = Double.parseDouble(singularInfo[3]);
            }catch (NumberFormatException n){
                System.out.println("Error in file. Price could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
                System.exit(0);
            }

            String bun = singularInfo[4].toLowerCase().strip();

            Meat meat = null;
            try {
                meat = Meat.valueOf(singularInfo[5].toUpperCase());
            }catch (IllegalArgumentException e){
                System.out.println("Error in file. Meat data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                System.exit(0);
            }

            boolean cheese = false;
            String cheeseRaw = singularInfo[6].strip().toUpperCase();
            if(cheeseRaw.equals("YES")) cheese = true;

            boolean pickles = false;
            String pickleRaw = singularInfo[7].strip().toUpperCase();
            if(pickleRaw.equals("YES")) pickles = true;

            boolean cucumber = false;
            String cucumberRaw = singularInfo[8].strip().toUpperCase();
            if(cucumberRaw.equals("YES")) cucumber = true;

            boolean tomato = false;
            String tomatoRaw = singularInfo[9].strip().toUpperCase();
            if(tomatoRaw.equals("YES")) tomato = true;

            Dressing dressing = null;
            try {
                dressing = Dressing.valueOf(singularInfo[10].toUpperCase().replace(" ","_"));
            }catch (IllegalArgumentException e){
                System.out.println("Error in file. Dressing data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                System.exit(0);
            }

            Set<String> leafyGreens = new HashSet<>();
            for(String l: leafyGreensRaw.split(",")){
                leafyGreens.add(l.toLowerCase().strip());
            }

            Set<Sauce> sauces = new HashSet<>();
            for(String s: saucesRaw.split(",")){
                Sauce sauce = null;
                try {
                    sauce = Sauce.valueOf(s.toUpperCase().strip());
                }catch (IllegalArgumentException e){
                    System.out.println("Error in file. Sauce/s data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                    System.exit(0);
                }
                sauces.add(sauce);
            }

            Map<Filter,Object> filterMap = new LinkedHashMap<>();
            filterMap.put(Filter.TYPE,type);
            if(type.equals(MenuType.BURGER)){
                filterMap.put(Filter.BUN, bun);
                if(sauces.size()>0) filterMap.put(Filter.SAUCE_S,sauces);
            }
            if(!meat.equals(Meat.NA)) filterMap.put(Filter.MEAT,meat);
            filterMap.put(Filter.PICKLES, pickles);
            filterMap.put(Filter.CHEESE, cheese);
            filterMap.put(Filter.TOMATO, tomato);
            if(type.equals(MenuType.SALAD)){
                filterMap.put(Filter.DRESSING,dressing);
                filterMap.put(Filter.LEAFY_GREENS,leafyGreens);
                filterMap.put(Filter.CUCUMBER, cucumber);
            }

            DreamMenuItem dreamMenuItem = new DreamMenuItem(filterMap);
            MenuItem menuItem = new MenuItem(menuItemIdentifier, menuItemName,price,description, dreamMenuItem);
            menu.addItem(menuItem);
        }
        return menu;
    }

}
































