package adminSide

object Session {
    private lateinit var session: Admin

    fun startSession(adminSession: Admin) {
        session = adminSession
    }

    fun disconnect() {
        session = Admin.empty()
    }

    fun get(): Admin =
        session

}