package item14_변수타입이명확하지않은경우확실하게지정하라

class Node(val name: String) {
    private fun create(name: String): Node? = Node(name)

    fun makeChild(childName: String): Node? {
        return create("$name.$childName").apply { print("Created ${this?.name} in ${this@Node.name}") }
    }
}

fun main() {
    Node("parent").makeChild("child")
}