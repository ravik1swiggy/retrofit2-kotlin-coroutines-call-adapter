package com.melegy.retrofitcoroutines.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.melegy.retrofitcoroutines.room.ID
import com.melegy.retrofitcoroutines.room.Quote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateQuote(quote: Quote)

    @Query("SELECT * from quote where id = $ID")
    fun getQuote(): Flow<Quote>
}
