/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.dsa_motorphinventorysystem;

import java.io.*;
import java.util.*;
 
//========================================================
// CLASS: Motorcycle
// Represents a single inventory item
//========================================================
class Motorcycle {
 
    private int    id;
    private String brand;
    private String model;
    private int    quantity;
    private double price;
 
    public Motorcycle(int id, String brand, String model, int quantity, double price) {
        this.id       = id;
        this.brand    = brand;
        this.model    = model;
        this.quantity = quantity;
        this.price    = price;
    }
 
    // Getters
    public int    getId()       { return id; }
    public String getBrand()    { return brand; }
    public String getModel()    { return model; }
    public int    getQuantity() { return quantity; }
    public double getPrice()    { return price; }
 
    // Setter for quantity (used in transactions)
    public void setQuantity(int quantity) { this.quantity = quantity; }
 
    @Override
    public String toString() {
        return String.format("ID: %d | Brand: %s | Model: %s | Qty: %d | PHP %,.0f",
                id, brand, model, quantity, price);
    }
}
 
//========================================================
// CLASS: BrandNode
// Represents one node in the Binary Search Tree
// Groups all motorcycles of the same brand
//========================================================
class BrandNode {
 
    private String           brand;
    private List<Motorcycle> motorcycles;
    BrandNode left, right;
 
    public BrandNode(String brand) {
        this.brand       = brand;
        this.motorcycles = new ArrayList<>();
    }
 
    public String           getBrand()       { return brand; }
    public List<Motorcycle> getMotorcycles() { return motorcycles; }
 
    public void addMotorcycle(Motorcycle m) {
        motorcycles.add(m);
    }
}
 
//========================================================
// CLASS: BrandBST
// Binary Search Tree sorted by Motor Brand
// Uses recursion for insertion and traversal
//========================================================
class BrandBST {
 
    // Recursive insertion by brand name
    public BrandNode insert(BrandNode root, Motorcycle bike) {
        if (root == null) {
            BrandNode node = new BrandNode(bike.getBrand());
            node.addMotorcycle(bike);
            return node;
        }
 
        int cmp = bike.getBrand().compareToIgnoreCase(root.getBrand());
 
        if (cmp < 0) {
            root.left = insert(root.left, bike);
        } else if (cmp > 0) {
            root.right = insert(root.right, bike);
        } else {
            root.addMotorcycle(bike); // Same brand — group in same node
        }
 
        return root;
    }
 
    // Recursive in-order traversal → alphabetical brand list
    public void inOrder(BrandNode node) {
        if (node == null) return;
        inOrder(node.left);
        System.out.println("Brand: " + node.getBrand());
        for (Motorcycle m : node.getMotorcycles()) {
            System.out.println("  - " + m.getModel()
                    + " (" + m.getQuantity() + " in stock, PHP "
                    + String.format("%,.0f", m.getPrice()) + ")");
        }
        inOrder(node.right);
    }
 
    // Flatten BST into a flat list (bridge to Merge Sort)
    public void flatten(BrandNode node, List<Motorcycle> list) {
        if (node == null) return;
        flatten(node.left, list);
        list.addAll(node.getMotorcycles());
        flatten(node.right, list);
    }
}
 
//========================================================
// CLASS: MergeSort
// Sorts a flat list of motorcycles by brand, then model
// Uses divide-and-conquer recursion — O(n log n)
//========================================================
class MergeSort {
 
    public List<Motorcycle> sort(List<Motorcycle> list) {
        if (list.size() <= 1) return list;
 
        int mid = list.size() / 2;
        List<Motorcycle> left  = sort(new ArrayList<>(list.subList(0, mid)));
        List<Motorcycle> right = sort(new ArrayList<>(list.subList(mid, list.size())));
 
        return merge(left, right);
    }
 
    private List<Motorcycle> merge(List<Motorcycle> left, List<Motorcycle> right) {
        List<Motorcycle> result = new ArrayList<>();
        int i = 0, j = 0;
 
        while (i < left.size() && j < right.size()) {
            int brandCmp = left.get(i).getBrand().compareToIgnoreCase(right.get(j).getBrand());
            if (brandCmp < 0) {
                result.add(left.get(i++));
            } else if (brandCmp > 0) {
                result.add(right.get(j++));
            } else {
                int modelCmp = left.get(i).getModel().compareToIgnoreCase(right.get(j).getModel());
                if (modelCmp <= 0) {
                    result.add(left.get(i++));
                } else {
                    result.add(right.get(j++));
                }
            }
        }
 
        while (i < left.size())  result.add(left.get(i++));
        while (j < right.size()) result.add(right.get(j++));
 
        return result;
    }
}
 
