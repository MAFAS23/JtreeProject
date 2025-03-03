/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

/**
 *
 * @author athif
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class AutoCompleteTextField extends JTextField {
    private final JPopupMenu popupMenu;
    private final List<String> vendorNames;
    private final DefaultListModel<String> listModel;
    private final JList<String> suggestionList;

    public AutoCompleteTextField(List<String> vendorNames) {
        this.vendorNames = vendorNames;
        this.popupMenu = new JPopupMenu();
        this.listModel = new DefaultListModel<>();
        this.suggestionList = new JList<>(listModel);
        initAutoComplete();
    }

    private void initAutoComplete() {
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestionList.setFocusable(false);

        JScrollPane scrollPane = new JScrollPane(suggestionList);
        popupMenu.setLayout(new BorderLayout());
        popupMenu.add(scrollPane, BorderLayout.CENTER);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = getText();
                if (input.isEmpty()) {
                    popupMenu.setVisible(false);
                } else {
                    updateSuggestions(input);
                    if (listModel.getSize() > 0) {
                        popupMenu.show(AutoCompleteTextField.this, 0, getHeight());
                        popupMenu.setVisible(true);
                    } else {
                        popupMenu.setVisible(false);
                    }
                }
            }
        });

        suggestionList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    selectSuggestion();
                }
            }
        });

        this.addActionListener(e -> selectSuggestion());
    }

    private void updateSuggestions(String input) {
        listModel.clear();
        for (String vendor : vendorNames) {
            if (vendor.toLowerCase().contains(input.toLowerCase())) {
                listModel.addElement(vendor);
            }
        }
    }

    private void selectSuggestion() {
        if (!suggestionList.isSelectionEmpty()) {
            setText(suggestionList.getSelectedValue());
            popupMenu.setVisible(false);
        }
    }
}
