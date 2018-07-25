package com.projectbox.footballmatchschedule.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created by adinugroho
 */
class TeamManagedDB(context: Context) : ManagedSQLiteOpenHelper(context, "Team.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TeamColumns.TABLE_NAME, true,
                TeamColumns.C_ID to TEXT + PRIMARY_KEY + UNIQUE,
                TeamColumns.C_NAME to TEXT,
                TeamColumns.C_BADGE to TEXT,
                TeamColumns.C_LEAGUE to TEXT,
                TeamColumns.C_DESC to TEXT,
                TeamColumns.C_YEAR to TEXT,
                TeamColumns.C_FAV to INTEGER
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.dropTable(TeamColumns.TABLE_NAME, true)
    }
}

object TeamColumns {
    const val TABLE_NAME = "table_team"

    const val C_ID = "team_id"
    const val C_NAME = "team_name"
    const val C_BADGE = "team_badge"
    const val C_LEAGUE = "league_id"
    const val C_DESC = "team_description"
    const val C_YEAR = "team_formed_year"
    const val C_FAV = "is_favorited"
}