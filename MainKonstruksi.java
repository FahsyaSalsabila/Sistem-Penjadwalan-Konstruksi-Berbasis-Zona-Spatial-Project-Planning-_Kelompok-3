import java.util.*;

public class MainKonstruksi {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("     SIMULASI PENJADWALAN KONSTRUKSI 4x4 ZONA");
        System.out.println("==================================================");
        
        // Input nama proyek
        System.out.print("Masukkan nama proyek: ");
        String namaProyek = sc.nextLine();

        Zona[][] grid = new Zona[4][4];

        // Input data zona
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println("\nZona [" + (i + 1) + "," + (j + 1) + "]");
                System.out.print("Nama Zona: ");
                String nama = sc.next();

                String tipe;
                while (true) {
                    System.out.print("Tipe (Pondasi/Dinding/Atap/Interior): ");
                    tipe = sc.next();
                    if (tipe.equalsIgnoreCase("Pondasi") ||
                        tipe.equalsIgnoreCase("Dinding") ||
                        tipe.equalsIgnoreCase("Atap") ||
                        tipe.equalsIgnoreCase("Interior")) break;
                    System.out.println("Tipe tidak valid, ulangi input!");
                }

                int durasi = 0;
                while (true) {
                    try {
                        System.out.print("Durasi (hari): ");
                        durasi = sc.nextInt();
                        if (durasi > 0) break;
                        System.out.println("Durasi harus > 0!");
                    } catch (Exception e) {
                        System.out.println("Masukkan angka valid!");
                        sc.nextLine();
                    }
                }

                double biaya = 0;
                while (true) {
                    try {
                        System.out.print("Biaya (Rp): ");
                        biaya = sc.nextDouble();
                        if (biaya > 0) break;
                        System.out.println("Biaya harus > 0!");
                    } catch (Exception e) {
                        System.out.println("Masukkan angka valid!");
                        sc.nextLine();
                    }
                }

                grid[i][j] = new Zona(nama, tipe, durasi, biaya);
            }
        }

        // Ubah grid ke list
        List<Zona> semuaZona = new ArrayList<>();
        for (Zona[] baris : grid) semuaZona.addAll(Arrays.asList(baris));

        // Input ketergantungan antar zona
        System.out.println("\n====================================================================================");
        System.out.println("                           ATUR KETERGANTUNGAN ZONA ");
        System.out.println("====================================================================================");
        for (Zona z : semuaZona) {
            System.out.print("Apakah zona " + z.getNama() + " punya ketergantungan? (ya/tidak): ");
            String jawab = sc.next();
            if (jawab.equalsIgnoreCase("ya")) {
                while (true) {
                    System.out.print("Masukkan nama zona yang harus selesai dulu (atau 'selesai' untuk berhenti): ");
                    String dep = sc.next();
                    if (dep.equalsIgnoreCase("selesai")) break;

                    Zona ket = cariZona(semuaZona, dep);
                    if (ket != null) {
                        z.tambahKetergantungan(ket);
                        System.out.println(">" + z.getNama() + " tergantung pada " + ket.getNama());
                    } else {
                        System.out.println("Zona tidak ditemukan!");
                    }
                }
            }
        }

        // Buat proyek 
        PetaProyek peta = new PetaProyek(grid);
        Proyek proyek = new Proyek(namaProyek, semuaZona, peta);

        double totalEfisiensi = 0;
        double totalBiayaAwal = peta.hitungBiayaTotal();
        double totalBiayaSekarang = totalBiayaAwal;

        int hari = 0;

        System.out.println("\n===========================================");
        System.out.println(" Simulasi Proyek: " + proyek.namaProyek);
        System.out.println("===========================================");

        // Loop harian simulasi
        while (!proyek.semuaSelesai()) {
            hari++; 
            System.out.println("\n=============== HARI KE-" + hari + " =================\n");
            List<Zona> selesaiHariIni = proyek.mulaiSimulasi();

            // Hitung efisiensi (10% per pasangan zona bersebelahan yang selesai bersamaan)
            double efisiensiHariIni = cekEfisiensi(peta.grid, selesaiHariIni);
            totalEfisiensi += efisiensiHariIni;

            if (efisiensiHariIni > 0) {
                System.out.printf(">> Efisiensi hari ini: +%.2f%%\n", efisiensiHariIni);

            }

            totalBiayaSekarang = totalBiayaAwal * (1 - totalEfisiensi / 100);
            System.out.printf(">> Biaya proyek terkini: Rp %,.0f\n", totalBiayaSekarang);


            try { Thread.sleep(800); } catch (Exception e) {}
        }

        System.out.println("\n>> Semua zona selesai pada hari ke-" + hari);
        System.out.printf(">> Total biaya sebelum efisiensi: Rp %,.0f\n", totalBiayaAwal);
        System.out.printf(">> Total efisiensi: %.2f%%\n", totalEfisiensi);
        System.out.printf(">> Total biaya setelah efisiensi: Rp %,.0f\n", totalBiayaSekarang);

        sc.close();
    }

    // Cari zona berdasarkan nama
    public static Zona cariZona(List<Zona> semuaZona, String nama) {
        for (Zona z : semuaZona)
            if (z.getNama().equalsIgnoreCase(nama)) return z;
        return null;
    }

    // Hitung efisiensi antar zona yang selesai bersamaan
    public static double cekEfisiensi(Zona[][] grid, List<Zona> selesaiHariIni) {
        double efisiensi = 0;
        List<String> pasanganEfisien = new ArrayList<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Zona a = grid[i][j];
                if (a.isSelesai() && selesaiHariIni.contains(a)) {
                    if (j + 1 < grid[i].length && grid[i][j + 1].isSelesai() && selesaiHariIni.contains(grid[i][j + 1])) {
                        efisiensi += 10;
                        pasanganEfisien.add(a.getNama() + " & " + grid[i][j + 1].getNama());
                    }
                    if (i + 1 < grid.length && grid[i + 1][j].isSelesai() && selesaiHariIni.contains(grid[i + 1][j])) {
                        efisiensi += 10;
                        pasanganEfisien.add(a.getNama() + " & " + grid[i + 1][j].getNama());
                    }
                }
            }
        }

        if (!pasanganEfisien.isEmpty()) {
            System.out.println(">> Zona bersebelahan selesai bersama: " + String.join(", ", pasanganEfisien));
        }

        return efisiensi;
    }
}
