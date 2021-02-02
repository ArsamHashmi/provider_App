package com.axiomindustries.provider002.database;

public class ContractNotification {

    public static final String TABLE_NAME = "notifications";
    public static final String COLUMN_NOTIFICATION_TIME = "timestamp";
    public static final String COLUMN_NOTIFICATION_TICKER = "ticker";
    public static final String COLUMN_NOTIFICATION_EXTRA = "extra";
    public static final String COLUMN_NOTIFICATION_PACKAGE = "package";
    public static final String COLUMN_NOTIFICATION_ICON = "icon";
    public static final String DATABASE_NAME = "mydb";
    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_DB_TABLE =
            " CREATE TABLE "
            + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOTIFICATION_TIME +" TEXT NOT NULL, "
            + COLUMN_NOTIFICATION_TICKER +" TEXT NOT NULL, "
            + COLUMN_NOTIFICATION_EXTRA +" TEXT NOT NULL, "
            + COLUMN_NOTIFICATION_ICON +" BLOB, "
            + COLUMN_NOTIFICATION_PACKAGE +" TEXT NOT NULL);";
}
