package com.github.ilusha2012.testideplugin.tool

import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlTag
import java.awt.Component
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer


class SmartPsiElementCellRenderer : DefaultTreeCellRenderer() {

    companion object {
        const val SPAN_FORMAT = "<span style='color:%s;'>%s</span>"
        const val DEFAULT_COLOR = "#5d6dce"
        const val UNKNOWN_COLOR = "red"
    }

    override fun getTreeCellRendererComponent(
        tree: JTree?,
        value: Any,
        sel: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ): Component {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus)
        val node = value as DefaultMutableTreeNode
        val userObject = node.userObject
        val text = if (userObject is SmartPsiElementPointer<*>) {
            val smartPsiElementPointer = userObject as SmartPsiElementPointer<PsiElement>
            val el = smartPsiElementPointer.element
            if (el is XmlTag && el.getAttributeValue("title") != null) {
                String.format(SPAN_FORMAT, DEFAULT_COLOR, el.getAttributeValue("title"))
            } else if (el is XmlTag && el.getAttributeValue("id") != null) {
                String.format(SPAN_FORMAT, DEFAULT_COLOR, el.text)
            } else {
                String.format(SPAN_FORMAT, DEFAULT_COLOR, "root")
            }
        } else {
            String.format(SPAN_FORMAT, UNKNOWN_COLOR, userObject)
        }
        this.text = "<html>$text</html>"
        return this
    }

}