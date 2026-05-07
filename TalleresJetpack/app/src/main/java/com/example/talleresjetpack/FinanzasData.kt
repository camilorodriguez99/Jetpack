package com.example.talleresjetpack

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Entity(tableName = "transacciones")
data class TransaccionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val tipo: String
)

@Dao
interface TransaccionDao {
    @Query("SELECT * FROM transacciones ORDER BY id DESC")
    fun getAll(): Flow<List<TransaccionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(t: TransaccionEntity)

    @Delete
    suspend fun eliminar(t: TransaccionEntity)
}

@Database(entities = [TransaccionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transaccionDao(): TransaccionDao
}

class TransaccionRepository @Inject constructor(private val dao: TransaccionDao) {
    fun getTransacciones() = dao.getAll()
    suspend fun agregar(t: TransaccionEntity) = dao.insertar(t)
    suspend fun borrar(t: TransaccionEntity) = dao.eliminar(t)
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "finanzas_db").build()
    }

    @Provides
    fun provideDao(db: AppDatabase): TransaccionDao {
        return db.transaccionDao()
    }
}

@HiltViewModel
class FinanzasViewModel @Inject constructor(
    private val repo: TransaccionRepository
) : ViewModel() {
    val transacciones = repo.getTransacciones().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun agregar(descripcion: String, monto: Double, tipo: String) {
        viewModelScope.launch {
            repo.agregar(TransaccionEntity(descripcion = descripcion, monto = monto, tipo = tipo))
        }
    }

    fun borrar(t: TransaccionEntity) {
        viewModelScope.launch {
            repo.borrar(t)
        }
    }
}