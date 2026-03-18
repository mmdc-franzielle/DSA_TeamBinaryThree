# 🏍️ MotorPH Inventory System
### DSA_TeamBinaryThree

> A data structures & algorithms implementation of a motorcycle inventory management system using HashMap, BST, Queue, and Stack.

---

## 👥 Team Members

| Name | Role |
|------|------|
| Franzie Frielle Mangalindan | Team Member |
| Bernice Mariano Cunanan | Team Member |
| Joshua Al Hosani Sarmiento | Team Member |

---

## 📋 Project Overview

This repository contains the **pseudocode and sample implementation** for a motorcycle inventory system designed for **MotorPH**. The project demonstrates the practical application of core data structures in a real-world inventory management scenario.

The system manages a fleet of motorcycles across multiple brands, supports transaction processing with undo capabilities, and generates sorted inventory reports — all powered by efficient data structure choices.

---

## 🧠 Data Structures Used

| Structure | Usage | Time Complexity |
|-----------|-------|-----------------|
| **HashMap** | Store and look up motorcycles by ID | O(1) average |
| **Binary Search Tree (BST)** | Store and display motorcycles sorted by brand | O(log n) average |
| **Queue** | Process inventory transactions in FIFO order | O(1) enqueue/dequeue |
| **Stack** | Undo last transaction in LIFO order | O(1) push/pop |

---

## ✨ Features

- **Add Inventory** — Load motorcycles with ID, brand, model, quantity, and price
- **Search by ID** — O(1) lookup using HashMap
- **Browse by Brand** — In-order BST traversal displays brands alphabetically
- **Transaction Queue** — Queue and process sales/restocks in order
- **Undo Last Transaction** — Reverse the most recent inventory change using a stack
- **Full Inventory Report** — Sorted report with total units and total value
- **CSV Export/Import** — Save and load inventory data from a `.csv` file
- **Rebuild BST** — Sync BST with current HashMap state after updates

---

## 🗂️ Repository Structure

```
DSA_TeamBinaryThree/
│
├── pseudocode/
│   └── motorcycle_inventory.pseudo    # Full pseudocode implementation
│
├── sample_data/
│   └── motorcycle_inventory.csv       # Sample CSV inventory file
│
└── README.md
```

---

## 🏍️ Sample Inventory Data

| ID  | Brand    | Model       | Qty | Price     |
|-----|----------|-------------|-----|-----------|
| 101 | Harley   | Street Glide | 8  | $21,999   |
| 102 | Yamaha   | R1           | 6  | $17,999   |
| 103 | Honda    | CBR600       | 12 | $11,999   |
| 104 | Kawasaki | Ninja        | 7  | $16,999   |
| 105 | BMW      | S1000RR      | 4  | $18,999   |
| 106 | Honda    | Gold Wing    | 3  | $23,999   |
| 107 | Yamaha   | MT-09        | 5  | $9,999    |

---

## 🔁 System Flow

```
Load CSV / Sample Data
        ↓
  Populate HashMap ──────────────────► O(1) Search by ID
        ↓
    Build BST ─────────────────────► Alphabetical Brand Display
        ↓
  Queue Transactions ─────────────► FIFO Processing
        ↓
  Process & Update ───────────────► Stack stores Undo Records
        ↓
  Generate Report / Save CSV
```

---

## 🌳 BST Structure (by Brand)

```
         Honda
        /     \
     Harley   Kawasaki
     /              \
   BMW             Yamaha
```

BST in-order traversal produces brands in alphabetical order:
**BMW → Harley → Honda → Kawasaki → Yamaha**

---

## 📌 Menu Options

```
╔════════════════════════════════════╗
║     MOTORPH INVENTORY SYSTEM       ║
╠════════════════════════════════════╣
║ 1. Search by ID (HashMap)          ║
║ 2. View All Brands (BST In-Order)  ║
║ 3. Generate Full Report            ║
║ 4. Undo Last Transaction           ║
║ 5. Rebuild BST from HashMap        ║
║ 6. Save to CSV                     ║
║ 7. Exit                            ║
╚════════════════════════════════════╝
```

---

## 🎓 Course Information

This project was created as part of a **Data Structures and Algorithms** course requirement.

**Team:** Binary Three
**Repository:** `DSA_TeamBinaryThree`

---

*© Team Binary Three — Franzie Frielle Mangalindan, Bernice Mariano Cunanan & Joshua Al Hosani Sarmiento*
