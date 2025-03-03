
import DAO.DaoGoodsReceipts;
import DAO.DaoProduct;
import DAO.DaoTree;
import DAO.DaoVendor;
import DAO.DaoGoodsIssuance;
import Panel.AllFrames;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author athif
 */

public class Main {
    public static void main(String[] args) {
        // Contoh instansiasi DaoTree dan DaoTreeRelations
        DaoTree daoTree = new DaoTree();
        
          // Inisialisasi DAO
        DaoProduct daoProduct = new DaoProduct();
        DaoVendor daoVendor = new DaoVendor();
        DaoGoodsReceipts daoGoodsReceipts = new DaoGoodsReceipts();
        // Buat koneksi ke database
//        DataSource dataSource= ConnectionDb.getInstance();
        DaoGoodsIssuance daoGoodsIssuance = new DaoGoodsIssuance();
        
        // Menampilkan frame
        AllFrames frame = new AllFrames(daoTree,daoProduct,daoVendor,daoGoodsReceipts, daoGoodsIssuance);
        frame.setVisible(true);
    }
    }

//
//import DAO.DaoGoodsReceipts;
//import DAO.DaoProduct;
//import DAO.DaoTree;
//import DAO.DaoVendor;
//import DAO.DaoGoodsIssuance;
//import DAO.DaoReceiptItems;
//import Panel.AllFrames;
//import java.sql.Connection;
//import Conn.ConnectionDb;
//import DAO.ImplementVendor;
//import Model.ModelGoodsIssuance;
//import Model.ModelGoodsReceipts;
//import Model.ModelIssuanceItem;
//import Model.ModelReceiptItems;
//import java.math.BigDecimal;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Random;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//
//
//public class Main {
//    public static void main(String[] args) {
//        // Contoh instansiasi DaoTree dan DaoTreeRelations
//        DaoTree daoTree = new DaoTree();
//
//
//        // Inisialisasi DAO dengan koneksi database yang sama
//        DaoProduct daoProduct = new DaoProduct();
//        DaoVendor daoVendor = new DaoVendor();
//        DaoGoodsReceipts daoGoodsReceipts = new DaoGoodsReceipts();  // Pastikan koneksi yang sama digunakan
//        DaoGoodsIssuance daoGoodsIssuance = new DaoGoodsIssuance();
//        DaoReceiptItems daoReceiptItems = new DaoReceiptItems();  // DAO untuk pengujian item penerimaan
//
//        // Menampilkan frame
//        AllFrames frame = new AllFrames(daoTree, daoProduct, daoVendor, daoGoodsReceipts, daoGoodsIssuance);
//        frame.setVisible(true);
//
//        // Membuat pool thread dengan 20 thread untuk simulasi 20 user
//         ExecutorService executor = Executors.newFixedThreadPool(50);
//
//        for (int i = 0; i < 50; i++) {
//            int finalI = i;
//            executor.submit(() -> {
//                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
//                System.out.println("Thread " + finalI + " starts at " + timestamp);
//                new UserTask(finalI, TaskType.getRandomTaskType(), daoGoodsReceipts, daoReceiptItems, daoVendor, daoProduct).run();
//                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
//                System.out.println("Thread " + finalI + " ends at " + timestamp);
//            });
//        }
//
//        executor.shutdown();
//    }
//
//    enum TaskType {
//        ADD_ID_4_QTY_5,
//        UPDATE_ID_6_QTY_5,
//        ADD_ID_4_QTY_3,
//        UPDATE_ID_6_QTY_6;
//
//        private static final Random RANDOM = new Random();
//
//        public static TaskType getRandomTaskType() {
//            TaskType[] values = TaskType.values();
//            return values[RANDOM.nextInt(values.length)];
//        }
//    }
//
//    static class UserTask implements Runnable {
//        private final int userId;
//        private final TaskType taskType;
//        private final DaoGoodsReceipts daoGoodsReceipts;
//        private final DaoReceiptItems daoReceiptItems;
//        private final ImplementVendor daoVendor;
//        private final DaoProduct daoProduct;
//
//        public UserTask(int userId, TaskType taskType, DaoGoodsReceipts daoGoodsReceipts, DaoReceiptItems daoReceiptItems, ImplementVendor daoVendor, DaoProduct daoProduct) {
//            this.userId = userId;
//            this.taskType = taskType;
//            this.daoGoodsReceipts = daoGoodsReceipts;
//            this.daoReceiptItems = daoReceiptItems;
//            this.daoVendor = daoVendor;
//            this.daoProduct = daoProduct;
//        }
//
//        @Override
//        public void run() {
//            try {
//                System.out.println("User " + userId + " mencoba untuk menginput penerimaan barang dengan tipe: " + taskType);
//
//                String receiptNumber = daoGoodsReceipts.generateReceiptNumber();
//                int vendorId = 2;
//                String vendorName = daoVendor.getVendorNameById(vendorId);
//
//                ModelGoodsReceipts goodsReceipt = new ModelGoodsReceipts.Builder()
//                        .receiptNumber(receiptNumber)
//                        .vendorId(vendorId)
//                        .vendorName(vendorName)
//                        .receiptDate(LocalDate.now())
//                        .build();
//
//                int receiptId = daoGoodsReceipts.insertGoodsReceipt(goodsReceipt);
//
//                int productId;
//                int quantity;
//
//                switch (taskType) {
//                    case ADD_ID_4_QTY_5:
//                        productId = 24;
//                        quantity = 2;
//                        break;
//                    case UPDATE_ID_6_QTY_5:
//                        productId = 24;
//                        quantity = 1;
//                        break;
//                    case ADD_ID_4_QTY_3:
//                        productId = 25;
//                        quantity = 2;
//                        break;
//                    case UPDATE_ID_6_QTY_6:
//                        productId = 25;
//                        quantity = 1;
//                        break;
//                    default:
//                        throw new IllegalArgumentException("Unknown task type: " + taskType);
//                }
//
//                ModelReceiptItems receiptItem = new ModelReceiptItems(receiptId, productId, quantity);
//                daoReceiptItems.insertReceiptItem(receiptItem);
//                daoProduct.updateProductStock(productId, quantity);
//
//                System.out.println("User " + userId + " selesai menginput penerimaan barang dan stok diperbarui dengan penambahan " + quantity + " unit untuk produk " + productId);
//            } catch (Exception e) {
//                System.err.println("Error terjadi pada user " + userId + ": " + e.getMessage());
//            }
//        }
//    }
//}


