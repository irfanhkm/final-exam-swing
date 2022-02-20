package app.data;

import java.util.Vector;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Produk {
    private String kodeBahan;
    private String namaBahan;
    private String satuan;
    private String jenisBahan;
    private Double harga;
    private Integer jumlah;

    public Vector<String> toVectorDataTable() {
        Vector<String> vector = new Vector<>();
        vector.add(this.kodeBahan);
        vector.add(this.namaBahan);
        vector.add(this.satuan);
        vector.add(this.jenisBahan);
        vector.add(this.harga.toString());
        vector.add(this.jumlah.toString());
        return vector;
    }
}
