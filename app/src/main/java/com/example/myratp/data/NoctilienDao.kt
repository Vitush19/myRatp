package com.example.myratp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myratp.model.Noctilien

@Dao
interface NoctilienDao {
    @Query("select * from allnoctilien")
    suspend fun getNoctilien(): List<Noctilien>

    @Insert
    suspend fun addNoctilien(noctilien: Noctilien)

    @Delete
    suspend fun deleteNoctilien(noctilien: Noctilien)

    @Query("delete from allnoctilien")
    suspend fun deleteAllNoctilien()

    @Query("select * from allnoctilien where id = :id")
    suspend fun getNoctilien(id: Int): Noctilien

    @Query("select * from allnoctilien where code = :code")
    suspend fun getNoctilienByCode(code: String): Noctilien
}