package app;

import app.data.Produk;
import app.query.ProductQuery;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public final class ProductTableFrame extends JFrame {
    // Singleton
    private static ProductTableFrame INSTANCE;

    // table
    private final DefaultTableModel dtm = new DefaultTableModel(); // data utk tabel
    private final JTable jTable = new JTable(); // layout tabel

    // query
    private final ProductQuery productQuery = ProductQuery.getInstance();

    // Singleton
    public static ProductTableFrame getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProductTableFrame();
        }

        return INSTANCE;
    }

    private ProductTableFrame() {
        this.setTitle("Product Form");
        this.setSize(800, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        jTable.setBounds(30, 40, 500, 500);

        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(jTable);
        this.add(sp);
        this.setVisible(true);

        refreshData();
    }

    public void refreshData() {
        // clear data before refresh
        dtm.setRowCount(0);
        dtm.setColumnCount(0);

        // heading table
        dtm.addColumn("Kode Bahan");
        dtm.addColumn("Nama Bahan");
        dtm.addColumn("Satuan");
        dtm.addColumn("Jenis Bahan");
        dtm.addColumn("Harga");
        dtm.addColumn("Jumlah");
        dtm.addColumn("Aksi");
        dtm.addColumn("Hapus");

        // data table
        List<Produk> produkList = productQuery.getProductList();
        for (Produk produk: produkList) {
            dtm.addRow(produk.toVectorDataTable());
        }

        jTable.setModel(dtm);

        jTable.getColumn("Aksi").setCellRenderer(new ButtonRenderer("Edit"));
        jTable.getColumn("Aksi").setCellEditor(new EditButtonEditor(new JCheckBox()));

        jTable.getColumn("Hapus").setCellRenderer(new ButtonRenderer("Hapus"));
        jTable.getColumn("Hapus").setCellEditor(new DeleteButtonEditor(new JCheckBox(), productQuery));
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer
    {
        private final String label;
        public ButtonRenderer(String label) {
            setOpaque(true);
            this.label = label;
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(label);
            return this;
        }
    }

    static class EditButtonEditor extends DefaultCellEditor
    {
        public EditButtonEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            System.out.println(table.getValueAt(row, 2).toString());
            Produk produk = Produk.builder()
                .kodeBahan(table.getValueAt(row, 0).toString())
                .namaBahan(table.getValueAt(row, 1).toString())
                .satuan(table.getValueAt(row, 2).toString())
                .jenisBahan(table.getValueAt(row, 3).toString())
                .harga(Double.parseDouble(table.getValueAt(row, 4).toString()))
                .jumlah(Integer.parseInt(table.getValueAt(row, 5).toString()))
                .build();
            JButton button = new JButton("Edit");
            button.addActionListener(e -> {
                ProductFormFrame frame = new ProductFormFrame(produk);
                frame.setVisible(true);
            });

            return button;
        }

        public Object getCellEditorValue()
        {
            return "Edit";
        }
    }

    static class DeleteButtonEditor extends DefaultCellEditor
    {
        private ProductQuery productQuery;
        public DeleteButtonEditor(JCheckBox checkBox, ProductQuery productQuery) {
            super(checkBox);
            this.productQuery = productQuery;
        }
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            JButton button = new JButton("Hapus");
            button.addActionListener(e -> {
                productQuery.deleteProdukByKodeBahan(table.getValueAt(row, 0).toString());
                ProductTableFrame.getInstance().refreshData();
            });

            return button;
        }

        public Object getCellEditorValue() {
            return "Hapus";
        }
    }
}
