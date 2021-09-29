package com.github.ilusha2012.testideplugin.tool

import com.intellij.openapi.wm.ToolWindow
import javax.swing.JPanel
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.xml.parsers.DocumentBuilderFactory
import org.bouncycastle.asn1.x500.style.RFC4519Style.title
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList


class XMLTreeToolWindow(toolWindow: ToolWindow) {
    private var xmlTree: JTree? = null
    var content: JPanel? = null

    init {

        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc: Document = builder.parse("/Users/mac2/Downloads/wh-java-dev-test/sample-file.xml")
        val root: Node = doc.documentElement as Node

        val dtModel = DefaultTreeModel(builtTreeNode(root))
        xmlTree!!.model = dtModel
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