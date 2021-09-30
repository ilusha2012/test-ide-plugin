package com.github.ilusha2012.testideplugin.tool

import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.IOException
import javax.swing.JComponent
import javax.swing.JTree
import javax.swing.TransferHandler
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeNode
import javax.swing.tree.TreePath


internal class TreeTransferHandler : TransferHandler() {
    var nodesFlavor: DataFlavor? = null
    var flavors = arrayOfNulls<DataFlavor>(1)
    private lateinit var nodesToRemove: Array<DefaultMutableTreeNode>

    override fun canImport(support: TransferSupport): Boolean {
        if (!support.isDrop) {
            return false
        }
        support.setShowDropLocation(true)
        if (!support.isDataFlavorSupported(nodesFlavor)) {
            return false
        }
        // Do not allow a drop on the drag source selections.
        val dl = support.dropLocation as JTree.DropLocation
        val tree = support.component as JTree
        val dropRow = tree.getRowForPath(dl.path)
        val selRows = tree.selectionRows ?: return false
        for (i in selRows.indices) {
            if (selRows[i] == dropRow) {
                return false
            }
        }
        // Do not allow MOVE-action drops if a non-leaf node is
        // selected unless all of its children are also selected.
        val action = support.dropAction
        if (action == MOVE) {
            return haveCompleteNode(tree)
        }
        // Do not allow a non-leaf node to be copied to a level
        // which is less than its source level.
        val dest: TreePath = dl.path
        val target = dest.lastPathComponent as DefaultMutableTreeNode
        val path: TreePath = tree.getPathForRow(selRows[0])
        val firstNode = path.lastPathComponent as DefaultMutableTreeNode
        return !(firstNode.childCount > 0 &&
                target.level < firstNode.level)
    }

    private fun haveCompleteNode(tree: JTree): Boolean {
        val selRows = tree.selectionRows
        var path: TreePath = tree.getPathForRow(selRows[0])
        val first = path.lastPathComponent as DefaultMutableTreeNode
        val childCount = first.childCount
        // first has children and no children are selected.
        if (childCount > 0 && selRows.size == 1) return false
        // first may have children.
        for (i in 1 until selRows.size) {
            path = tree.getPathForRow(selRows[i])
            val next = path.lastPathComponent as DefaultMutableTreeNode
            if (first.isNodeChild(next)) {
                // Found a child of first.
                if (childCount > selRows.size - 1) {
                    // Not all children of first are selected.
                    return false
                }
            }
        }
        return true
    }

    override fun createTransferable(c: JComponent): Transferable? {
        val tree = c as JTree
        val paths: Array<TreePath>? = tree.selectionPaths
        if (paths != null) {
            // Make up a node array of copies for transfer and
            // another for/of the nodes that will be removed in
            // exportDone after a successful drop.
            val copies: MutableList<DefaultMutableTreeNode> = ArrayList()
            val toRemove: MutableList<DefaultMutableTreeNode> = ArrayList()
            val node = paths[0].lastPathComponent as DefaultMutableTreeNode
            val copy = copy(node)
            copies.add(copy)
            toRemove.add(node)
            for (i in 1 until paths.size) {
                val next = paths[i].lastPathComponent as DefaultMutableTreeNode
                // Do not allow higher level nodes to be added to list.
                if (next.level < node.level) {
                    break
                } else if (next.level > node.level) {  // child node
                    copy.add(copy(next))
                    // node already contains child
                } else {
                    // sibling
                    copies.add(copy(next))
                    toRemove.add(next)
                }
            }
            val nodes = copies.toTypedArray()
            nodesToRemove = toRemove.toTypedArray()
            return NodesTransferable(nodes)
        }
        return null
    }

    /** Defensive copy used in createTransferable.  */
    private fun copy(node: TreeNode): DefaultMutableTreeNode {
        return DefaultMutableTreeNode(node)
    }

    override fun exportDone(source: JComponent, data: Transferable, action: Int) {
        if (action and MOVE == MOVE) {
            val tree = source as JTree
            val model = tree.model as DefaultTreeModel
            // Remove nodes saved in nodesToRemove in createTransferable.
            for (i in nodesToRemove.indices) {
                model.removeNodeFromParent(nodesToRemove[i])
            }
        }
    }

    override fun getSourceActions(c: JComponent): Int {
        return COPY_OR_MOVE
    }

    override fun importData(support: TransferSupport): Boolean {
        if (!canImport(support)) {
            return false
        }
        // Extract transfer data.
        var nodes: Array<DefaultMutableTreeNode?>? = null
        try {
            val t = support.transferable
            nodes = t.getTransferData(nodesFlavor) as Array<DefaultMutableTreeNode?>
        } catch (ufe: UnsupportedFlavorException) {
            println("UnsupportedFlavor: " + ufe.message)
        } catch (ioe: IOException) {
            println("I/O error: " + ioe.message)
        }
        // Get drop location info.
        val dl = support.dropLocation as JTree.DropLocation
        val childIndex = dl.childIndex
        val dest: TreePath = dl.path
        val parent = dest.lastPathComponent as DefaultMutableTreeNode
        val tree = support.component as JTree
        val model = tree.model as DefaultTreeModel
        // Configure for drop mode.
        var index = childIndex // DropMode.INSERT
        if (childIndex == -1) {     // DropMode.ON
            index = parent.childCount
        }
        // Add data to model.
        for (i in nodes!!.indices) {
            model.insertNodeInto(nodes[i], parent, index++)
        }
        return true
    }

    inner class NodesTransferable(var nodes: Array<DefaultMutableTreeNode>) : Transferable {
        @Throws(UnsupportedFlavorException::class)
        override fun getTransferData(flavor: DataFlavor): Any {
            if (!isDataFlavorSupported(flavor)) throw UnsupportedFlavorException(flavor)
            return nodes
        }

        override fun getTransferDataFlavors(): Array<DataFlavor?> {
            return flavors
        }

        override fun isDataFlavorSupported(flavor: DataFlavor): Boolean {
            return nodesFlavor!!.equals(flavor)
        }
    }

    init {
        try {
            val mimeType = DataFlavor.javaJVMLocalObjectMimeType +
                    ";class=\"" +
                    Array<DefaultMutableTreeNode>::class.java.name +
                    "\""
            nodesFlavor = DataFlavor(mimeType)
            flavors[0] = nodesFlavor
        } catch (e: ClassNotFoundException) {
            println("ClassNotFound: " + e.message)
        }
    }
}