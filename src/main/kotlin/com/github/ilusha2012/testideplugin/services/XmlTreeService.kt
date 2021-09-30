package com.github.ilusha2012.testideplugin.services

import com.github.ilusha2012.testideplugin.tool.XMLTreeToolWindowFactory
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.ToolWindowManager
import java.io.InputStream
import java.nio.file.Path
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
    var file: VirtualFile? = null

    fun updateToolWindowContentFromCurrentFile() {
        updateToolWindowContent()
    }

    fun updateToolWindowContent(document: Document) {
        file = FileDocumentManager.getInstance().getFile(document)
        updateToolWindowContent()
    }

    private fun updateToolWindowContent() {
        val type = file?.fileType
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(PLUGIN_NAME) ?: return
        toolWindow.contentManager.removeAllContents(true)

        if (type?.name != TYPE_SUPPORT) {
            return
        }

        updateTree(file?.inputStream)
        XMLTreeToolWindowFactory().createToolWindowContent(project, toolWindow)
    }

    private fun updateTree(inputStream: InputStream? = null) {
        if (inputStream == null) {
            return
        }
        val node = parseXml(inputStream) ?: return
        tree = DefaultTreeModel(builtTreeNode(node))
    }

    private fun parseXml(inputStream: InputStream): Node? {
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        return try {
            builder.parse(inputStream).documentElement as Node
        } catch (e: SAXParseException) {
            null
        }
    }

    private fun builtTreeNode(rootNode: Node): DefaultMutableTreeNode {
        val title = if (rootNode.attributes.getNamedItem("title") != null) {
            rootNode.attributes.getNamedItem("title").nodeValue
        } else if(rootNode.childNodes?.item(1)?.nodeType == Node.ELEMENT_NODE) {
            rootNode.attributes?.getNamedItem("id")?.nodeValue ?: rootNode.nodeName
        } else {
            rootNode.textContent
        }
        val dmtNode = DefaultMutableTreeNode(title)

        val nodeList: NodeList = rootNode.childNodes
        for (index in 0 until nodeList.length) {
            val tempNode: Node = nodeList.item(index)

            if (tempNode.nodeType == Node.ELEMENT_NODE && tempNode.hasChildNodes()) {
                dmtNode.add(builtTreeNode(tempNode))
            } else {
                val srcItem = tempNode.attributes?.getNamedItem("src") ?: continue
                val file = VirtualFileManager.getInstance()
                    .findFileByNioPath(Path.of("${file?.parent?.canonicalPath}/${srcItem.nodeValue}"))
                    ?: continue

                val node = parseXml(file.inputStream)
                if (node != null) {
                    dmtNode.add(builtTreeNode(node))
                }
            }
        }
        return dmtNode
    }
}
