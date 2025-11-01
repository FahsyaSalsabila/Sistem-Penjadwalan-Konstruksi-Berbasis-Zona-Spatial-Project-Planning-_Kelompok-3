import java.util.ArrayList;
import java.util.List;

public class Proyek {
    String namaProyek;
    List<Zona> semuaZona;
    PetaProyek peta;

    public Proyek(String namaProyek, List<Zona> semuaZona, PetaProyek peta) {
        this.namaProyek = namaProyek;
        this.semuaZona = semuaZona;
        this.peta = peta;
    }

    public List<Zona> mulaiSimulasi() {
        List<Zona> selesaiHariIni = new ArrayList<>();

        for (Zona z : semuaZona) {
            if (!z.isSelesai() && z.cekKetergantungan()) {
                boolean selesai = z.mulaiZona();
                if (selesai) selesaiHariIni.add(z);
            }
        }

        peta.tampilkanProgress();
        return selesaiHariIni;
    }

    public boolean semuaSelesai() {
        for (Zona z : semuaZona) {
            if (!z.isSelesai()) return false;
        }
        return true;
    }
}
