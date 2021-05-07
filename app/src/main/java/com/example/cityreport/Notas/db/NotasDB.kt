package com.example.cityreport.Notas.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cityreport.Notas.dao.NotaDao
import com.example.cityreport.Notas.entities.Nota
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Nota::class), version = 1, exportSchema = false)
public abstract class NotasDB : RoomDatabase() {

    abstract fun NotaDao(): NotaDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var NotaDao = database.NotaDao()

                    // Delete all content here.
                    //NotaDao.deleteAll()

                    // Add exemplos de notas
                    var nota = Nota(1, "Rua apertada na Nacional 13", "Não passar por lá com trânsito!")
                    NotaDao.insert(nota)

                    nota = Nota(2, "Rua central de Viana", "Tampa saneamento alta, cuidado para não partir o cárter!" )
                    NotaDao.insert(nota)

                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NotasDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NotasDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotasDB::class.java,
                    "notas_database"
                )
                    //estratégia de destruição
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}