// NOW
//public class Main {
//    public static void main(String[] args) {
//        // Contoh instansiasi DaoTree dan DaoTreeRelations
//        DaoTree daoTree = new DaoTree();
//
//        // Buat koneksi ke database
//        ConnectionDb connectionDb = new ConnectionDb();
//        Connection conn = connectionDb.connect_to_db();
//
//        try {
//            // Mengatur isolasi transaksi ke SERIALIZABLE
//            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        // Inisialisasi DAO dengan koneksi database yang sama
//        DaoProduct daoProduct = new DaoProduct();
//        DaoVendor daoVendor = new DaoVendor();
//        DaoGoodsReceipts daoGoodsReceipts = new DaoGoodsReceipts();
//        DaoGoodsIssuance daoGoodsIssuance = new DaoGoodsIssuance();
//        DaoReceiptItems daoReceiptItems = new DaoReceiptItems();
//
//        // Menampilkan frame
//        AllFrames frame = new AllFrames(daoTree, daoProduct, daoVendor, daoGoodsReceipts, daoGoodsIssuance, conn);
//        frame.setVisible(true);
//
//        // Membuat pool thread dengan 50 thread untuk simulasi 50 user
//        ExecutorService executor = Executors.newFixedThreadPool(50);
//
//        // Jalankan thread untuk simulasi user secara acak
//        Random random = new Random();
//        for (int i = 0; i < 50; i++) {
//            TaskType taskType = random.nextBoolean() ? TaskType.ADD_STOCK : TaskType.ISSUE_PRODUCT;
//            executor.submit(new UserTask(i, taskType, daoGoodsIssuance, daoGoodsReceipts, daoReceiptItems, daoProduct, daoVendor));
//        }
//
//        // Mematikan executor setelah semua tugas selesai
//        executor.shutdown();
//    }
//
//    // Enum untuk menentukan jenis tugas
//    enum TaskType {
//        ISSUE_PRODUCT,    // Operasi pengeluaran barang
//        ADD_STOCK,        // Operasi penerimaan barang (penambahan stok)
//    }
//
//    // Kelas untuk mensimulasikan tugas user
//    static class UserTask implements Runnable {
//        private final int userId;
//        private final TaskType taskType;
//        private final DaoGoodsIssuance daoGoodsIssuance;
//        private final DaoGoodsReceipts daoGoodsReceipts;
//        private final DaoReceiptItems daoReceiptItems;
//        private final DaoProduct daoProduct;
//        private final DaoVendor daoVendor;
//        private final Random random = new Random();
//
//        public UserTask(int userId, TaskType taskType, DaoGoodsIssuance daoGoodsIssuance, DaoGoodsReceipts daoGoodsReceipts, DaoReceiptItems daoReceiptItems, DaoProduct daoProduct, DaoVendor daoVendor) {
//            this.userId = userId;
//            this.taskType = taskType;
//            this.daoGoodsIssuance = daoGoodsIssuance;
//            this.daoGoodsReceipts = daoGoodsReceipts;
//            this.daoReceiptItems = daoReceiptItems;
//            this.daoProduct = daoProduct;
//            this.daoVendor = daoVendor;
//        }
//
//        @Override
//        public void run() {
//            try {
//                // Mensimulasikan operasi berdasarkan tipe tugas
//                System.out.println("User " + userId + " mencoba melakukan operasi dengan tipe: " + taskType);
//
//                // Tambahkan delay acak untuk mensimulasikan beban sistem yang berbeda
//                int delay = random.nextInt(200); // Delay antara 0 hingga 200 ms
//                Thread.sleep(delay);
//
//                // Pilih operasi berdasarkan TaskType
//                synchronized (daoProduct) { // Memastikan satu operasi terjadi dalam satu waktu untuk mencegah race condition
//                    switch (taskType) {
//                        case ISSUE_PRODUCT:
//                            issueProduct();
//                            break;
//                        case ADD_STOCK:
//                            addStock();
//                            break;
//                    }
//                }
//
//                System.out.println("User " + userId + " selesai melakukan operasi dengan tipe: " + taskType);
//            } catch (Exception e) {
//                System.err.println("Error terjadi pada user " + userId + ": " + e.getMessage());
//            }
//        }
//
//        private void issueProduct() throws SQLException {
//            // Memeriksa stok sebelum pengeluaran barang
//            int productId = 24; // Contoh product ID
//            int quantity = random.nextInt(10) + 1; // Jumlah barang yang dikeluarkan (acak 1-10)
//
//            int currentStock = daoProduct.getCurrentStock(productId); // Dapatkan stok saat ini
//            if (currentStock >= quantity) {
//                // Mensimulasikan pengeluaran barang
//                ModelGoodsIssuance issuance = new ModelGoodsIssuance();
//                issuance.setIssuanceNumber(daoGoodsIssuance.generateIssuanceNumber());
//                issuance.setIssuanceDate(LocalDate.now());
//                issuance.setTotalPrice(BigDecimal.ZERO); // Inisialisasi harga total
//                issuance.setNote("Pengeluaran barang oleh user " + userId);
//
//                int issuanceId = daoGoodsIssuance.insertGoodsIssuance(issuance);
//
//                // Tambahkan item pengeluaran
//                ModelIssuanceItem item = new ModelIssuanceItem(issuanceId, productId, quantity, new BigDecimal("1000"), "testing1");
//                daoGoodsIssuance.insertIssuanceItem(item);
//
//                // Mengurangi stok barang setelah pengeluaran
//                daoProduct.decreaseProductStock(productId, quantity);
//            } else {
//                System.out.println("Error: Stok tidak cukup untuk produk dengan ID: " + productId + " untuk user " + userId);
//            }
//        }
//
//        private void addStock() throws SQLException {
//            // Mensimulasikan penerimaan barang (penambahan stok)
//            ModelGoodsReceipts receipt = new ModelGoodsReceipts();
//            receipt.setReceiptNumber(daoGoodsReceipts.generateReceiptNumber());
//            receipt.setReceiptDate(LocalDate.now());
//            receipt.setVendorId(2); // Contoh vendor ID
//            receipt.setNote("Penerimaan barang oleh user " + userId);
//
//            int receiptId = daoGoodsReceipts.insertGoodsReceipt(receipt);
//
//            // Tambahkan item penerimaan
//            int productId = 24; // Contoh product ID
//            int quantity = random.nextInt(20) + 1; // Jumlah barang yang diterima (acak 1-20)
//            ModelReceiptItems receiptItem = new ModelReceiptItems(receiptId, productId, quantity);
//            daoReceiptItems.insertReceiptItem(receiptItem);
//
//            // Menambahkan stok barang setelah penerimaan
//            daoProduct.updateProductStock(productId, quantity);
//        }
//    }
//}


