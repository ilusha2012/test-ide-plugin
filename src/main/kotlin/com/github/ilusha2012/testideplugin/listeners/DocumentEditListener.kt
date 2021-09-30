package com.github.ilusha2012.testideplugin.listeners

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.DocumentUtil

class DocumentEditListener : FileDocumentManagerListener {
    override fun beforeFileContentReload(file: VirtualFile, document: Document) {
        super.beforeFileContentReload(file, document)

//        DocumentUtil.
    }


}