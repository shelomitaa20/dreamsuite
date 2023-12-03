package dreamsuiteapp;

import static dreamsuiteapp.koneksi.getKoneksi;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Laporan extends javax.swing.JFrame {

    public Laporan() {
        initComponents();
    }
    
    public void add(){
        String id = jTextFieldIDLaporan.getText();
        String totalpendapatan = jTextFieldTotalPendapatan.getText();
        String usernameadmin = jTextFieldUsernameAdmin.getText();
    
        try{
            Connection kon = getKoneksi();
            Statement ps = kon.createStatement();
            
            //memeriksa apakah username ada di database
            String queryAdmin = "SELECT * FROM admin WHERE username = ?";
            PreparedStatement psAdmin = kon.prepareStatement(queryAdmin);
            psAdmin.setString(1, usernameadmin);
            ResultSet rs = psAdmin.executeQuery();
            
             if (rs.next()) {
                String query = "INSERT INTO laporan VALUES('"+id+"','"+totalpendapatan+"','"+usernameadmin+"')";
                ps.executeUpdate(query);
                JOptionPane.showMessageDialog(rootPane,"Data berhasil ditambahkan");
                this.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(rootPane,"Username Salah!");
                this.setVisible(true);
            }

            rs.close();
            ps.close();
            kon.close();
            
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    
   public void refreshTableLaporan() {
        DefaultTableModel model = (DefaultTableModel) jTableLaporan.getModel();
        model.setRowCount(0); // Mengosongkan isi tabel

        try {
            Connection kon = getKoneksi();
            Statement ps = kon.createStatement();
            ResultSet rs = ps.executeQuery("SELECT l.id_laporan, COALESCE(SUM(t.total_transaksi), 0) AS total_pendapatan " +
                                           "FROM Laporan l " +
                                           "LEFT JOIN Transaksi t ON l.id_laporan = t.id_laporan " +
                                           "GROUP BY l.id_laporan");

            while (rs.next()) {
                String id = rs.getString("id_laporan");
                String totalPendapatan = rs.getString("total_pendapatan");

                // Menambahkan data ke dalam tabel
                model.addRow(new Object[]{id, totalPendapatan});

                // Perbarui nilai total_pendapatan di tabel Laporan
                String updateQuery = "UPDATE Laporan SET total_pendapatan = ? WHERE id_laporan = ?";
                PreparedStatement updateStatement = kon.prepareStatement(updateQuery);
                updateStatement.setString(1, totalPendapatan);
                updateStatement.setString(2, id);
                updateStatement.executeUpdate();
                updateStatement.close();
            }
            rs.close();
            ps.close();
            kon.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
   
    private void addTableListener() {
        jTableLaporan.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override //kenapa perlu ovveride?
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = jTableLaporan.getSelectedRow();
                    if (selectedRow != -1) { // Pastikan baris dipilih tidak kosong
                        // Ambil nilai dari baris yang dipilih
                        String id = jTableLaporan.getValueAt(selectedRow, 0).toString();
                        String total = jTableLaporan.getValueAt(selectedRow, 1).toString();

                        // Set nilai yang diambil ke dalam JTextField dan JComboBox
                        jTextFieldIDLaporan.setText(id);
                        jTextFieldTotalPendapatan.setText(total);
                    }
                }
            }
        });
    }
    
    public void edit() {
        String id = jTextFieldIDLaporan.getText();
        String usernameadmin = jTextFieldUsernameAdmin.getText();
        String totalpendapatan = jTextFieldTotalPendapatan.getText();

        try {
            Connection kon = getKoneksi();
            String queryAdmin = "SELECT * FROM admin WHERE username = ?";
            PreparedStatement psAdmin = kon.prepareStatement(queryAdmin);
            psAdmin.setString(1, usernameadmin);
            ResultSet rs = psAdmin.executeQuery();

            if (rs.next()) {
                String query = "UPDATE laporan SET id_laporan=? WHERE total_pendapatan=?";
                PreparedStatement ps = kon.prepareStatement(query);
                ps.setString(1, id);
                ps.setString(2, totalpendapatan);

                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(rootPane, "Data berhasil diupdate");
                    refreshTableLaporan();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Gagal mengupdate data");
                }

                ps.close();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Username Salah!");
            }

            rs.close();
            psAdmin.close();
            kon.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
      
    public void delete() {
        String id = jTextFieldIDLaporan.getText();
        String usernameadmin = jTextFieldUsernameAdmin.getText();

        try {
            Connection kon = getKoneksi();
            String queryAdmin = "SELECT * FROM admin WHERE username = ?";
            PreparedStatement psAdmin = kon.prepareStatement(queryAdmin);
            psAdmin.setString(1, usernameadmin);
            ResultSet rs = psAdmin.executeQuery();

            if (rs.next()) {
                String query = "DELETE FROM laporan WHERE id_laporan=?";
                PreparedStatement ps = kon.prepareStatement(query);
                ps.setString(1, id);

                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(rootPane, "Data berhasil dihapus");
                    refreshTableLaporan();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Gagal menghapus data");
                }

                ps.close();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Username Salah!");
            }

            rs.close();
            psAdmin.close();
            kon.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabelPegawai = new javax.swing.JLabel();
        jLabelLaporan2 = new javax.swing.JLabel();
        jLabelKamar = new javax.swing.JLabel();
        jLabelFasilitas = new javax.swing.JLabel();
        jLabelPemesanan = new javax.swing.JLabel();
        jLabelTransaksi = new javax.swing.JLabel();
        jButtonLogOut = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabelDreamSuite = new javax.swing.JLabel();
        jLabelLaporan = new javax.swing.JLabel();
        jLabelIDLaporan = new javax.swing.JLabel();
        jTextFieldIDLaporan = new javax.swing.JTextField();
        jButtonAddLaporan = new javax.swing.JButton();
        jButtonEditLaporan = new javax.swing.JButton();
        jButtonDeleteLaporan = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableLaporan = new javax.swing.JTable();
        jLabelUsernameAdmin = new javax.swing.JLabel();
        jTextFieldUsernameAdmin = new javax.swing.JTextField();
        jButtonRefreshLaporan = new javax.swing.JButton();
        jLabelIDLaporan1 = new javax.swing.JLabel();
        jTextFieldTotalPendapatan = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 0, 102));

        jLabelPegawai.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelPegawai.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPegawai.setText("Pegawai");
        jLabelPegawai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelPegawaiMouseClicked(evt);
            }
        });

        jLabelLaporan2.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelLaporan2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelLaporan2.setText("Laporan");

        jLabelKamar.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelKamar.setForeground(new java.awt.Color(255, 255, 255));
        jLabelKamar.setText("Kamar");
        jLabelKamar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelKamarMouseClicked(evt);
            }
        });

        jLabelFasilitas.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelFasilitas.setForeground(new java.awt.Color(255, 255, 255));
        jLabelFasilitas.setText("Fasilitas");

        jLabelPemesanan.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelPemesanan.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPemesanan.setText("Pemesanan");

        jLabelTransaksi.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTransaksi.setText("Transaksi");

        jButtonLogOut.setBackground(new java.awt.Color(255, 0, 51));
        jButtonLogOut.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonLogOut.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLogOut.setText("Log Out");
        jButtonLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPegawai)
                    .addComponent(jLabelLaporan2)
                    .addComponent(jLabelFasilitas)
                    .addComponent(jLabelKamar)
                    .addComponent(jLabelPemesanan)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButtonLogOut)
                        .addComponent(jLabelTransaksi)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(jLabelPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelLaporan2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelFasilitas, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelKamar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelPemesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(jButtonLogOut)
                .addGap(42, 42, 42))
        );

        jPanel3.setBackground(new java.awt.Color(226, 236, 255));

        jLabelDreamSuite.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        jLabelDreamSuite.setForeground(new java.awt.Color(0, 0, 102));
        jLabelDreamSuite.setText("DreamSuite");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(290, 290, 290)
                .addComponent(jLabelDreamSuite)
                .addContainerGap(304, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabelDreamSuite)
                .addContainerGap())
        );

        jLabelLaporan.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelLaporan.setForeground(new java.awt.Color(255, 153, 51));
        jLabelLaporan.setText("Laporan");

        jLabelIDLaporan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelIDLaporan.setText("ID Laporan");

        jButtonAddLaporan.setBackground(new java.awt.Color(255, 153, 51));
        jButtonAddLaporan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAddLaporan.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAddLaporan.setText("Add");
        jButtonAddLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddLaporanActionPerformed(evt);
            }
        });

        jButtonEditLaporan.setBackground(new java.awt.Color(255, 153, 51));
        jButtonEditLaporan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonEditLaporan.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEditLaporan.setText("Edit");
        jButtonEditLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditLaporanActionPerformed(evt);
            }
        });

        jButtonDeleteLaporan.setBackground(new java.awt.Color(255, 153, 51));
        jButtonDeleteLaporan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonDeleteLaporan.setForeground(new java.awt.Color(255, 255, 255));
        jButtonDeleteLaporan.setText("Delete");
        jButtonDeleteLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteLaporanActionPerformed(evt);
            }
        });

        jTableLaporan.setBackground(new java.awt.Color(0, 0, 102));
        jTableLaporan.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        jTableLaporan.setForeground(new java.awt.Color(255, 255, 255));
        jTableLaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Total Pendapatan"
            }
        ));
        jTableLaporan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTableLaporanFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTableLaporanFocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(jTableLaporan);

        jLabelUsernameAdmin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelUsernameAdmin.setText("Username Admin");

        jTextFieldUsernameAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldUsernameAdminActionPerformed(evt);
            }
        });

        jButtonRefreshLaporan.setBackground(new java.awt.Color(255, 153, 51));
        jButtonRefreshLaporan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonRefreshLaporan.setForeground(new java.awt.Color(255, 255, 255));
        jButtonRefreshLaporan.setText("Refresh");
        jButtonRefreshLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshLaporanActionPerformed(evt);
            }
        });

        jLabelIDLaporan1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelIDLaporan1.setText("Total Pendapatan");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabelLaporan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 554, Short.MAX_VALUE)
                                .addComponent(jButtonRefreshLaporan))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabelIDLaporan, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                                        .addGap(169, 169, 169))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextFieldIDLaporan)
                                            .addComponent(jLabelUsernameAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldUsernameAdmin)
                                            .addComponent(jLabelIDLaporan1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jButtonAddLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButtonEditLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButtonDeleteLaporan))
                                            .addComponent(jTextFieldTotalPendapatan))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(40, 40, 40))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonRefreshLaporan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabelIDLaporan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldIDLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jLabelIDLaporan1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTotalPendapatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(jLabelUsernameAdmin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldUsernameAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonAddLaporan)
                            .addComponent(jButtonEditLaporan)
                            .addComponent(jButtonDeleteLaporan)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddLaporanActionPerformed
        add();
    }//GEN-LAST:event_jButtonAddLaporanActionPerformed

    private void jTextFieldUsernameAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldUsernameAdminActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldUsernameAdminActionPerformed

    private void jButtonRefreshLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshLaporanActionPerformed
        refreshTableLaporan();
    }//GEN-LAST:event_jButtonRefreshLaporanActionPerformed

    private void jButtonDeleteLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteLaporanActionPerformed
        delete();
    }//GEN-LAST:event_jButtonDeleteLaporanActionPerformed

    private void jTableLaporanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTableLaporanFocusLost
        
    }//GEN-LAST:event_jTableLaporanFocusLost

    private void jButtonEditLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditLaporanActionPerformed
        edit();
    }//GEN-LAST:event_jButtonEditLaporanActionPerformed

    private void jTableLaporanFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTableLaporanFocusGained
        addTableListener();
    }//GEN-LAST:event_jTableLaporanFocusGained

    private void jButtonLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogOutActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButtonLogOutActionPerformed

    private void jLabelPegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPegawaiMouseClicked
       Pegawai pegawai = new Pegawai(); 
       pegawai.setVisible(true); 

       this.setVisible(false);
    }//GEN-LAST:event_jLabelPegawaiMouseClicked

    private void jLabelKamarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelKamarMouseClicked
       Kamar kamar = new Kamar(); 
       kamar.setVisible(true); 

       this.setVisible(false);
    }//GEN-LAST:event_jLabelKamarMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Laporan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddLaporan;
    private javax.swing.JButton jButtonDeleteLaporan;
    private javax.swing.JButton jButtonEditLaporan;
    private javax.swing.JButton jButtonLogOut;
    private javax.swing.JButton jButtonRefreshLaporan;
    private javax.swing.JLabel jLabelDreamSuite;
    private javax.swing.JLabel jLabelFasilitas;
    private javax.swing.JLabel jLabelIDLaporan;
    private javax.swing.JLabel jLabelIDLaporan1;
    private javax.swing.JLabel jLabelKamar;
    private javax.swing.JLabel jLabelLaporan;
    private javax.swing.JLabel jLabelLaporan2;
    private javax.swing.JLabel jLabelPegawai;
    private javax.swing.JLabel jLabelPemesanan;
    private javax.swing.JLabel jLabelTransaksi;
    private javax.swing.JLabel jLabelUsernameAdmin;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableLaporan;
    private javax.swing.JTextField jTextFieldIDLaporan;
    private javax.swing.JTextField jTextFieldTotalPendapatan;
    private javax.swing.JTextField jTextFieldUsernameAdmin;
    // End of variables declaration//GEN-END:variables
}
