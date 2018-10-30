package com.longshihan.myplugin

import com.longshihan.myplugin.extensions.AppconfigExt
import org.gradle.api.Plugin
import org.gradle.api.Project

public class MyPlugin implements Plugin<Project> {

    void apply(Project project) {
        System.out.println("========================")
        System.out.println("start gradle plugin!")
        System.out.println("========================")

        project.extensions.create('appConfig', AppconfigExt)



    }
}