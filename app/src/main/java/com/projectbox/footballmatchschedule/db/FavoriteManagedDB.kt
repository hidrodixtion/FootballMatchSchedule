package com.projectbox.footballmatchschedule.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created by adinugroho
 */
class FavoriteManagedDB(context: Context) : ManagedSQLiteOpenHelper(context, "FavoriteEvent.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(FavoriteScheduleColumns.TABLE_NAME, true,
                FavoriteScheduleColumns.C_ID to TEXT + PRIMARY_KEY + UNIQUE,
                FavoriteScheduleColumns.C_HOME_ID to TEXT,
                FavoriteScheduleColumns.C_AWAY_ID to TEXT,
                FavoriteScheduleColumns.C_HOME_TEAM to TEXT,
                FavoriteScheduleColumns.C_AWAY_TEAM to TEXT,
                FavoriteScheduleColumns.C_HOME_SCORE to INTEGER,
                FavoriteScheduleColumns.C_AWAY_SCORE to INTEGER,
                FavoriteScheduleColumns.C_HOME_GOAL_DETAILS to TEXT,
                FavoriteScheduleColumns.C_AWAY_GOAL_DETAILS to TEXT,
                FavoriteScheduleColumns.C_HOME_SHOTS to INTEGER,
                FavoriteScheduleColumns.C_AWAY_SHOTS to INTEGER,
                FavoriteScheduleColumns.C_HOME_GK to TEXT,
                FavoriteScheduleColumns.C_HOME_DEF to TEXT,
                FavoriteScheduleColumns.C_HOME_MID to TEXT,
                FavoriteScheduleColumns.C_HOME_FW to TEXT,
                FavoriteScheduleColumns.C_AWAY_GK to TEXT,
                FavoriteScheduleColumns.C_AWAY_DEF to TEXT,
                FavoriteScheduleColumns.C_AWAY_MID to TEXT,
                FavoriteScheduleColumns.C_AWAY_FW to TEXT,
                FavoriteScheduleColumns.C_DATE to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.dropTable(FavoriteScheduleColumns.TABLE_NAME, true)
    }
}

object FavoriteScheduleColumns {
    const val TABLE_NAME = "table_schedule"

    const val C_ID = "schedule_id"
    const val C_HOME_ID = "home_id"
    const val C_AWAY_ID = "away_id"
    const val C_HOME_TEAM = "home_team"
    const val C_AWAY_TEAM = "away_team"
    const val C_HOME_SCORE = "home_score"
    const val C_AWAY_SCORE = "away_score"
    const val C_DATE = "date"
    const val C_HOME_GOAL_DETAILS = "home_goal_details"
    const val C_AWAY_GOAL_DETAILS = "away_goal_details"
    const val C_HOME_SHOTS = "home_shots"
    const val C_AWAY_SHOTS = "away_shots"
    const val C_HOME_GK = "home_gk"
    const val C_HOME_DEF = "home_def"
    const val C_HOME_MID = "home_mid"
    const val C_HOME_FW = "home_fw"
    const val C_AWAY_GK = "away_gk"
    const val C_AWAY_DEF = "away_def"
    const val C_AWAY_MID = "away_mid"
    const val C_AWAY_FW = "away_fw"
}