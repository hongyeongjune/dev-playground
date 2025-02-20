package objects.chapter1

class Theater(
    private val ticketSettler: TicketSeller
) {
    fun enter(audience: Audience) {
        ticketSettler.sellTo(audience)
    }
}