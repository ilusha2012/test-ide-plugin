package com.github.ilusha2012.testideplugin.listeners

import com.github.ilusha2012.testideplugin.services.XmlTreeService
import com.intellij.codeInsight.daemon.impl.EditorTrackerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor

class CurrentDocumentSelectListener : EditorTrackerListener {

    override fun activeEditorsChanged(activeEditors: MutableList<Editor>) {
        if (activeEditors.size != 1) {
            return
        }

        val project = activeEditors[0].project
        val doc = activeEditors[0].document

        if (project == null) {
            return
        }

        val xmlTreeService = XmlTreeService.getInstance(project)
        ApplicationManager.getApplication().runReadAction {
            xmlTreeService.updateToolWindowContent(doc)
        }
    }
}