//========================================================
// CLASS: Transaction
// Represents one stock change event (sell or restock)
// Used by the Queue (FIFO)
//========================================================
class Transaction {
 
    private int id;
    private int change;
 
    public Transaction(int id, int change) {
        this.id     = id;
        this.change = change;
    }
 
    public int getId()     { return id; }
    public int getChange() { return change; }
}
 
//========================================================
// CLASS: UndoRecord
// Stores the reverse of a transaction
// Used by the Stack (LIFO)
//========================================================
class UndoRecord {
 
    private int id;
    private int undoAmount;
 
    public UndoRecord(int id, int undoAmount) {
        this.id         = id;
        this.undoAmount = undoAmount;
    }
 
    public int getId()         { return id; }
    public int getUndoAmount() { return undoAmount; }
}
 
//========================================================
// CLASS: Inventory
// Core system object — owns all data structures
// and exposes all operations as methods
//========================================================
class Inventory {
 
    // ── Data Structures ────────────────────────────────
    private HashMap<Integer, Motorcycle> hashMap_inventory; // Key: ID → O(1) lookup
    private BrandBST                     bst;               // Sorted by brand name
    private BrandNode                    bstRoot;           // BST root node
    private MergeSort                    mergeSort;         // For report generation
    private Queue<Transaction>           transactionQueue;  // FIFO — stock in/out
    private Stack<UndoRecord>            undoStack;         // LIFO — undo operations
 
    private static final int DEFAULT_QUANTITY = 10;
 
    // ── Constructor — initializes all data structures ──
    public Inventory() {
        hashMap_inventory = new HashMap<>();
        bst               = new BrandBST();
        bstRoot           = null;
        mergeSort         = new MergeSort();
        transactionQueue  = new LinkedList<>();
        undoStack         = new Stack<>();
    }
 
    // ── Add one motorcycle to HashMap and BST ──────────
    public void addMotorcycle(Motorcycle bike) {
        hashMap_inventory.put(bike.getId(), bike);
        bstRoot = bst.insert(bstRoot, bike);
    }
 
    // ── Load from MotorPH CSV ──────────────────────────
    // Format: EntrNo, EntrName, EntrDetails, ManufDate, Acquisition, UnitPrice
    // EntrName: "Brand Model" e.g. "Honda CBR500R"
    public boolean loadFromCSV(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            return false;
        }
 
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
 
            String line;
            boolean isHeader = true;
 
            while ((line = br.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }
                if (line.trim().isEmpty()) continue;
 
                String[] fields = parseCSVLine(line);
                if (fields.length < 6) continue;
 
                int    id       = Integer.parseInt(fields[0].trim());
                String fullName = fields[1].trim();
                double price    = Double.parseDouble(fields[5].trim());
                int    quantity = DEFAULT_QUANTITY;
 
                // Split "Honda CBR500R" → brand="Honda", model="CBR500R"
                int spaceIdx = fullName.indexOf(' ');
                String brand = (spaceIdx > 0) ? fullName.substring(0, spaceIdx) : fullName;
                String model = (spaceIdx > 0) ? fullName.substring(spaceIdx + 1) : fullName;
 
                addMotorcycle(new Motorcycle(id, brand, model, quantity, price));
            }
 
