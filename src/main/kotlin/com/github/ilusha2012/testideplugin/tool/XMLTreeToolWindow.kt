package com.github.ilusha2012.testideplugin.tool

import com.intellij.openapi.wm.ToolWindow
import java.io.File
import javax.swing.JPanel
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.xml.parsers.SAXParserFactory
import org.xml.sax.helpers.DefaultHandler


class XMLTreeToolWindow(toolWindow: ToolWindow) {
    private var xmlTree: JTree? = null
    var content: JPanel? = null

    init {
//        val base = DefaultMutableTreeNode("XML Viewer")
//
//        val fact = SAXParserFactory.newInstance()
//        val parser = fact.newSAXParser()
//        val defaultHandler = DefaultHandler()
//        parser.parse(File("/Users/mac2/Downloads/wh-java-dev-test/sample-file.xml"), defaultHandler)
//
//        defaultHandler.startDocument()
//
//
        xmlTree!!
    }
}