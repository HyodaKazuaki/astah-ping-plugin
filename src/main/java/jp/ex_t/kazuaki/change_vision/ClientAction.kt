package jp.ex_t.kazuaki.change_vision

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate
import com.change_vision.jude.api.inf.ui.IWindow
import javax.swing.JOptionPane

class ClientAction: IPluginActionDelegate {
    @Throws(Exception::class)
    override fun run(window: IWindow) {
        try {
            val ipAddress = JOptionPane.showInputDialog(window.parent, "Input IP address or \"localhost\"") ?: return
            val ipAddressPattern = Regex("""^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])${'$'}""")
            if (!ipAddressPattern.matches(ipAddress) && ipAddress != "localhost") throw IPAddressFormatException()
            val portNumber = (JOptionPane.showInputDialog(window.parent, "Input server port.") ?: return).toInt()
            if ((0 > portNumber) or (65535 < portNumber)) throw NumberFormatException()

            val socketClient = SocketClient(ipAddress, portNumber)
            socketClient.connect()
            println("Connection established.")

            while (true) {
                val message = JOptionPane.showInputDialog(window.parent, "Input message what you send server. Empty closes connection.") ?: ""
                socketClient.write(message)
                if (message == "") {
                    socketClient.close()
                    println("Connection closed.")
                    break
                }
                val receivedMessage = socketClient.read()
                println("Received: $receivedMessage")
            }

        } catch (e: IPAddressFormatException) {
            JOptionPane.showMessageDialog(window.parent, "IP address must be IPv4 address.", "Warning", JOptionPane.WARNING_MESSAGE)
        } catch (e: NumberFormatException) {
            JOptionPane.showMessageDialog(window.parent, "Port number must be an integer and be 0 - 65535.", "Warning", JOptionPane.WARNING_MESSAGE)
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(window.parent, "Exception occurred.", "Alert", JOptionPane.ERROR_MESSAGE)
            throw e
        }
    }

    class IPAddressFormatException: NumberFormatException() {}
}