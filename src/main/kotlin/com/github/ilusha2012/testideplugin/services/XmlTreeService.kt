package com.github.ilusha2012.testideplugin.services

import com.github.ilusha2012.testideplugin.tool.XMLTreeToolWindowFactory
import com.intellij.ide.highlighter.XmlFileType
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.elementType
import com.intellij.psi.xml.XmlElementType
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.refactoring.suggested.createSmartPointer
import java.io.InputStream
import java.nio.file.Path
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class XmlTreeService(private val project: Project) {

    companion object {
        const val PLUGIN_NAME: String = "XML Tree"

        fun getInstance(project: Project): XmlTreeService = project.getService(XmlTreeService::class.java)
    }

    var tree: DefaultTreeModel? = null
    var file: VirtualFile? = null
    var psiFile: PsiFile? = null

    fun updateToolWindowContentFromCurrentFile() {
        updateToolWindowContent()
    }

    fun updateToolWindowContent(document: Document) {
        file = FileDocumentManager.getInstance().getFile(document)
        psiFile = PsiManager.getInstance(project).findFile(file!!)
        updateToolWindowContent()
    }

    private fun updateToolWindowContent() {
        val type = psiFile?.fileElementType
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(PLUGIN_NAME) ?: return
        toolWindow.contentManager.removeAllContents(true)

        if (type == null) {
            return
        }
        if (type.equals(XmlFileType.INSTANCE)) {
            return
        }

        updateTree(file?.inputStream)
        XMLTreeToolWindowFactory().createToolWindowContent(project, toolWindow)
    }

    private fun updateTree(inputStream: InputStream? = null) {
        if (inputStream == null) {
            return
        }
        val currentFile = psiFile
        if (currentFile is XmlFile) {
            tree = DefaultTreeModel(builtTree(currentFile.rootTag!!))
        }
    }

    private fun builtTree(node: PsiElement): DefaultMutableTreeNode {
        val dmtNode = DefaultMutableTreeNode(node.createSmartPointer())

        val nodeList = node.children
        for (element in nodeList) {
            if (element.elementType != XmlElementType.XML_TAG) {
                continue
            }
            val tag = element as XmlTag
            val srcItem = tag.getAttributeValue("src")

            if (srcItem != null) {
                val file = VirtualFileManager.getInstance()
                    .findFileByNioPath(Path.of("${file?.parent?.canonicalPath}/${srcItem}"))
                    ?: continue
                val psiElement = PsiManager.getInstance(project).findFile(file)?.createSmartPointer()?.element
                    ?: continue

                if (psiElement is XmlFile) {
                    dmtNode.add(builtTree(psiElement.rootTag!!))
                }

            } else {
                dmtNode.add(builtTree(element))
            }
        }
        return dmtNode
    }
}
