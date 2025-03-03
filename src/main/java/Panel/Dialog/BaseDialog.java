
package Panel.Dialog;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseDialog extends JDialog {
    protected JPanel contentPanel;
    protected JPanel inputPanel;
    protected Map<String, JTextField> fields;
    protected JButton okButton;
    protected JButton cancelButton;

    public BaseDialog(Frame parent, String title) {
        super(parent, title, true);
        fields = new LinkedHashMap<>();
        initBaseComponents();
    }

    private void initBaseComponents() {
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPanel);

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        contentPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> dispose());
        okButton.addActionListener(e -> onOkButtonClick());

        setPreferredSize(new Dimension(350, 250));
    }

    protected void addField(String label, String fieldName) {
        JLabel jLabel = new JLabel(label);
        JTextField textField = new JTextField(20);
        fields.put(fieldName, textField);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = fields.size() - 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        inputPanel.add(jLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        inputPanel.add(textField, gbc);
    }

    protected String getFieldValue(String fieldName) {
        JTextField field = fields.get(fieldName);
        return field != null ? field.getText() : "";
    }

    protected void setFieldValue(String fieldName, String value) {
        JTextField field = fields.get(fieldName);
        if (field != null) {
            field.setText(value);
        }
    }

    protected void finalizeDialog() {
        pack();
        setLocationRelativeTo(getOwner());
    }

    // Abstract method untuk event handling, akan diimplementasikan di subclass
    protected abstract void onOkButtonClick();
}