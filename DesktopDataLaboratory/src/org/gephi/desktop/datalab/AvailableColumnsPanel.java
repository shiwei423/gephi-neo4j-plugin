/*
Copyright 2008-2010 Gephi
Authors : Eduardo Ramos <eduramiba@gmail.com>
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gephi.desktop.datalab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import net.miginfocom.swing.MigLayout;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeTable;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Severity;
import org.netbeans.validation.api.ui.ValidationGroup;
import org.netbeans.validation.api.ui.ValidationListener;
import org.netbeans.validation.api.ui.ValidationPanel;
import org.openide.util.NbBundle;

/**
 * UI for selecting available columns of a table in Data laboratory
 * @see AvailableColumnsModel
 * @author Eduardo
 */
public class AvailableColumnsPanel extends javax.swing.JPanel {

    private final AttributeTable table;
    private final AvailableColumnsModel availableColumnsModel;
    private AttributeColumn[] columns;
    private JCheckBox[] columnsCheckBoxes;
    private AvailableColumnsValidator validator;

    /** Creates new form AvailableColumnsPanel */
    public AvailableColumnsPanel(AttributeTable table, AvailableColumnsModel availableColumnsModel) {
        initComponents();
        this.table = table;
        this.availableColumnsModel = availableColumnsModel;
        columns = table.getColumns();
        refreshColumns();
        refreshAvailableColumnsControls();
    }

    public ValidationPanel getValidationPanel() {
        ValidationPanel validationPanel = new ValidationPanel();
        validationPanel.setInnerComponent(this);

        ValidationGroup group = validationPanel.getValidationGroup();
        group.add(validator = new AvailableColumnsValidator());
        refreshAvailableColumnsControls();
        return validationPanel;
    }

    private void refreshColumns() {
        columnsCheckBoxes = new JCheckBox[columns.length];
        contentPanel.removeAll();
        contentPanel.setLayout(new MigLayout("", "[pref!]"));
        for (int i = 0; i < columns.length; i++) {
            columnsCheckBoxes[i] = new JCheckBox(columns[i].getTitle(), availableColumnsModel.isColumnAvailable(columns[i]));
            columnsCheckBoxes[i].addActionListener(new ColumnCheckBoxListener(i));
            contentPanel.add(columnsCheckBoxes[i], "wrap");
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void refreshAvailableColumnsControls() {
        boolean enabled = availableColumnsModel.canAddAvailableColumn();
        for (JCheckBox cb : columnsCheckBoxes) {
            if (!cb.isSelected()) {
                cb.setEnabled(enabled);
            }
        }
        if (validator != null) {
            validator.event();
        }
    }

    class ColumnCheckBoxListener implements ActionListener {

        private int index;

        public ColumnCheckBoxListener(int index) {
            this.index = index;
        }

        public void actionPerformed(ActionEvent e) {
            if (columnsCheckBoxes[index].isSelected()) {
                availableColumnsModel.addAvailableColumn(columns[index]);
            } else {
                availableColumnsModel.removeAvailableColumn(columns[index]);
            }
            refreshAvailableColumnsControls();
        }
    }

    class AvailableColumnsValidator extends ValidationListener {

        @Override
        protected boolean validate(Problems prblms) {
            if (!availableColumnsModel.canAddAvailableColumn()) {
                prblms.add(NbBundle.getMessage(AvailableColumnsPanel.class, "AvailableColumnsPanel.maximum-available-columns.info"), Severity.INFO);
                return false;
            }
            return true;
        }

        public void event() {
            validate();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scroll = new javax.swing.JScrollPane();
        contentPanel = new javax.swing.JPanel();
        descriptionLabel = new javax.swing.JLabel();

        contentPanel.setLayout(new java.awt.GridLayout());
        scroll.setViewportView(contentPanel);

        descriptionLabel.setText(org.openide.util.NbBundle.getMessage(AvailableColumnsPanel.class, "AvailableColumnsPanel.descriptionLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .addComponent(scroll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(descriptionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JScrollPane scroll;
    // End of variables declaration//GEN-END:variables
}
