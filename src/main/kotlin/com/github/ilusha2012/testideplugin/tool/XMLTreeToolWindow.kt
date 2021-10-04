package com.github.ilusha2012.testideplugin.tool

import com.github.ilusha2012.testideplugin.services.XmlTreeService
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import javax.swing.DropMode
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTree
import javax.swing.tree.TreeSelectionModel


class XMLTreeToolWindow(project: Project, toolWindow: ToolWindow) {
    var xmlTree: JTree? = null
    var content: JPanel? = null
    var refreshBtn: JButton? = null

    init {
        refreshBtn?.addActionListener { e ->
            val xmlTreeService = XmlTreeService.getInstance(project)
            xmlTreeService.updateToolWindowContentFromCurrentFile()
        }

        xmlTree?.cellRenderer = SmartPsiElementCellRenderer()
        xmlTree?.dragEnabled = true
        xmlTree?.dropMode = DropMode.ON_OR_INSERT
        xmlTree?.transferHandler = TreeTransferHandler()
        xmlTree?.selectionModel?.selectionMode = TreeSelectionModel.CONTIGUOUS_TREE_SELECTION
    }
}