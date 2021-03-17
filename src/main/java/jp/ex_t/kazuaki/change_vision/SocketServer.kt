package jp.ex_t.kazuaki.change_vision

import java.io.*
import java.net.InetSocketAddress
import java.net.ServerSocket

class SocketServer(private val portNumber: Int) {
    private lateinit var serverSocket: ServerSocket
    private var isLaunched = false
    fun launch() {
        serverSocket = ServerSocket()
        serverSocket.reuseAddress = true
        serverSocket.bind(InetSocketAddress(portNumber))
        isLaunched = true
        println("Launched server. localhost:$portNumber")
    }

    fun stop() {
        isLaunched = false
        println("Stopping server...")
    }

    fun serve() {
        while (isLaunched) {
            val socket = serverSocket.accept()
            val inputStream = socket.getInputStream()
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val outputStream = socket.getOutputStream()
            val printWriter = PrintWriter(BufferedWriter(OutputStreamWriter(outputStream)))
            while (true) {
                while (inputStream.available() == 0) {}
                val receivedMessage = bufferedReader.readLine()
                if (receivedMessage == "") {
                    break
                }
                println("Received: $receivedMessage")
                printWriter.write(receivedMessage)
                printWriter.flush()
            }
            bufferedReader.close()
            printWriter.close()
            socket.close()
        }
        close()
    }

    private fun close() {
        serverSocket.close()
        println("Stopped server.")
    }
}