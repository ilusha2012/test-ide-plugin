package com.github.ilusha2012.testideplugin.tool

import com.github.ilusha2012.testideplugin.services.XmlTreeService
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import java.io.StringWriter
import javax.swing.DropMode
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTree
import javax.swing.tree.TreeSelectionModel
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


class XMLTreeToolWindow(project: Project, toolWindow: ToolWindow) {
    var xmlTree: JTree? = null
    var content: JPanel? = null
    var refreshBtn: JButton? = null
    var applyBtn: JButton? = null

    init {
        refreshBtn?.addActionListener { e ->
            val xmlTreeService = project.getService(XmlTreeService::class.java)
            xmlTreeService.updateToolWindowContentFromCurrentFile()
        }

        applyBtn?.addActionListener { e ->
            TODO("Logic for saving tree to file")
//            val xmlTree = project.getService(XmlTreeService::class.java)?.xmlObject ?: return@addActionListener
//
//            // Transform the document into a string
//            val domSource = DOMSource(xmlTree.documentElement)
//            val tf = TransformerFactory.newInstance()
//            val transformer: Transformer = tf.newTransformer()
//            transformer.apply {
//                setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
//                setOutputProperty(OutputKeys.METHOD, "xml")
//                setOutputProperty(OutputKeys.ENCODING, "UTF-8")
//                setOutputProperty(OutputKeys.INDENT, "no")
//            }
//            val sw = StringWriter()
//            val sr = StreamResult(sw)
//            transformer.transform(domSource, sr)
//
//            println(sw.toString())
        }

        xmlTree?.dragEnabled = true
        xmlTree?.dropMode = DropMode.ON_OR_INSERT
        xmlTree?.transferHandler = TreeTransferHandler()
        xmlTree?.selectionModel?.selectionMode = TreeSelectionModel.CONTIGUOUS_TREE_SELECTION
    }
}