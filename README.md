## **📄 Smart Expense Tracker**

### **1. Project Overview**

**Smart Expense Tracker** is a Jetpack Compose + MVVM based Android app to track daily expenses, view reports, and export data as CSV.
The project follows **AI-first development** where AI tools like ChatGPT, Cursor, and Gemini were used for design, code, and documentation assistance.

---

### **2. Features**

| Feature                 | Description                                                                      |
| ----------------------- | -------------------------------------------------------------------------------- |
| **Expense Entry**       | Add title, amount, category, notes with validation (title non-empty, amount > 0) |
| **Duplicate Detection** | Blocks same title + amount + date                                                |
| **Expense List**        | Shows today's expenses with total amount & count, date filter, group toggle      |
| **Empty State**         | Message when no expenses are present                                             |
| **Report Screen**       | Last 7-day totals, category-wise breakdown, mock chart                           |
| **CSV Export**          | Stores CSV in internal storage, snackbar confirmation                            |
| **Persistence**         | Room database for storing expenses                                               |
| **Material Design 3**   | Modern Compose UI with theming                                                   |

---

### **3. Project Structure**

```
com.example.smartexpensetracker/
│
├── data/
│   ├── model/Expense.kt
│   ├── repo/ExpenseRepository.kt
│   └── db/ExpenseDao.kt
│
├── ui/
│   ├── expense/ExpenseEntryScreen.kt
│   ├── list/ExpenseListScreen.kt
│   └── report/ExpenseReportScreen.kt
│
├── viewmodel/
│   ├── ExpenseEntryViewModel.kt
│   ├── ExpenseListViewModel.kt
│   └── ExpenseReportViewModel.kt

```

---

### **4. Tech Stack**

* **Language**: Kotlin
* **UI**: Jetpack Compose (Material 3)
* **Architecture**: MVVM
* **Async**: Kotlin Coroutines + Flow
* **Charts**: Mocked Compose
* **Export**: CSV (internal storage)

---

### **5. AI Usage**

#### **AI Tools Used**

* **ChatGPT**:

  * UI layout ideas (Compose UI)
  * MVVM structure & repository pattern setup
  * ViewModel/Data class generation
  * UX feedback and enhancements
  * Validation + duplicate detection logic
  * Code comments & README creation
* **Cursor/Copilot**:

  * Compose boilerplate generation
  * DAO and repository quick methods
* **Gemini**:

  * Alternate UI & UX suggestions

---

#### **AI Contribution Table**

| Feature / Task                        | Done by AI | Done Manually |
| ------------------------------------- | ---------- | ------------- |
| UI layout ideas                       | ✅          |               |
| MVVM structuring                      | ✅          |               |
| ViewModel/Data class generation       | ✅          |               |
| UX feedback & enhancements            | ✅          |               |
| Prompt tuning & retries               | ✅          |               |
| Code comments                         | ✅          |               |
| README help                           | ✅          |               |
| CSV export logic                      | ✅          |               |
| Room DB setup                         |            | ✅             |
| Testing & debugging                   |            | ✅             |
| Final validation rules                |            | ✅             |
| Duplicate detection logic integration |            | ✅             |
| Date filter UI                        |            | ✅             |

---

### **6. How to Run**

1. Clone the repository:

   ```bash
   git clone https://github.com/farzansiddiqui/ExpenseTracker.git
   ```
2. Open in **Android Studio (Giraffe or newer)**
3. Run Gradle Sync
4. Launch app on emulator/physical device (API 24+)

---

### **7. Notes**

* CSV is saved in internal storage (no permission required)
* Chart data is mocked for assignment purposes

---

### **8. License**

Educational use only — AI-first Android Developer Assignment.

---
