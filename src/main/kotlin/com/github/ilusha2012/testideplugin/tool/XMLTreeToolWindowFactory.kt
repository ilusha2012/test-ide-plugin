package com.github.ilusha2012.testideplugin.tool

import com.github.ilusha2012.testideplugin.services.XmlTreeService
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.ui.content.ContentFactory


class XMLTreeToolWindowFactory : ToolWindowFactory {

    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val xmlTreeToolWindow = XMLTreeToolWindow(toolWindow)

        val xmlTreeService = project.getService(XmlTreeService::class.java)
        xmlTreeToolWindow.xmlTree?.model = xmlTreeService.tree

        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(xmlTreeToolWindow.content, "", false)
        toolWindow.contentManager.addContent(content)

//        project.messageBus.connect().subscribe(
//            ToolWindowManagerListener.TOPIC,
//            XMLTreeToolWindowListener(
//                project = project,
//                toolWindowComponent = xmlTreeToolWindow
//            )
//        )
    }

    private inner class XMLTreeToolWindowListener(
        private val project: Project,
        private val toolWindowComponent: XMLTreeToolWindow
    ) : ToolWindowManagerListener {

//        override fun stateChanged(toolWindowManager: ToolWindowManager) {
////            toolWindowComponent.setContent(project.projectFile?.canonicalPath)
//        }
    }
}