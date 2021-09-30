package com.github.ilusha2012.testideplugin.tool

import com.github.ilusha2012.testideplugin.services.XmlTreeService
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTree

class XMLTreeToolWindow(project: Project, toolWindow: ToolWindow) {
    var xmlTree: JTree? = null
    var content: JPanel? = null
    var refreshBtn: JButton? = null

    init {
        refreshBtn?.addActionListener { e ->
            val xmlTreeService = project.getService(XmlTreeService::class.java)
            xmlTreeService.updateToolWindowContentFromCurrentFile()
        }
    }
}