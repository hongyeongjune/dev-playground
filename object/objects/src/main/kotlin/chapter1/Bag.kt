package objects.chapter1

class Bag(
    private val invitation: Invitation?,
    private var amount: Long,
    private var ticket: Ticket?,
) {
    fun hold(ticket: Ticket): Long {
        return when {
            hasInvitation() -> {
                setTicket(ticket)
                0L
            }
            else -> {
                setTicket(ticket)
                minusAmount(ticket.fee)
                ticket.fee
            }
        }
    }

    private fun hasInvitation(): Boolean {
        return invitation != null
    }

    private fun minusAmount(amount: Long) {
        this.amount -= amount
    }

    private fun setTicket(ticket: Ticket) {
        this.ticket = ticket
    }
}