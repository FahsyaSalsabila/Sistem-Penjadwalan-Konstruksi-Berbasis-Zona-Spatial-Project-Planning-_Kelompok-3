import java.util.ArrayList;
import java.util.List;

public class Zona {
    private String nama;
    private String tipe;
    private int durasiHari;
    private double biaya;
    boolean selesai = false;
    private List<Zona> ketergantungan = new ArrayList<>();

    public Zona(String nama, String tipe, int durasiHari, double biaya) {
        this.nama = nama;
        this.tipe = tipe;
        this.durasiHari = durasiHari;
        this.biaya = biaya;
    }

    public String getNama() { 
    return nama; }
    public String getTipe() { 
    return tipe; }
    public boolean isSelesai() { 
    return selesai; }
    public int getDurasiHari() { 
    return durasiHari; }
    public double getBiaya() { 
    return biaya; }

    public void tambahKetergantungan(Zona z) {
        this.ketergantungan.add(z);
    }

    public boolean cekKetergantungan() {
        for (Zona z : ketergantungan) {
            if (!z.selesai) return false;
        }
        return true;
    }

    //  simulasi pengerjaan zona selama 1 hari
    public boolean mulaiZona() {
        if (cekKetergantungan() && !selesai) {
            durasiHari--;
            if (durasiHari <= 0) {
                selesai = true;
                System.out.println("Zona " + nama + " (" + tipe + ") telah SELESAI.");
                return true; // selesai hari ini
            } else {
                System.out.println("Zona " + nama + " sedang dikerjakan (" + durasiHari + " hari tersisa).");
            }
        }
        return false;
    }
}
