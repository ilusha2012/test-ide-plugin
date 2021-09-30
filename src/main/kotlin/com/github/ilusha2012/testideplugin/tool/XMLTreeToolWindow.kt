package com.github.ilusha2012.testideplugin.tool

import com.intellij.openapi.wm.ToolWindow
import javax.swing.JPanel
import javax.swing.JTree

class XMLTreeToolWindow(toolWindow: ToolWindow) {
    var xmlTree: JTree? = null
    var content: JPanel? = null
}