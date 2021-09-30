package com.github.ilusha2012.testideplugin.services

import com.github.ilusha2012.testideplugin.tool.XMLTreeToolWindowFactory
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import java.io.InputStream
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXParseException


class XmlTreeService(private val project: Project) {

    companion object {
        const val PLUGIN_ID: String = "XMLTree"
        const val PLUGIN_NAME: String = "XML Tree"
        const val TYPE_SUPPORT: String = "XML"
    }

    var tree: DefaultTreeModel? = null

    fun updateTree(inputStream: InputStream? = null) {
        if (inputStream == null) {
            return
        }
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc = try {
            builder.parse(inputStream)
        } catch (e: SAXParseException) {
            null
        } ?: return

        val root: Node = doc.documentElement as Node

        tree = DefaultTreeModel(builtTreeNode(root))
    }

    fun updateToolWindowContent(document: Document) {
        val file = FileDocumentManager.getInstance().getFile(document)
        val type = file?.fileType
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(PLUGIN_NAME) ?: return

        toolWindow.contentManager.removeAllContents(true)

        if (type?.name != TYPE_SUPPORT) {
            return
        }

        updateTree(file.inputStream)
        XMLTreeToolWindowFactory().createToolWindowContent(project, toolWindow)
    }

    private fun builtTreeNode(root: Node): DefaultMutableTreeNode {
        val title = if (root.attributes.getNamedItem("title") != null) {
            root.attributes.getNamedItem("title").nodeValue
        } else {
            root.textContent
        }
        val dmtNode = DefaultMutableTreeNode(title)
        val nodeList: NodeList = root.childNodes
        for (index in 0 until nodeList.length) {
            val tempNode: Node = nodeList.item(index)
            if (tempNode.nodeType == Node.ELEMENT_NODE && tempNode.hasChildNodes()) {
                dmtNode.add(builtTreeNode(tempNode))
            }
        }
        return dmtNode
    }
}
