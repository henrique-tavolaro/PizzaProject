package com.example.pizzaproject.datasource.room

import androidx.room.*
import com.example.pizzaproject.domain.models.CartDetail
import com.example.pizzaproject.domain.models.OrderInProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProductToOrder(orderInProgress: OrderInProgress)

    @Transaction
    @Query("SELECT SUM(price) FROM order_in_progress")
    fun getOrderTotal(): Flow<Double?>

    @Transaction
    @Query("DELETE FROM order_in_progress WHERE product = :product")
    suspend fun deleteProductFromOrder(product: String)

    @Transaction
    @Query("DELETE FROM order_in_progress")
    suspend fun clearCart()

    @Transaction
    @Query(
        "SELECT product, SUM(price) as sum_price, COUNT(product) as product_count " +
                "FROM order_in_progress GROUP BY product"
    )
    fun getCart(): Flow<List<CartDetail>?>
}