            System.out.println("✓ Loaded from: " + filename);
            return true;
 
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading CSV: " + e.getMessage());
            return false;
        }
    }
 
    // ── Parse CSV line (handles quoted fields with commas)
    private String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
 
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }
 
    // ── Load fallback sample data ──────────────────────
    public void loadSampleData() {
        Motorcycle[] items = {
            new Motorcycle(101, "Harley",   "Street Glide",  8, 21999),
            new Motorcycle(102, "Yamaha",   "R1",            6, 17999),
            new Motorcycle(103, "Honda",    "CBR600",       12, 11999),
            new Motorcycle(104, "Kawasaki", "Ninja",         7, 16999),
            new Motorcycle(105, "BMW",      "S1000RR",       4, 18999),
            new Motorcycle(106, "Honda",    "Gold Wing",     3, 23999),
            new Motorcycle(107, "Yamaha",   "MT-09",         5,  9999)
        };
        for (Motorcycle m : items) addMotorcycle(m);
    }
 
    // ── Queue a transaction ────────────────────────────
    public void queueTransaction(int id, int change) {
        transactionQueue.add(new Transaction(id, change));
        System.out.println("→ Queued: ID " + id + " | change " + change);
    }
 
    // ── Process all queued transactions (FIFO) ─────────
    public void processTransactions() {
        int count = 0;
        while (!transactionQueue.isEmpty()) {
            Transaction t = transactionQueue.poll();
            count++;
            System.out.println("Transaction " + count + ":");
 
            if (hashMap_inventory.containsKey(t.getId())) {
                Motorcycle bike   = hashMap_inventory.get(t.getId());
                int        oldQty = bike.getQuantity();
 
                if (t.getChange() < 0 && bike.getQuantity() < Math.abs(t.getChange())) {
                    System.out.println("  ✗ ERROR: Insufficient stock for "
                            + bike.getBrand() + " " + bike.getModel());
                    System.out.println("    Available: " + bike.getQuantity()
                            + ", Requested: " + Math.abs(t.getChange()));
                    continue;
                }
 
                bike.setQuantity(bike.getQuantity() + t.getChange());
                hashMap_inventory.put(bike.getId(), bike);
                undoStack.push(new UndoRecord(bike.getId(), -t.getChange()));
 
                System.out.println("  ✓ Updated: " + bike.getBrand() + " " + bike.getModel());
                System.out.println("    Stock: " + oldQty + " → " + bike.getQuantity());
            } else {
                System.out.println("  ✗ ERROR: Motorcycle ID " + t.getId() + " not found.");
            }
        }
        System.out.println("-------------------------");
        System.out.println("Processed " + count + " transactions.");
    }
 
    // ── Search by ID using HashMap — O(1) ──────────────
    public void searchByID(Scanner sc) {
        System.out.print("Enter Motorcycle ID: ");
        int id = sc.nextInt();
 
        if (hashMap_inventory.containsKey(id)) {
            Motorcycle m = hashMap_inventory.get(id);
            System.out.println("\n=== SEARCH RESULT (ID: " + id + ") ===");
            System.out.println("  ✓ FOUND:");
            System.out.println("    ID:       " + m.getId());
            System.out.println("    Brand:    " + m.getBrand());
            System.out.println("    Model:    " + m.getModel());
            System.out.println("    Quantity: " + m.getQuantity());
            System.out.println("    Price:    PHP " + String.format("%,.0f", m.getPrice()));
            System.out.println("==================================");
        } else {
            System.out.println("  ✗ No motorcycle found with ID: " + id);
        }
    }
 
    // ── View all brands via BST in-order traversal ─────
    public void viewAllBrands() {
        System.out.println("\n=== ALL BRANDS (Alphabetical via BST In-Order) ===");
        bst.inOrder(bstRoot);
        System.out.println("===================================================");
    }
 
    // ── Generate report using Merge Sort ───────────────
    public void generateReport() {
        System.out.println("\n==========================================================");
        System.out.println("              MOTORPH INVENTORY REPORT");
        System.out.println("       Sorted by Brand & Model using Merge Sort");
        System.out.println("==========================================================");
        System.out.printf("%-5s %-12s %-22s %-6s %s%n",
                "ID", "Brand", "Model", "Qty", "Price (PHP)");
        System.out.println("----------------------------------------------------------");
 
        List<Motorcycle> flatList = new ArrayList<>();
        bst.flatten(bstRoot, flatList);
        List<Motorcycle> sorted = mergeSort.sort(flatList);
 
        double totalValue = 0;
        int    totalUnits = 0;
 
        for (Motorcycle m : sorted) {
            System.out.printf("%-5d %-12s %-22s %-6d %,.0f%n",
                    m.getId(), m.getBrand(), m.getModel(), m.getQuantity(), m.getPrice());
            totalValue += m.getQuantity() * m.getPrice();
            totalUnits += m.getQuantity();
        }
 
        Set<String> brands = new HashSet<>();
        for (Motorcycle m : sorted) brands.add(m.getBrand());
 
        System.out.println("==========================================================");
        System.out.println("SUMMARY:");
        System.out.println("  Total Items:   " + sorted.size());
        System.out.println("  Total Units:   " + totalUnits);
        System.out.printf("  Total Value:   PHP %,.0f%n", totalValue);
        System.out.println("  Unique Brands: " + brands.size() + " " + brands);
        System.out.println("==========================================================");
    }
 
    // ── Undo last transaction using Stack — LIFO ───────
    public void undoLastTransaction() {
        System.out.println("\n=== UNDO OPERATION ===");
 
        if (undoStack.isEmpty()) {
            System.out.println("  No transactions to undo.");
            System.out.println("======================");
            return;
        }
 
        UndoRecord last = undoStack.pop();
 
        if (hashMap_inventory.containsKey(last.getId())) {
            Motorcycle bike   = hashMap_inventory.get(last.getId());
            int        oldQty = bike.getQuantity();
            bike.setQuantity(bike.getQuantity() + last.getUndoAmount());
            hashMap_inventory.put(bike.getId(), bike);
 
            System.out.println("  ✓ Undid last transaction for "
                    + bike.getBrand() + " " + bike.getModel());
            System.out.println("    Stock: " + oldQty + " → " + bike.getQuantity());
        } else {
            System.out.println("  ✗ Error: Motorcycle not found for undo.");
        }
        System.out.println("======================");
    }
 
    // ── Rebuild BST from HashMap ───────────────────────
    public void rebuildBST() {
        System.out.println("\nRebuilding BST with updated quantities...");
        bstRoot = null;
        for (Motorcycle bike : hashMap_inventory.values()) {
            bstRoot = bst.insert(bstRoot, bike);
        }
        System.out.println("✓ BST rebuilt with latest quantities.");
    }
 
    // ── Save inventory to CSV ──────────────────────────
    public void saveToCSV() {
        try (FileWriter fw = new FileWriter("motorcycle_inventory.csv")) {
            fw.write("ID,BRAND,MODEL,QUANTITY,PRICE\n");
            for (Motorcycle m : hashMap_inventory.values()) {
                fw.write(m.getId() + "," + m.getBrand() + "," + m.getModel() + ","
                        + m.getQuantity() + "," + m.getPrice() + "\n");
            }
            System.out.println("✓ Inventory saved to motorcycle_inventory.csv ("
                    + hashMap_inventory.size() + " records)");
        } catch (IOException e) {
            System.out.println("Error saving CSV: " + e.getMessage());
        }
    }
 
    // ── Utility ────────────────────────────────────────
    public int size() {
        return hashMap_inventory.size();
    }
 
    public List<Integer> getSortedIDs() {
        List<Integer> ids = new ArrayList<>(hashMap_inventory.keySet());
        Collections.sort(ids);
        return ids;
    }
}
 
