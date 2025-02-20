package objects.chapter1

class TicketOffice(
    private var amount: Long,
    private val tickets: MutableList<Ticket>,
) {
    fun sellTicketTo(audience: Audience) {
        plusAmount(audience.buy(getTicket()))
    }

    private fun getTicket(): Ticket {
        return tickets.removeFirst()
    }

    private fun plusAmount(amount: Long) {
        this.amount += amount
    }
}