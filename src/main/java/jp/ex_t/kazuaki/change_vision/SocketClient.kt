package jp.ex_t.kazuaki.change_vision

import java.io.*
import java.net.Socket

class SocketClient(private val ipAddress: String, private val portNumber: Int) {
    private lateinit var socket: Socket
    private lateinit var inputStream: InputStream
    private lateinit var bufferedReader: BufferedReader
    private lateinit var outputStream: OutputStream
    private lateinit var printWriter: PrintWriter

    fun connect() {
        socket = Socket(ipAddress, portNumber)
        inputStream =socket.getInputStream()
        bufferedReader = BufferedReader(InputStreamReader(inputStream))
        outputStream = socket.getOutputStream()
        printWriter = PrintWriter(BufferedWriter(OutputStreamWriter(outputStream)))
    }

    fun write(message: String) {
        printWriter.write(message)
        printWriter.flush()
    }

    fun read(): String {
        while (inputStream.available() == 0) {
        }
        return bufferedReader.readLine()
    }

    fun close() {
        bufferedReader.close()
        printWriter.close()
        socket.close()
    }
}