package com.github.ilusha2012.testideplugin.services

import com.intellij.openapi.project.Project
import com.github.ilusha2012.testideplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
