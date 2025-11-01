public class PetaProyek {
    Zona[][] grid;

    public PetaProyek(Zona[][] grid) {
        this.grid = grid;
    }

    public void tampilkanProgress() {
        System.out.println("\n==============================");
        System.out.println("       PROGRES PROYEK          ");
        System.out.println("==============================");
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Zona z = grid[i][j];
                System.out.print(z.getNama() + "[" + (z.isSelesai() ? "✔" : " ") + "]\t");
            }
            System.out.println();
        }
        System.out.println("==============================\n");
    }

    public double hitungBiayaTotal() {
        double total = 0;
        for (Zona[] baris : grid) {
            for (Zona z : baris) {
                total += z.getBiaya();
            }
        }
        return total;
    }

   public int hitungWaktuSelesai() {
    int total = 0;
    for (Zona[] baris : grid) {
        for (Zona z : baris) {
            if (z!= null) {
                total += z.getDurasiHari(); // tambahkan durasi tiap zona
            }
        }
    }
    return total;
}
}
