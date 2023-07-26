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


    /**
     * Will return true as success if ignored.
     */
    @JvmSynthetic
    open fun insertAndGetStatus(): Boolean {
        return false
    }

}