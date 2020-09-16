package com.melegy.retrofitcoroutines.room.dao

import androidx.room.*
import com.melegy.retrofitcoroutines.room.Quote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao : BaseDao<Quote> {

	@Query("SELECT * from quote where id = :key")
	fun getQuote(key: Int): Flow<Quote>
}

interface BaseDao<DATA> {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertIgnore(obj: DATA)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertOrUpdate(obj: DATA)

	@Insert
	suspend fun insert(vararg obj: DATA)

	@Update
	suspend fun update(obj: DATA)

	@Delete
	suspend fun delete(obj: DATA)

}
