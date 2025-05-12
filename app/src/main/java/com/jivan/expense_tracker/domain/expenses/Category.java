package com.jivan.expense_tracker.domain.expenses;

//        | id | name         | group      | is_custom |
//        | -- | ------------ | ---------- | ---------- |
//        | 1  | Rent         | Essentials | false      |
//        | 2  | Dining Out   | Lifestyle  | false      |
//        | 3  | Guitar Class | Personal   | true       |

public class Category {
    private int id;
    private String name;           // e.g., "Groceries"
    private String group;          // e.g., "Essentials"
    private boolean isCustom;      // true if user-created

    // Constructors, getters, setters

    // Default constructor
    public Category(){

    }

    public Category(int id, String name, String group, boolean isCustom) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.isCustom = isCustom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }
}

