package no.iktdev.streamit.library.db.query

interface CommonQueryFuncions {
    @JvmSynthetic
    fun insert(): Unit {
        insertAndGetStatus()
    }
    @JvmSynthetic
    fun insertAndGetStatus(): Boolean { return false}
    @JvmSynthetic
    fun insertAndGetId(): Int? {
        return null
    }
}