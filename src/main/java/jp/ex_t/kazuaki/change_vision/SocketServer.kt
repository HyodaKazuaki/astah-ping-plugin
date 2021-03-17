package jp.ex_t.kazuaki.change_vision

import kotlinx.coroutines.*
import java.io.*
import java.net.InetSocketAddress
import java.net.ServerSocket

class SocketServer(private val portNumber: Int) {
    private lateinit var serverSocket: ServerSocket
    private var isLaunched = false
    private lateinit var job: Job

    fun launch() {
        serverSocket = ServerSocket()
        serverSocket.reuseAddress = true
        serverSocket.bind(InetSocketAddress(portNumber))
        isLaunched = true
        job = GlobalScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    serve()
                }
            }
        }
        println("Launched server. localhost:$portNumber")
    }

    fun stop() {
        println("Stopping server...")
        isLaunched = false
        job.cancel()
        close()
        println("Stopped server.")
    }

    fun serve() {
        while (isLaunched) {
            serverSocket.accept().use { socket ->
                println("Connection established.")
                socket.getInputStream().use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { bufferedReader ->
                        socket.getOutputStream().use { outputStream ->
                            PrintWriter(BufferedWriter(OutputStreamWriter(outputStream))).use { printWriter ->
                                while (isLaunched) {
                                    while (inputStream.available() == 0) {}
                                    val receivedMessage = bufferedReader.readLine()
                                    if (receivedMessage == "") {
                                        break
                                    }
                                    println("Received: $receivedMessage")
                                    printWriter.println(receivedMessage)
                                    printWriter.flush()
                                }
                            }
                        }
                    }
                }
            }
            println("Connection closed.")
        }
    }

    private fun close() {
        serverSocket.close()
    }
}