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

public class Pegawai extends javax.swing.JFrame {

    /**
     * Creates new form Pegawai
     */
    public Pegawai() {
        initComponents();
    }
    
    public void add(){
        String id = jTextFieldIDPegawai.getText();
        String nama = jTextFieldNamaPegawai.getText();
        String telp = jTextFieldTelpPegawai.getText();
        String alamat = jTextFieldAlamatPegawai.getText();
        String jabatan = jComboBoxJabatanPegawai.getSelectedItem().toString();
        String status = jComboBoxStatusPegawai.getSelectedItem().toString();
        String usernameadmin = jTextFieldIDAdmin.getText();
        try{
            Connection kon = getKoneksi();
            Statement ps = kon.createStatement();
            
            //memeriksa apakah username ada di database
            String queryAdmin = "SELECT * FROM admin WHERE username = ?";
            PreparedStatement psAdmin = kon.prepareStatement(queryAdmin);
            psAdmin.setString(1, usernameadmin);
            ResultSet rs = psAdmin.executeQuery();
            
             if (rs.next()) {
                String query = "INSERT INTO pegawai VALUES('"+id+"', '"+nama+"', '"+telp+"', '"+alamat+"', '"+jabatan+"', '"+status+"','"+usernameadmin+"')";
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
    
    public void refreshTablePegawai() {
        DefaultTableModel model = (DefaultTableModel) jTablePegawai.getModel();
        model.setRowCount(0); // Mengosongkan isi tabel

        try {
            Connection kon = getKoneksi();
            Statement ps = kon.createStatement();
            ResultSet rs = ps.executeQuery("SELECT * FROM pegawai");

            while (rs.next()) {
                String id = rs.getString("id_pegawai");
                String nama = rs.getString("nama_pegawai");
                String telp = rs.getString("telp_pegawai");
                String alamat = rs.getString("alamat_pegawai");
                String jabatan = rs.getString("jabatan_pegawai");
                String status = rs.getString("status_pegawai");

                // Menambahkan data ke dalam tabel
                model.addRow(new Object[]{id, nama, telp, alamat, jabatan, status});
            }

            rs.close();
            ps.close();
            kon.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void addTableListener() {
        jTablePegawai.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override //kenapa perlu ovveride?
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = jTablePegawai.getSelectedRow();
                    if (selectedRow != -1) { // Pastikan baris dipilih tidak kosong
                        // Ambil nilai dari baris yang dipilih
                        String id = jTablePegawai.getValueAt(selectedRow, 0).toString();
                        String nama = jTablePegawai.getValueAt(selectedRow, 1).toString();
                        String telp = jTablePegawai.getValueAt(selectedRow, 2).toString();
                        String alamat = jTablePegawai.getValueAt(selectedRow, 3).toString();
                        String jabatan = jTablePegawai.getValueAt(selectedRow, 4).toString();
                        String status = jTablePegawai.getValueAt(selectedRow, 5).toString();

                        // Set nilai yang diambil ke dalam JTextField dan JComboBox
                        jTextFieldIDPegawai.setText(id);
                        jTextFieldNamaPegawai.setText(nama);
                        jTextFieldTelpPegawai.setText(telp);
                        jTextFieldAlamatPegawai.setText(alamat);
                        jComboBoxJabatanPegawai.setSelectedItem(jabatan);
                        jComboBoxStatusPegawai.setSelectedItem(status);
                    }
                }
            }
        });
    }
    
    public void edit() {
        String id = jTextFieldIDPegawai.getText();
        String nama = jTextFieldNamaPegawai.getText();
        String telp = jTextFieldTelpPegawai.getText();
        String alamat = jTextFieldAlamatPegawai.getText();
        String jabatan = jComboBoxJabatanPegawai.getSelectedItem().toString();
        String status = jComboBoxStatusPegawai.getSelectedItem().toString();
        String usernameadmin = jTextFieldIDAdmin.getText();

        try {
            Connection kon = getKoneksi();
            String queryAdmin = "SELECT * FROM admin WHERE username = ?";
            PreparedStatement psAdmin = kon.prepareStatement(queryAdmin);
            psAdmin.setString(1, usernameadmin);
            ResultSet rs = psAdmin.executeQuery();

            if (rs.next()) {
                String query = "UPDATE pegawai SET nama_pegawai=?, telp_pegawai=?, alamat_pegawai=?, jabatan_pegawai=?, status_pegawai=? WHERE id_pegawai=?";
                PreparedStatement ps = kon.prepareStatement(query);
                ps.setString(1, nama);
                ps.setString(2, telp);
                ps.setString(3, alamat);
                ps.setString(4, jabatan);
                ps.setString(5, status);
                ps.setString(6, id);

                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(rootPane, "Data berhasil diupdate");
                    refreshTablePegawai();
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
        String id = jTextFieldIDPegawai.getText();
        String usernameadmin = jTextFieldIDAdmin.getText();

        try {
            Connection kon = getKoneksi();
            String queryAdmin = "SELECT * FROM admin WHERE username = ?";
            PreparedStatement psAdmin = kon.prepareStatement(queryAdmin);
            psAdmin.setString(1, usernameadmin);
            ResultSet rs = psAdmin.executeQuery();

            if (rs.next()) {
                String query = "DELETE FROM pegawai WHERE id_pegawai=?";
                PreparedStatement ps = kon.prepareStatement(query);
                ps.setString(1, id);

                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(rootPane, "Data berhasil dihapus");
                    refreshTablePegawai();
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

        jPanelPegawai = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabelLaporan = new javax.swing.JLabel();
        jLabelPegawai = new javax.swing.JLabel();
        jLabelFasilitas = new javax.swing.JLabel();
        jLabelKamar = new javax.swing.JLabel();
        jLabelPemesanan = new javax.swing.JLabel();
        jLabelTransaksi = new javax.swing.JLabel();
        jButtonLogOut = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldIDPegawai = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldNamaPegawai = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldTelpPegawai = new javax.swing.JTextField();
        jTextFieldAlamatPegawai = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxJabatanPegawai = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxStatusPegawai = new javax.swing.JComboBox<>();
        jButtonAddPegawai = new javax.swing.JButton();
        jButtonEditPegawai = new javax.swing.JButton();
        jButtonDeletePegawai = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePegawai = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldIDAdmin = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButtonRefreshPegawai = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanelPegawai.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(0, 0, 102));

        jLabelLaporan.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelLaporan.setForeground(new java.awt.Color(255, 255, 255));
        jLabelLaporan.setText("Laporan");
        jLabelLaporan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabelLaporanFocusGained(evt);
            }
        });
        jLabelLaporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelLaporanMouseClicked(evt);
            }
        });

        jLabelPegawai.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelPegawai.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPegawai.setText("Pegawai");

        jLabelFasilitas.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelFasilitas.setForeground(new java.awt.Color(255, 255, 255));
        jLabelFasilitas.setText("Fasilitas");

        jLabelKamar.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelKamar.setForeground(new java.awt.Color(255, 255, 255));
        jLabelKamar.setText("Kamar");
        jLabelKamar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelKamarMouseClicked(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPemesanan)
                    .addComponent(jLabelKamar)
                    .addComponent(jLabelFasilitas)
                    .addComponent(jLabelLaporan)
                    .addComponent(jLabelPegawai)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButtonLogOut)
                        .addComponent(jLabelTransaksi)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addComponent(jLabelPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelFasilitas, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelKamar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelPemesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                .addComponent(jButtonLogOut)
                .addGap(40, 40, 40))
        );

        jPanel2.setBackground(new java.awt.Color(226, 236, 255));

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 102));
        jLabel8.setText("DreamSuite");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(280, 280, 280)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("ID");

        jTextFieldIDPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldIDPegawaiActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Nama");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("No Telp");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Alamat");

        jComboBoxJabatanPegawai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Jabatan", "Manager", "Sales Marketing", "Chef", "House Keeper", "Waiter" }));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Jabatan");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Status");

        jComboBoxStatusPegawai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Status", "Aktif", "Tidak Aktif" }));

        jButtonAddPegawai.setBackground(new java.awt.Color(255, 153, 51));
        jButtonAddPegawai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAddPegawai.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAddPegawai.setText("Add");
        jButtonAddPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddPegawaiActionPerformed(evt);
            }
        });

        jButtonEditPegawai.setBackground(new java.awt.Color(255, 153, 51));
        jButtonEditPegawai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonEditPegawai.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEditPegawai.setText("Edit");
        jButtonEditPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditPegawaiActionPerformed(evt);
            }
        });

        jButtonDeletePegawai.setBackground(new java.awt.Color(255, 153, 51));
        jButtonDeletePegawai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonDeletePegawai.setForeground(new java.awt.Color(255, 255, 255));
        jButtonDeletePegawai.setText("Delete");
        jButtonDeletePegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeletePegawaiActionPerformed(evt);
            }
        });

        jTablePegawai.setBackground(new java.awt.Color(0, 0, 102));
        jTablePegawai.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTablePegawai.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        jTablePegawai.setForeground(new java.awt.Color(255, 255, 255));
        jTablePegawai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nama", "No Telp", "Alamat", "Jabatan", "Status"
            }
        ));
        jTablePegawai.setGridColor(new java.awt.Color(204, 204, 255));
        jTablePegawai.setShowGrid(true);
        jTablePegawai.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTablePegawaiFocusGained(evt);
            }
        });
        jTablePegawai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePegawaiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTablePegawai);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Username Admin");

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 153, 51));
        jLabel9.setText("Pegawai");

        jButtonRefreshPegawai.setBackground(new java.awt.Color(255, 153, 51));
        jButtonRefreshPegawai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonRefreshPegawai.setForeground(new java.awt.Color(255, 255, 255));
        jButtonRefreshPegawai.setText("Refresh");
        jButtonRefreshPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshPegawaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPegawaiLayout = new javax.swing.GroupLayout(jPanelPegawai);
        jPanelPegawai.setLayout(jPanelPegawaiLayout);
        jPanelPegawaiLayout.setHorizontalGroup(
            jPanelPegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPegawaiLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelPegawaiLayout.createSequentialGroup()
                        .addGroup(jPanelPegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelPegawaiLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jButtonAddPegawai)
                                .addGap(12, 12, 12)
                                .addComponent(jButtonEditPegawai)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonDeletePegawai))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jTextFieldIDPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jTextFieldNamaPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jTextFieldTelpPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(jTextFieldAlamatPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxJabatanPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel7)
                                .addComponent(jComboBoxStatusPegawai, 0, 234, Short.MAX_VALUE)
                                .addComponent(jTextFieldIDAdmin)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelPegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonRefreshPegawai))
                        .addContainerGap(39, Short.MAX_VALUE))))
        );
        jPanelPegawaiLayout.setVerticalGroup(
            jPanelPegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelPegawaiLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelPegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonRefreshPegawai, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPegawaiLayout.createSequentialGroup()
                        .addComponent(jTextFieldIDPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNamaPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTelpPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldAlamatPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxJabatanPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxStatusPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldIDAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addGroup(jPanelPegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonEditPegawai)
                            .addComponent(jButtonAddPegawai)
                            .addComponent(jButtonDeletePegawai)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(42, 42, 42))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelPegawai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelPegawai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldIDPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldIDPegawaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldIDPegawaiActionPerformed

    private void jButtonAddPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddPegawaiActionPerformed
        add();
    }//GEN-LAST:event_jButtonAddPegawaiActionPerformed

    private void jButtonDeletePegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeletePegawaiActionPerformed
        delete();
    }//GEN-LAST:event_jButtonDeletePegawaiActionPerformed

    private void jButtonRefreshPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshPegawaiActionPerformed
        refreshTablePegawai();
    }//GEN-LAST:event_jButtonRefreshPegawaiActionPerformed

    private void jButtonLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogOutActionPerformed
       System.exit(0);
    }//GEN-LAST:event_jButtonLogOutActionPerformed

    private void jTablePegawaiFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTablePegawaiFocusGained
        addTableListener();
    }//GEN-LAST:event_jTablePegawaiFocusGained

    private void jTablePegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePegawaiMouseClicked

    }//GEN-LAST:event_jTablePegawaiMouseClicked

    private void jButtonEditPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditPegawaiActionPerformed
        edit();
    }//GEN-LAST:event_jButtonEditPegawaiActionPerformed

    private void jLabelLaporanFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabelLaporanFocusGained
       
    }//GEN-LAST:event_jLabelLaporanFocusGained

    private void jLabelLaporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelLaporanMouseClicked
       Laporan laporan = new Laporan(); 
       laporan.setVisible(true); 

       this.setVisible(false); 
    }//GEN-LAST:event_jLabelLaporanMouseClicked

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
            java.util.logging.Logger.getLogger(Pegawai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pegawai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pegawai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pegawai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pegawai().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddPegawai;
    private javax.swing.JButton jButtonDeletePegawai;
    private javax.swing.JButton jButtonEditPegawai;
    private javax.swing.JButton jButtonLogOut;
    private javax.swing.JButton jButtonRefreshPegawai;
    private javax.swing.JComboBox<String> jComboBoxJabatanPegawai;
    private javax.swing.JComboBox<String> jComboBoxStatusPegawai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelFasilitas;
    private javax.swing.JLabel jLabelKamar;
    private javax.swing.JLabel jLabelLaporan;
    private javax.swing.JLabel jLabelPegawai;
    private javax.swing.JLabel jLabelPemesanan;
    private javax.swing.JLabel jLabelTransaksi;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelPegawai;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTablePegawai;
    private javax.swing.JTextField jTextFieldAlamatPegawai;
    private javax.swing.JTextField jTextFieldIDAdmin;
    private javax.swing.JTextField jTextFieldIDPegawai;
    private javax.swing.JTextField jTextFieldNamaPegawai;
    private javax.swing.JTextField jTextFieldTelpPegawai;
    // End of variables declaration//GEN-END:variables
}
