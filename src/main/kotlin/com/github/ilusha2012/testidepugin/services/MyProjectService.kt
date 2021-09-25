package com.github.ilusha2012.testidepugin.services

import com.intellij.openapi.project.Project
import com.github.ilusha2012.testidepugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
