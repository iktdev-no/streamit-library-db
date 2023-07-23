package no.iktdev.streamit.library.db.query

open class BaseQuery {
    @JvmSynthetic
    open fun insertAndGetId(): Int? {
        return null
    }

    @JvmSynthetic
    open fun insert() {
        return
    }


    @JvmSynthetic
    open fun insertAndGetStatus(): Boolean {
        return false
    }

}