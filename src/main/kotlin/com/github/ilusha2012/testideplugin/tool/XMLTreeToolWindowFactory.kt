package com.github.ilusha2012.testideplugin.tool

import com.github.ilusha2012.testideplugin.services.XmlTreeService
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory


class XMLTreeToolWindowFactory : ToolWindowFactory {

    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val xmlTreeToolWindow = XMLTreeToolWindow(project, toolWindow)

        val xmlTreeService = project.getService(XmlTreeService::class.java)
        xmlTreeToolWindow.xmlTree?.model = xmlTreeService.tree

        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(xmlTreeToolWindow.content, "", false)
        toolWindow.contentManager.addContent(content)
    }
}