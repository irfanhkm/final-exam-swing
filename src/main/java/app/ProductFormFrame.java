package app;

import app.data.Produk;
import app.query.ProductQuery;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import lombok.Getter;

public class ProductFormFrame extends JFrame {

    // layouting
    private final JPanel panel = new JPanel(new GridBagLayout());
    private static final ProductTableFrame mainFrame = ProductTableFrame.getInstance();

    // query db
    private final ProductQuery productQuery = ProductQuery.getInstance();
    @Getter
    private final Produk produk;

    // data form
    private final List<List<JComponent>> listInput = new ArrayList<>();

    public ProductFormFrame(Produk produk) {
        this.produk = produk;
        this.setTitle("Form Produk");
        this.setSize(400, 600);
        this.setLocationRelativeTo(null);
        this.add(panel);

        setupForm();
        buildForm();
    }

    private GridBagConstraints constraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
        return constraints;
    }

    private void buildForm() {
        GridBagConstraints constraints = constraints();

        for (List<JComponent> components: listInput) {
            if (components.size() == 2) {
                constraints.gridy += 1;
                constraints.gridx = 0;
                panel.add(components.get(0), constraints);
                constraints.gridx = 1;
                panel.add(components.get(1), constraints);
            } else {
                constraints.gridy += 1;
                constraints.gridx = 1;
                panel.add(components.get(0), constraints);
            }
        }
    }

    private void setupForm() {
        JTextField inputNamaBahan = new JTextField(20);
        if (getProduk() != null && getProduk().getNamaBahan() != null) {
            inputNamaBahan.setText(produk.getNamaBahan());
        }
        listInput.add(new ArrayList<>(
            Arrays.asList(
                new JLabel("Nama Bahan"),
                inputNamaBahan
            )
        ));

        JComboBox<String> satuanOption = new JComboBox<>();
        satuanOption.addItem("Butir");
        satuanOption.addItem("Botol");
        satuanOption.addItem("Kg");
        satuanOption.addItem("Buah");

        if (getProduk() != null && getProduk().getSatuan() != null) {
            satuanOption.setSelectedItem(getProduk().getSatuan());
        }
        listInput.add(new ArrayList<>(
            Arrays.asList(
                new JLabel("Satuan Bahan"),
                satuanOption
            )
        ));

        JComboBox<String> jenisBahanOption = new JComboBox<>();
        jenisBahanOption.addItem("Baku");
        jenisBahanOption.addItem("Dekorasi");
        if (getProduk() != null && getProduk().getJenisBahan() != null) {
            jenisBahanOption.setSelectedItem(produk.getJenisBahan());
        }
        listInput.add(new ArrayList<>(
            Arrays.asList(
                new JLabel("Jenis Bahan"),
                jenisBahanOption
            )
        ));

        NumberFormat doubleFormat = NumberFormat.getNumberInstance();
        JFormattedTextField inputHarga = new JFormattedTextField(doubleFormat);
        inputHarga.setName("Harga");
        inputHarga.setColumns(20);
        doubleFormat.setMinimumFractionDigits(2);
        doubleFormat.setMaximumFractionDigits(2);
        doubleFormat.setRoundingMode(RoundingMode.HALF_UP);
        if (getProduk() != null && getProduk().getHarga() != null) {
            inputHarga.setText(getProduk().getHarga().toString());
        }
        listInput.add(new ArrayList<>(
            Arrays.asList(
                new JLabel("Harga"),
                inputHarga
            )
        ));

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        JFormattedTextField inputJumlah = new JFormattedTextField(integerFormat);
        inputJumlah.setName("Jumlah");
        inputJumlah.setColumns(20);
        if (getProduk() != null && getProduk().getJumlah() != null) {
            inputJumlah.setText(getProduk().getJumlah().toString());
        }
        listInput.add(new ArrayList<>(
            Arrays.asList(
                new JLabel("Jumlah"),
                inputJumlah
            )
        ));

        JButton buttonSubmit = new JButton("Submit");
        buttonSubmit.addActionListener(
            e -> {
                boolean validated = checkingNullData();
                if (validated) {
                    if (getProduk() == null) {
                        productQuery.insertProduk(
                            Produk.builder()
                                .namaBahan(inputNamaBahan.getText())
                                .satuan((String) satuanOption.getSelectedItem())
                                .jenisBahan((String) jenisBahanOption.getSelectedItem())
                                .harga(Double.valueOf(inputHarga.getText().replaceAll(",", "")))
                                .jumlah(Integer.parseInt(inputJumlah.getText().replaceAll(",", "")))
                                .build()
                        );
                    } else {
                        productQuery.updateProduk(
                            Produk.builder()
                                .kodeBahan(getProduk().getKodeBahan())
                                .namaBahan(inputNamaBahan.getText())
                                .satuan((String) satuanOption.getSelectedItem())
                                .jenisBahan((String) jenisBahanOption.getSelectedItem())
                                .harga(Double.valueOf(inputHarga.getText().replaceAll(",", "")))
                                .jumlah(Integer.parseInt(inputJumlah.getText().replaceAll(",", "")))
                                .build()
                        );
                    }
                    mainFrame.refreshData();
                    this.dispose();
                    showDialog(this, "Success");
                } else {
                    showDialog(this, "Form harus diisi semua");
                }
            }
        );
        listInput.add(new ArrayList<>(
            Collections.singletonList(buttonSubmit)
        ));
    }

    private boolean checkingNullData() {
        boolean validated = true;
        List<JComponent> components = listInput.stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());

        for (JComponent component: components) {
            if (component instanceof JTextField) {
                if (((JTextField) component).getText().equals("")) {
                    validated = false;
                }
                continue;
            }
            if (component instanceof JComboBox) {
                Object item = ((JComboBox) component).getSelectedItem();
                if (item != null && item.equals("")) {
                    validated = false;
                }
            }
        }
        return validated;
    }

    private void showDialog(JFrame frame, String text) {
        JDialog jDialog = new JDialog(frame);
        JLabel l = new JLabel(text);
        jDialog.add(l);
        // setsize of dialog
        jDialog.setSize(300, 300);
        // set visibility of dialog
        jDialog.setVisible(true);
    }

}
