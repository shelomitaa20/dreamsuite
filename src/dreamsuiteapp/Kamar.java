package dreamsuiteapp;

import static dreamsuiteapp.koneksi.getKoneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Kamar extends javax.swing.JFrame {

    public Kamar() {
        initComponents();
        
    }
    
    public void add(){
        String no = jComboBoxNo.getSelectedItem().toString();
        String tipe = jComboBoxTipe.getSelectedItem().toString();
        String jenisbed = jComboBoxJenisBed.getSelectedItem().toString();
        String harga = jComboBoxHarga.getSelectedItem().toString();
        String ketersediaan = jComboBoxKetersediaan.getSelectedItem().toString();
        String nama_fasilitas = jComboBoxFasilitas.getSelectedItem().toString();
        String usernameAdmin= jTextFieldIDAdmin.getText();
        try{
            Connection kon = getKoneksi();
            Statement ps = kon.createStatement();
            
            //memeriksa apakah username ada di database
            String queryAdmin = "SELECT * FROM admin WHERE username = ?";
            PreparedStatement psAdmin = kon.prepareStatement(queryAdmin);
            psAdmin.setString(1, usernameAdmin);
            ResultSet rs = psAdmin.executeQuery();
            
             if (rs.next()) {
                String query = "INSERT INTO kamar VALUES('" + no + "','" + tipe + "', '" + jenisbed + "', '" + harga + "', '" + ketersediaan + "', '" + usernameAdmin + "','" + nama_fasilitas + "')";
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

    public void refreshTableKamar() {
        DefaultTableModel model = (DefaultTableModel) jTableKamar.getModel();
        model.setRowCount(0); // Mengosongkan isi tabel

        try {
            Connection kon = getKoneksi();
            Statement ps = kon.createStatement();
            ResultSet rs = ps.executeQuery("SELECT * FROM kamar");

            while (rs.next()) {
                String no = rs.getString("no_kamar");
                String tipe = rs.getString("tipe_kamar");
                String jenisbed = rs.getString("jenis_bed");
                String harga = rs.getString("harga_kamar");
                String ketersediaan = rs.getString("ketersediaan_kamar");
                String fasilitas = rs.getString("nama_fasilitas");

                // Menambahkan data ke dalam tabel
                model.addRow(new Object[]{no, tipe, jenisbed, harga, ketersediaan, fasilitas});
            }

            rs.close();
            ps.close();
            kon.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
 
    public void edit() {
        String no = jComboBoxNo.getSelectedItem().toString();
        String tipe = jComboBoxTipe.getSelectedItem().toString();
        String jenisbed = jComboBoxJenisBed.getSelectedItem().toString();
        String harga = jComboBoxHarga.getSelectedItem().toString();
        String ketersediaan = jComboBoxKetersediaan.getSelectedItem().toString();
        String fasilitas = jComboBoxFasilitas.getSelectedItem().toString();
        String usernameadmin = jTextFieldIDAdmin.getText();

        try {
            Connection kon = getKoneksi();
            String queryAdmin = "SELECT * FROM admin WHERE username = ?";
            PreparedStatement psAdmin = kon.prepareStatement(queryAdmin);
            psAdmin.setString(1, usernameadmin);
            ResultSet rs = psAdmin.executeQuery();

            if (rs.next()) {
                String query = "UPDATE kamar SET tipe_kamar=?, jenis_bed=?, harga_kamar=?, ketersediaan_kamar=?, nama_fasilitas=? WHERE no_kamar=?";
                PreparedStatement ps = kon.prepareStatement(query);
                ps.setString(1, tipe);
                ps.setString(2, jenisbed);
                ps.setString(3, harga);
                ps.setString(4, ketersediaan);
                ps.setString(5, fasilitas);
                ps.setString(6, no);

                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(rootPane, "Data berhasil diupdate");
                    refreshTableKamar();
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
    
    private void otomatis(){
        String selectedNo = jComboBoxNo.getSelectedItem().toString();

        // Cek apakah nomor terpilih dalam rentang 1 sampai 10
        int selectedNumber = 0;
        try {
            selectedNumber = Integer.parseInt(selectedNo);
        } catch (NumberFormatException e) {
            // Handle jika parsing gagal (misalnya jika bukan angka)
            e.printStackTrace();
        }

        // Set tipe, jenis bed, harga, dan fasilitas berdasarkan nomor terpilih
        String tipe = "";
        String jenisBed = "";
        String harga = "";
        String fasilitas = "";

        if (selectedNumber >= 1 && selectedNumber <= 10) {
            tipe = "Standard Room";
            jenisBed = "Single Bed";
            harga = "500.000";
            fasilitas = "Junior Facility";
        } else if (selectedNumber >= 11 && selectedNumber <= 20) {
            tipe = "Superior Room";
            jenisBed = "Double Bed";
            harga = "750.000";
            fasilitas = "Romantic Facility";
        } else if (selectedNumber >= 21 && selectedNumber <= 30) {
            tipe = "Deluxe Room";
            jenisBed = "King Bed";
            harga = "1.000.000";
            fasilitas = "Family Facility";
        }

        // Set nilai pada JComboBox sesuai dengan hasil pengecekan
        jComboBoxTipe.setSelectedItem(tipe);
        jComboBoxJenisBed.setSelectedItem(jenisBed);
        jComboBoxHarga.setSelectedItem(harga);
        jComboBoxFasilitas.setSelectedItem(fasilitas);
    }


    private void addTableListener() {
        jTableKamar.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override //kenapa perlu ovveride?
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = jTableKamar.getSelectedRow();
                    if (selectedRow != -1) { // Pastikan baris dipilih tidak kosong
                        // Ambil nilai dari baris yang dipilih
                        String no = jTableKamar.getValueAt(selectedRow, 0).toString();
                        String tipe = jTableKamar.getValueAt(selectedRow, 1).toString();
                        String jenisbed = jTableKamar.getValueAt(selectedRow, 2).toString();
                        String harga = jTableKamar.getValueAt(selectedRow, 3).toString();
                        String ketersediaan = jTableKamar.getValueAt(selectedRow, 4).toString();
                        String fasilitas = jTableKamar.getValueAt(selectedRow, 5).toString();

                        // Set nilai yang diambil ke dalam JTextField dan JComboBox
                        jComboBoxNo.setSelectedItem(no);
                        jComboBoxTipe.setSelectedItem(tipe);
                        jComboBoxJenisBed.setSelectedItem(jenisbed);
                        jComboBoxHarga.setSelectedItem(harga);
                        jComboBoxKetersediaan.setSelectedItem(ketersediaan);
                        jComboBoxFasilitas.setSelectedItem(fasilitas);
                    }
                }
            }
        });
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
        jLabelLaporan = new javax.swing.JLabel();
        jLabelFasilitas2 = new javax.swing.JLabel();
        jLabelKamar2 = new javax.swing.JLabel();
        jLabelPemesanan = new javax.swing.JLabel();
        jLabelTransaksi = new javax.swing.JLabel();
        jButtonLogOut = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabelDreamSuite = new javax.swing.JLabel();
        jLabelKamar = new javax.swing.JLabel();
        jLabelNo = new javax.swing.JLabel();
        jComboBoxNo = new javax.swing.JComboBox<>();
        jLabelTipe = new javax.swing.JLabel();
        jComboBoxTipe = new javax.swing.JComboBox<>();
        jLabelJenisBed = new javax.swing.JLabel();
        jComboBoxJenisBed = new javax.swing.JComboBox<>();
        jLabelHarga = new javax.swing.JLabel();
        jComboBoxHarga = new javax.swing.JComboBox<>();
        jLabelKetersediaan = new javax.swing.JLabel();
        jComboBoxKetersediaan = new javax.swing.JComboBox<>();
        jLabelFasilitas = new javax.swing.JLabel();
        jComboBoxFasilitas = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableKamar = new javax.swing.JTable();
        jButtonAdd = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();
        jLabelUsernameAdmin = new javax.swing.JLabel();
        jTextFieldIDAdmin = new javax.swing.JTextField();

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

        jLabelLaporan.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelLaporan.setForeground(new java.awt.Color(255, 255, 255));
        jLabelLaporan.setText("Laporan");
        jLabelLaporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelLaporanMouseClicked(evt);
            }
        });

        jLabelFasilitas2.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelFasilitas2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelFasilitas2.setText("Fasilitas");

        jLabelKamar2.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelKamar2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelKamar2.setText("Kamar");

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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelPegawai)
                            .addComponent(jLabelLaporan)
                            .addComponent(jLabelFasilitas2)
                            .addComponent(jLabelKamar2)
                            .addComponent(jLabelPemesanan)
                            .addComponent(jLabelTransaksi)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jButtonLogOut)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addComponent(jLabelPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabelFasilitas2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelKamar2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelPemesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonLogOut)
                .addGap(32, 32, 32))
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
                .addGap(192, 192, 192)
                .addComponent(jLabelDreamSuite)
                .addContainerGap(385, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabelDreamSuite)
                .addContainerGap())
        );

        jLabelKamar.setFont(new java.awt.Font("Microsoft YaHei", 1, 18)); // NOI18N
        jLabelKamar.setForeground(new java.awt.Color(255, 153, 51));
        jLabelKamar.setText("Kamar");

        jLabelNo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelNo.setText("No");

        jComboBoxNo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih No", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        jComboBoxNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNoActionPerformed(evt);
            }
        });

        jLabelTipe.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTipe.setText("Tipe");

        jComboBoxTipe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Tipe", "Standard Room", "Superior Room", "Deluxe Room" }));
        jComboBoxTipe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTipeActionPerformed(evt);
            }
        });

        jLabelJenisBed.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelJenisBed.setText("Jenis Bed");

        jComboBoxJenisBed.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jenis Bed", "Single Bed", "Double Bed", "King Bed" }));
        jComboBoxJenisBed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxJenisBedActionPerformed(evt);
            }
        });

        jLabelHarga.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelHarga.setText("Harga");

        jComboBoxHarga.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Harga", "500000", "750000", "1000000" }));
        jComboBoxHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxHargaActionPerformed(evt);
            }
        });

        jLabelKetersediaan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelKetersediaan.setText("Ketersediaan");

        jComboBoxKetersediaan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Ketersediaan", "Tersedia", "Tidak Tersedia" }));
        jComboBoxKetersediaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxKetersediaanActionPerformed(evt);
            }
        });

        jLabelFasilitas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelFasilitas.setText("Fasilitas");

        jComboBoxFasilitas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Fasilitas", "Junior Facility", "Romantic Facility", "Family Facility" }));
        jComboBoxFasilitas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFasilitasActionPerformed(evt);
            }
        });

        jTableKamar.setBackground(new java.awt.Color(0, 0, 102));
        jTableKamar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTableKamar.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        jTableKamar.setForeground(new java.awt.Color(255, 255, 255));
        jTableKamar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Tipe", "Kapasitas", "Harga", "Status", "Fasilitas"
            }
        ));
        jTableKamar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTableKamarFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(jTableKamar);

        jButtonAdd.setBackground(new java.awt.Color(255, 153, 51));
        jButtonAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAdd.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAdd.setText("Add");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonEdit.setBackground(new java.awt.Color(255, 153, 51));
        jButtonEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonEdit.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEdit.setText("Edit");
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });

        jButtonRefresh.setBackground(new java.awt.Color(255, 153, 51));
        jButtonRefresh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonRefresh.setForeground(new java.awt.Color(255, 255, 255));
        jButtonRefresh.setText("Refresh");
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });

        jLabelUsernameAdmin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelUsernameAdmin.setText("Username Admin");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButtonRefresh))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabelJenisBed, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelTipe, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBoxNo, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelKamar)
                                        .addComponent(jLabelNo)
                                        .addComponent(jLabelHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelFasilitas, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelKetersediaan)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jButtonAdd)
                                            .addGap(18, 18, 18)
                                            .addComponent(jButtonEdit))
                                        .addComponent(jComboBoxTipe, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBoxJenisBed, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBoxHarga, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBoxKetersediaan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBoxFasilitas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabelUsernameAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldIDAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(29, 29, 29))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelKamar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jButtonRefresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelNo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelTipe)
                        .addGap(5, 5, 5)
                        .addComponent(jComboBoxTipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelJenisBed)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxJenisBed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelHarga)
                        .addGap(2, 2, 2)
                        .addComponent(jComboBoxHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelKetersediaan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxKetersediaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelFasilitas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxFasilitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelUsernameAdmin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldIDAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonAdd)
                            .addComponent(jButtonEdit))
                        .addGap(33, 33, 33))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxNoActionPerformed
          otomatis();
    }//GEN-LAST:event_jComboBoxNoActionPerformed

    private void jComboBoxTipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTipeActionPerformed
      
    }//GEN-LAST:event_jComboBoxTipeActionPerformed

    private void jComboBoxJenisBedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxJenisBedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxJenisBedActionPerformed

    private void jComboBoxHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxHargaActionPerformed

    private void jComboBoxKetersediaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxKetersediaanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxKetersediaanActionPerformed

    private void jComboBoxFasilitasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFasilitasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxFasilitasActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        add();
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditActionPerformed
        edit();
    }//GEN-LAST:event_jButtonEditActionPerformed

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshActionPerformed
        refreshTableKamar();
    }//GEN-LAST:event_jButtonRefreshActionPerformed

    private void jButtonLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogOutActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButtonLogOutActionPerformed

    private void jTableKamarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTableKamarFocusGained
        addTableListener();
    }//GEN-LAST:event_jTableKamarFocusGained

    private void jLabelPegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelPegawaiMouseClicked
       Pegawai pegawai = new Pegawai(); 
       pegawai.setVisible(true); 

       this.setVisible(false); 
    }//GEN-LAST:event_jLabelPegawaiMouseClicked

    private void jLabelLaporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelLaporanMouseClicked
       Laporan laporan = new Laporan(); 
       laporan.setVisible(true); 

       this.setVisible(false); 
    }//GEN-LAST:event_jLabelLaporanMouseClicked

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
            java.util.logging.Logger.getLogger(Kamar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Kamar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Kamar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Kamar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new Kamar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonLogOut;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JComboBox<String> jComboBoxFasilitas;
    private javax.swing.JComboBox<String> jComboBoxHarga;
    private javax.swing.JComboBox<String> jComboBoxJenisBed;
    private javax.swing.JComboBox<String> jComboBoxKetersediaan;
    private javax.swing.JComboBox<String> jComboBoxNo;
    private javax.swing.JComboBox<String> jComboBoxTipe;
    private javax.swing.JLabel jLabelDreamSuite;
    private javax.swing.JLabel jLabelFasilitas;
    private javax.swing.JLabel jLabelFasilitas2;
    private javax.swing.JLabel jLabelHarga;
    private javax.swing.JLabel jLabelJenisBed;
    private javax.swing.JLabel jLabelKamar;
    private javax.swing.JLabel jLabelKamar2;
    private javax.swing.JLabel jLabelKetersediaan;
    private javax.swing.JLabel jLabelLaporan;
    private javax.swing.JLabel jLabelNo;
    private javax.swing.JLabel jLabelPegawai;
    private javax.swing.JLabel jLabelPemesanan;
    private javax.swing.JLabel jLabelTipe;
    private javax.swing.JLabel jLabelTransaksi;
    private javax.swing.JLabel jLabelUsernameAdmin;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableKamar;
    private javax.swing.JTextField jTextFieldIDAdmin;
    // End of variables declaration//GEN-END:variables
}
