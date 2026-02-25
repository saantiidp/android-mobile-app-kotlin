package com.example.expomanager.model.database

// Clase CouponDataAccess que actúa como intermediario entre CouponDao y las demás clases de la aplicación
class CouponDataAccess(private val couponDao: CouponDao) {
    // Método para insertar un solo cupón en la base de datos
    // Llama al método insertCoupon de CouponDao
    suspend fun insertCoupon(coupon: CouponEntity) = couponDao.insertCoupon(coupon)

    // Método para obtener todos los cupones de la base de datos
    // Llama al método getAllCoupons de CouponDao y retorna una lista de cupones
    suspend fun getAllCoupons(): List<CouponEntity> {
        return couponDao.getAllCoupons()
    }

    //  Función para insertar todos los cupones restaurados
    suspend fun insertAllCoupons(coupons: List<CouponEntity>) {
        couponDao.insertAllCoupons(coupons)
    }
}
