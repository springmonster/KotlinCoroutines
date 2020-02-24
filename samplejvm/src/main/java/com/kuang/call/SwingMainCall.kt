package com.kuang.call

import com.kuang.retrofit.coroutines.call.githubNetCall
import com.kuang.retrofit.dto.User
import com.kuang.utils.Logger
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.SwingUtilities

class MainWindow : JFrame() {

    private val scope = MainScope()

    private var button = JButton("Click me")

    init {
        initThread()

        contentPane.add(button, BorderLayout.NORTH)
        title = "Coroutine1.3"
        setSize(600, 100)
        isResizable = true
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        addWindowListener(object : WindowListener {
            override fun windowDeiconified(e: WindowEvent) = Unit

            override fun windowClosing(e: WindowEvent) {
                Logger.debug("window closing")
                scope.cancel()
                removeWindowListener(this)
            }

            //region unused listeners
            override fun windowClosed(e: WindowEvent) = Unit

            override fun windowActivated(e: WindowEvent) = Unit
            override fun windowDeactivated(e: WindowEvent) = Unit
            override fun windowOpened(e: WindowEvent) = Unit
            override fun windowIconified(e: WindowEvent) = Unit
            //endregion
        })
    }

    fun onButtonClick(listener: suspend (ActionEvent) -> Unit) {
        button.addActionListener { event ->
            scope.launch {
                listener(event)
            }
        }
    }

    fun setButtonText(text: String) {
        this.button.text = text
    }

    private fun initThread() {
        SwingUtilities.invokeAndWait {
            val oldName = Thread.currentThread().name
            val newName = "Swing Main"
            Thread.currentThread().name = newName
            Logger.error("Change thread name from $oldName to $newName for debug only.")
        }
    }
}

fun main() {
    val frame = MainWindow()

    frame.onButtonClick {
        githubNetCall.getUser("SpringMonster").enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Logger.error(t)
            }

            // 类似于Android的handler去刷新UI
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    SwingUtilities.invokeLater {
                        Logger.debug(response.body())
                        frame.setButtonText(response.body()?.name ?: "default")
                    }
                }
            }
        })
    }
}
