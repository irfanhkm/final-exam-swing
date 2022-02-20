package app.query;

import app.data.Produk;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductQuery {
    // Singleton
    private static ProductQuery INSTANCE;

    // connection database
    private final DbConnection dbConnection = DbConnectionImpl.getInstance();

    private ProductQuery() {
        //
    }

    // Singleton
    public static ProductQuery getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProductQuery();
        }

        return INSTANCE;
    }

    public List<Produk> getProductList() {
        List<Produk> produkList = new ArrayList<>();
        try {
            ResultSet rs = dbConnection.executeQuery("select * from produk");
            while(rs.next()) {
                produkList.add(
                    Produk.builder()
                        .kodeBahan(rs.getString("kodeBahan"))
                        .namaBahan(rs.getString("namaBahan"))
                        .satuan(rs.getString("satuan"))
                        .jenisBahan(rs.getString("jenisBahan"))
                        .harga(rs.getDouble("harga"))
                        .jumlah(rs.getInt("jumlah"))
                        .build()
                );
            }
        } catch (Exception e) {
            System.out.printf("error when execute query %s%n", e);
        }
        return produkList;
    }

    private String randomId() {
        Random random = new Random();
        int leftLimit = 97; // letter `a`
        int rightLimit = 122; // letter `z`
        return random.ints(leftLimit, rightLimit + 1)
            .limit(5)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    public void insertProduk(Produk produk) {
        String query = "INSERT INTO produk ("
            + "`KodeBahan`, `NamaBahan`, `Satuan`, `JenisBahan`, `Harga`, `Jumlah`)";
        String values = String.format(" VALUES ('%s', '%s', '%s', '%s', %s, %s)",
            randomId(), produk.getNamaBahan(), produk.getSatuan(), produk.getJenisBahan(),
            produk.getHarga(), produk.getJumlah());

        System.out.printf("query = %s", query + values);
        dbConnection.executeUpdate(query + values);
    }

    public void updateProduk(Produk produk) {
        String plainQuery = "UPDATE produk SET `NamaBahan`='%s', `Satuan`='%s', "
            + "`JenisBahan`='%s', `Harga`= %s, `Jumlah`=%s WHERE `kodeBahan`= '%s'";
        String query = String.format(plainQuery, produk.getNamaBahan(), produk.getSatuan(),
            produk.getJenisBahan(), produk.getHarga(), produk.getJumlah(), produk.getKodeBahan()
        );

        System.out.printf("query = %s ", query);
        dbConnection.executeUpdate(query);
    }

    public void deleteProdukByKodeBahan(String kodeBahan) {
        String query = String.format("delete from produk where `KodeBahan` = '%s'", kodeBahan);
        System.out.printf("query = %s ", query);
        dbConnection.executeUpdate(query);
    }
}
