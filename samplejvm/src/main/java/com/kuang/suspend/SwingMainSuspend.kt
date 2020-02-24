package com.kuang.suspend

import com.kuang.retrofit.coroutines.suspend.githubNetSuspend
import com.kuang.utils.Logger
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
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

    /**
     * 因为是在MainScope中进行的操作，所以输入日志，对button进行赋值都是在主线程中
     * 如何进行一场捕获，进行try catch就可以
     */
    frame.onButtonClick {
        try {
            val user = githubNetSuspend.getUser("SpringMonster")
            Logger.debug(user)
            frame.setButtonText(user.name)
        } catch (e: Exception) {
            Logger.error(e)
        }
    }
}
