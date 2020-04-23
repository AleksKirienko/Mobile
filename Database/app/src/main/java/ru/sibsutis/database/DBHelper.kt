package ru.sibsutis.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "SQL"
val TABLENAME = "Student"
val COL_ID = "_id"
val COL_NAME = "name"
val COL_AGE = "age"
val COL_WEIGHT = "weight"
val COL_HEIGHT = "height"

class DBHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLENAME (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_AGE + " INTEGER, " +
                COL_WEIGHT + " INTEGER, " +
                COL_HEIGHT + " INTEGER);"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun insertData(user: User) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, user.name)
        cv.put(COL_AGE, user.age)
        cv.put(COL_WEIGHT, user.weight)
        cv.put(COL_HEIGHT, user.height)
        db.insert(TABLENAME, null, cv)

//        if (res == (-1).toLong())
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
//        else Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()

        db.close()
    }

    fun readData(): ArrayList<User> {
        val list = ArrayList<User>()
        val db = this.readableDatabase
        val res = db.rawQuery("SELECT * FROM $TABLENAME", null)
        var user: User
        if (res.moveToFirst()) {
            do {
                user = User()
                user.id = res.getInt(res.getColumnIndex(COL_ID))
                user.name = res.getString(res.getColumnIndex(COL_NAME))
                user.age = res.getInt(res.getColumnIndex(COL_AGE))
                user.weight = res.getInt(res.getColumnIndex(COL_WEIGHT))
                user.height = res.getInt(res.getColumnIndex(COL_HEIGHT))
                list.add(user)

            } while (res.moveToNext())
        }
        res.close()
        db.close()
        return list
    }

    fun updateData() {
        val db = this.writableDatabase
        val res = db.rawQuery("SELECT * FROM $TABLENAME", null)
        if (res.moveToFirst()) {
            do {
                var cv = ContentValues()
                cv.put(COL_AGE, (res.getInt(res.getColumnIndex(COL_AGE)) + 1))
                db.update(
                    TABLENAME, cv,
                    COL_ID + "=? AND " + COL_NAME + "=? AND " + COL_WEIGHT + "=? AND " + COL_HEIGHT + "=?",
                    arrayOf(
                        res.getString(res.getColumnIndex(COL_ID)),
                        res.getString(res.getColumnIndex(COL_NAME)),
                        res.getString(res.getColumnIndex(COL_WEIGHT)),
                        res.getString(res.getColumnIndex(COL_HEIGHT))
                    )
                )
            } while (res.moveToNext())
        }
        res.close()
        db.close()
    }

    fun sort(): ArrayList<User> {
        val students = ArrayList<User>()
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM $TABLENAME ORDER BY $COL_AGE", null)
        var user: User
        if (c.moveToFirst()) {
            do {
                user = User()
                user.id = c.getInt(c.getColumnIndex(COL_ID))
                user.name = c.getString(c.getColumnIndex(COL_NAME))
                user.age = c.getInt(c.getColumnIndex(COL_AGE))
                user.weight = c.getInt(c.getColumnIndex(COL_WEIGHT))
                user.height = c.getInt(c.getColumnIndex(COL_HEIGHT))
                students.add(user)

            } while (c.moveToNext())
        }
        c.close()
        db.close()
        return students
    }

    fun deleteDB() {
        val db = this.writableDatabase
        db.delete(TABLENAME, null, null)
        db.close()
    }

}