//========================================================
// CLASS: DSA_MotorPHInventorySystem
// Entry point — creates Inventory object and runs menu
//========================================================
public class DSA_MotorPHInventorySystem {
 
    public static void main(String[] args) throws UnsupportedEncodingException {
 
        // Fix UTF-8 so box-drawing characters render correctly
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
 
        // ── Create the Inventory object ─────────────────
        Inventory inventory = new Inventory();
 
        // ── Step 1: Load from MotorPH CSV ──────────────
        boolean loaded = inventory.loadFromCSV("MotorPH_Products_List_2025.csv");
 
        // ── Step 2: Fall back to sample data if needed ──
        if (!loaded || inventory.size() == 0) {
            System.out.println("CSV not found. Loading sample data...");
            inventory.loadSampleData();
        }
 
        System.out.println("✓ Loaded " + inventory.size() + " motorcycles into inventory.");
 
        // ── Step 3: Queue sample transactions ───────────
        List<Integer> ids = inventory.getSortedIDs();
        if (ids.size() >= 4) {
            inventory.queueTransaction(ids.get(0), -2);
            inventory.queueTransaction(ids.get(1), -1);
            inventory.queueTransaction(ids.get(2), +5);
            inventory.queueTransaction(ids.get(3), -1);
        }
 
        // ── Step 4: Process transactions ────────────────
        System.out.println("\nProcessing transactions...");
        inventory.processTransactions();
 
        // ── Step 5: Launch menu ─────────────────────────
        Scanner sc     = new Scanner(System.in);
        int     choice = 0;
 
         while (choice != 7) {
            
            System.out.println(  "\nMOTORPH INVENTORY SYSTEM         ");
      
            System.out.println(  "1. Search by ID     (HashMap)      ");
            System.out.println(  "2. View All Brands  (BST In-Order) ");
            System.out.println(  "3. Generate Report  (Merge Sort)   ");
            System.out.println(  "4. Undo Transaction (Stack)        ");
            System.out.println(  "5. Rebuild BST from HashMap        ");
            System.out.println(  "6. Save to CSV                     ");
            System.out.println(  "7. Exit                            ");
            System.out.print("Enter choice: ");
 
            choice = sc.nextInt();
 
            switch (choice) {
                case 1 -> inventory.searchByID(sc);
                case 2 -> inventory.viewAllBrands();
                case 3 -> inventory.generateReport();
                case 4 -> inventory.undoLastTransaction();
                case 5 -> inventory.rebuildBST();
                case 6 -> inventory.saveToCSV();
                case 7 -> {
                    inventory.saveToCSV();
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
 
        sc.close();
    }
}