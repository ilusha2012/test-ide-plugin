package com.github.ilusha2012.testideplugin.services

import com.github.ilusha2012.testideplugin.tool.XMLTreeToolWindowFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import java.io.StringReader
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource


class XmlTreeService(private val project: Project) {

    val PLUGIN_ID: String = "XMLTree"
    val PLUGIN_NAME: String = "XML Tree"

    var tree: DefaultTreeModel? = null

    fun updateTree(path: String? = null) {
        if (path == null) {
            return
        }
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val inputSource = InputSource(StringReader(path))
        val doc: Document = builder.parse(inputSource)
        val root: Node = doc.documentElement as Node

        tree = DefaultTreeModel(builtTreeNode(root))
    }

    fun updateToolWindowContent() {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(PLUGIN_NAME)

        if (toolWindow != null) {
            toolWindow.contentManager.removeAllContents(true)
            XMLTreeToolWindowFactory().createToolWindowContent(project, toolWindow)
        }
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
