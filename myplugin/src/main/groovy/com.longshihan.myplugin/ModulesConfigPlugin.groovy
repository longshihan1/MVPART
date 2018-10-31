package com.longshihan.myplugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.longshihan.myplugin.extensions.AppConfigExt
import com.longshihan.myplugin.extensions.AppExt
import com.longshihan.myplugin.extensions.LibraryExt
import com.longshihan.myplugin.utils.AppManifestStrategy
import com.longshihan.myplugin.utils.LibraryManifestStrategy
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException

import java.util.stream.Collectors

class ModulesConfigPlugin implements Plugin<Project> {

    private static final String PARENT_EXTENSION_NAME = "appConfig"

    @Override
    void apply(Project project) {
        System.out.println("-----------------------start01------------------------------------")
        AppConfigExt appConfigExt = getAppConfigExtension(project)
        configModules(project, appConfigExt)
        System.out.println("-----------------------end01------------------------------------")
    }

    static void configModules(Project project, AppConfigExt appConfigExt){
        if (appConfigExt == null){
            throw new NullPointerException("can not find appConfig")
        }
        List<AppExt> filterList = appConfigExt.apps.stream()
                .filter{ (it.name.startsWith(':') ? it.name : new String(":" + it.name)).endsWith(project.name) }
                .skip(0).collect()

        if (filterList != null && filterList.size() > 0){
            AppExt appExt = filterList.get(0)
            System.out.println(" 02:   "+appExt.toString())
            AppPlugin appPlugin = project.plugins.apply(AppPlugin)
            appPlugin.extension.defaultConfig.setApplicationId(appExt.applicationId)
            new AppManifestStrategy(project).resetManifest(appExt)
            dependModules(project, appExt, appConfigExt)
        }else {
            List<LibraryExt> modulefilterList = appConfigExt.modules.stream().filter { it.name.endsWith(project.name) }.skip(0).collect()
            if (modulefilterList != null && modulefilterList.size() > 0) {
                modulesRunAlone(project, modulefilterList, appConfigExt.debugEnable)
            }
        }
    }

    static void dependModules(Project project, AppExt appExt, AppConfigExt appConfigExt){
        Map<String,LibraryExt> moduleExtMap = appConfigExt.modules.stream().filter{
            modules ->
                String modulesName = appExt.modules.stream().find{ it.contains(modules.name) }
                modulesName != null && !modulesName.isEmpty()
        }.collect(Collectors.toMap({ it.name},{ it -> it}))

        if (appExt.modules != null && appExt.modules.size() > 0){
            List<String> modulesList = appExt.modules.stream()
                    .filter{
                appConfigExt.debugEnable ? (moduleExtMap != null && !moduleExtMap[it].isRunAlone) : true }
            .map{
                project.dependencies.add(appExt.dependMethod, project.project(it))
                it
            }.collect()
            println("build app: [$appExt.name] , depend modules: $modulesList")
        }
    }

    AppConfigExt getAppConfigExtension(Project project){
        try{
            return project.parent.extensions.getByName(PARENT_EXTENSION_NAME) as AppConfigExt
        }catch (UnknownDomainObjectException ignored){
            if (project.parent != null){
                getAppConfigExtension(project.parent)
            }else {
                throw new UnknownDomainObjectException(ignored as String)
            }
        }
    }

    private static void modulesRunAlone(Project project, List<LibraryExt> filterList, boolean isDebug) {
        LibraryExt moduleExt = filterList.get(0)
        System.out.println(" 02-01:   " + moduleExt.toString())
        if (isDebug && moduleExt.isRunAlone) {
            AppPlugin appPlugin = project.plugins.apply(AppPlugin)
            appPlugin.extension.defaultConfig.setApplicationId(moduleExt.applicationId)
            //依赖子module
            if (moduleExt.modules != null && moduleExt.modules.size() > 0){
                List<String> modulesList = moduleExt.modules.stream()
                .map{
                    project.dependencies.add(moduleExt.dependMethod, project.project(it))
                    it
                }.collect()
                println("build app: [$moduleExt.name] , depend modules: $modulesList")
            }

//            if (moduleExt.runAloneSuper != null && !moduleExt.runAloneSuper.isEmpty()) {
//                project.dependencies.add("implementation", project.project(moduleExt.runAloneSuper))
//                println("build run alone modules: [$moduleExt.name], runSuper = $moduleExt.runAloneSuper")
//            } else {
//                println("build run alone modules: [$moduleExt.name]")
//            }
            if (moduleExt.mainActivity != null && !moduleExt.mainActivity.isEmpty()) {
                new AppManifestStrategy(project).resetManifest(moduleExt)
            }
        } else {
            project.plugins.apply(LibraryPlugin)
            new LibraryManifestStrategy(project).resetManifest(moduleExt)
        }
    }
}