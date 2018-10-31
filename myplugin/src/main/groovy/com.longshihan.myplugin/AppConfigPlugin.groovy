package com.longshihan.myplugin

import com.longshihan.myplugin.extensions.AppConfigExt
import com.longshihan.myplugin.extensions.AppExt
import com.longshihan.myplugin.extensions.LibraryExt
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.function.BiConsumer

/**
 * 只检查相关配置和收集相关配置
 */
class AppConfigPlugin implements Plugin<Project> {

    private static final String EXTENSION_NAME = "appConfig"

    @Override
    void apply(Project project) {
        System.out.println("-----------------------start------------------------------------")
        AppConfigExt appConfigExt = new AppConfigExt(project)
        project.extensions.add(EXTENSION_NAME, appConfigExt)
        configApp(project)
        System.out.println("-----------------------end------------------------------------")
    }

    void configApp(Project project) {
        List<String> moduleList = new ArrayList<>()
        NamedDomainObjectContainer<AppExt> appList
        AppConfigExt appConfigExt
        project.afterEvaluate {
            appConfigExt = project.extensions.getByName(EXTENSION_NAME) as AppConfigExt
            appList = appConfigExt.apps
            //检测包内是否重复
            checkRepeat(appConfigExt)
            //检查配置是否重复
            checkModules(appConfigExt,moduleList)
        }
        initChildModules(moduleList, project)
        println("project child modules: $moduleList")
    }

    void initChildModules(List<String> moduleList ,Project project){

        if (project.childProjects.isEmpty()){
            System.out.println(" 1:   "+project.toString())
            moduleList.add(project.toString()
                    .replace("project ","")
                    .replace('\'',''))
            return
        }
        project.childProjects.entrySet().forEach{
            initChildModules(moduleList, it.value)
        }

    }

    static void checkRepeat(AppConfigExt appConfigExt){
        Map<String,List<AppExt>> appGroupMap =
                appConfigExt.apps.groupBy{ it.name.startsWith(':') ? it.name : new String(":" + it.name)}
        appGroupMap.forEach(new BiConsumer<String, List<AppExt>>() {
            @Override
            void accept(String s, List<AppExt> appExts) {
                if (appExts.size() > 1){
                    throw new IllegalArgumentException("app is repeat. app name: [$s]")
                }
            }
        })

        Map<String,List<LibraryExt>> moduleGroupMap =
                appConfigExt.modules.groupBy{ it.name.startsWith(':') ? it.name : new String(":" + it.name)}

        moduleGroupMap.forEach(new BiConsumer<String, List<LibraryExt>>() {
            @Override
            void accept(String s, List<LibraryExt> libraryExts) {
                if (libraryExts.size() > 1){
                    throw new IllegalArgumentException("modules is repeat. modules name: [$s]")
                }
            }
        })
    }

    static void checkModules(AppConfigExt appConfigExt, List<String> projectModules){
        Set<String> configSet = new HashSet<>()
        Set<String> modulesSet = new HashSet<>()
        if (projectModules != null){
            modulesSet.addAll(projectModules)
        }
        List<String> notFoundList = new ArrayList<>()

        List<String> appNameList = appConfigExt.apps
                .stream()
                .map{it.name.startsWith(':') ? it.name : new String(":" + it.name)}.collect()

        List<String> moduleNameList = appConfigExt.modules.stream().map{
                            String name = it.name.startsWith(':') ? it.name : new String(":" + it.name)
                            if (appNameList.contains(name)){
                                throw new IllegalArgumentException("$it.name already configured " +
                                        "as an application, please check appConfig")
                            }
                            name
                        }.collect()
        println "moduleNameList = $moduleNameList"

        configSet.addAll(appNameList)
        configSet.addAll(moduleNameList)

        configSet.forEach{
            if(!modulesSet.contains(it)){
                notFoundList.add(it)
            }
        }
        if (notFoundList.size() > 0){
            throw  new IllegalArgumentException(
                    "not fount modules = " + notFoundList
            )
        }

        appConfigExt.apps.stream().forEach{ app ->
            app.modules.stream().forEach{
                if (! configSet.contains(it)){
                    throw  new IllegalArgumentException(
                            "appConfig error , can not find $app.name modules $it by project" )
                }
            }
        }

        println("modules: " + configSet)
    }

}