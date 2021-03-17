package jp.ex_t.kazuaki.change_vision

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate
import com.change_vision.jude.api.inf.ui.IWindow
import javax.swing.JOptionPane

class ServerAction: IPluginActionDelegate {
    private var isLaunchedServer = false
    private lateinit var socketServer: SocketServer

    @Throws(Exception::class)
    override fun run(window: IWindow) {
        try {
            if (!isLaunchedServer) {
                val portNumber = (JOptionPane.showInputDialog(window.parent, "Input server port.") ?: return).toInt()
                if ((0 > portNumber) or (65535 < portNumber)) throw NumberFormatException()
                socketServer = SocketServer(portNumber)
                socketServer.launch()
            } else {
                socketServer.stop()
            }
            isLaunchedServer = !isLaunchedServer

        } catch (e: NumberFormatException) {
            JOptionPane.showMessageDialog(window.parent, "Port number must be an integer and be 0 - 65535.", "Warning", JOptionPane.WARNING_MESSAGE)
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(window.parent, "Exception occurred.", "Alert", JOptionPane.ERROR_MESSAGE)
            throw e
        }
    }
}