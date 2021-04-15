package com.shinkson47.opex.backend.io.cjar;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import java.util.List;

class CJARFilePrompt {
    private JPanel panel1;
    private JTextArea textArea1;
    private JComboBox<String> comboBox1;
    private JButton thatSAllTheButton;
    private DefaultListModel<String> model = new DefaultListModel<>();
    private JList list1;
    private JLabel lblTotal;
    private JButton btnRemove;
    private JTextField textField1;

    protected CJARFilePrompt(File parent, CJARMeta meta){
        list1.setModel(model);
        updateRegistered(meta);
        for (String PKG : ContentJavaArchive.ALL_CJAR_PACKAGES)
            comboBox1.addItem(PKG);

        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRegistered(meta);
            }
        });

        textArea1.setDropTarget(
                new DropTarget() {
                    public synchronized void drop(DropTargetDropEvent evt) {
                        try {
                            evt.acceptDrop(DnDConstants.ACTION_COPY);
                            List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                            for (File file : droppedFiles) {
                                ContentJavaArchive.addFilePreExport(file,parent, comboBox1.getSelectedItem().toString(),meta);
                                updateRegistered(meta);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
        );

        JDialog dialog = new JDialog(new JFrame(), true);
        dialog.setContentPane(panel1);
        dialog.setSize(600,1000);
        dialog.setLocationRelativeTo(null);

        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContentJavaArchive.removePreExport(parent, list1.getSelectedValue().toString(), meta);
                updateRegistered(meta);
            }
        });

        thatSAllTheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        dialog.setVisible(true);


    }

    private void updateRegistered(CJARMeta meta) {
        model.removeAllElements();
        for (String file : meta.paths)
            if(file.contains(textField1.getText()))
                model.addElement(file);
        lblTotal.setText(model.getSize() + " / " + meta.paths.size() + " shown.");
    }
}
