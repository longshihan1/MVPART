package com.longshihan.myplugin.utils

import com.longshihan.myplugin.extensions.ModuleExt
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.gradle.api.Project

abstract class ManifestStrategy {

    protected String path
    def manifest
    boolean edit = false

    ManifestStrategy(Project project){
        path = "${project.getBuildFile().getParent()}/src/main/AndroidManifest.xml"
        System.out.println("  03-06  :"+path)
        File manifestFile = new File(path)
        if (!manifestFile.getParentFile().exists() && !manifestFile.getParentFile().mkdirs()){
            println "Unable to find AndroidManifest and create fail, please manually create"
        }
//        manifest = new XmlSlurper().parse(manifestFile)
        manifest = new XmlParser().parse(manifestFile)
    }

    abstract void setMainIntentFilter(def activity, boolean isFindMain)

    void resetManifest(ModuleExt moduleExt){
        System.out.println("  03-04  :"+moduleExt.toString())
        if(manifest.@package != moduleExt.applicationId && moduleExt.applicationId != null && !moduleExt.applicationId.isEmpty()){
//            manifest.@package = moduleExt.applicationId
            manifest.attributes().put('package', moduleExt.applicationId)
            edit = true
            System.out.println("  03-02  :edit=true")
        }

        boolean isFindMain = false
        if (moduleExt.mainActivity != null && !moduleExt.mainActivity.isEmpty()){
            manifest.application.activity.each { activity ->
                if (activity.@'android:name' == moduleExt.mainActivity){
                    def filter = activity.'intent-filter'.find{
                        it.action.@'android:name' == "android.intent.action.MAIN"
                    }
                    isFindMain = true
                    setMainIntentFilter(activity, filter != null && filter.size() > 0)
                }
            }
        }

        manifest.application.activity.each { activity ->
            def filter = activity.'intent-filter'.find{
                it.action.@'android:name' == "android.intent.action.MAIN"
            }
            if (filter != null
                    && moduleExt.mainActivity != null
                    && !moduleExt.mainActivity.isEmpty()
                    && activity.@'android:name' != moduleExt.mainActivity){
                filter.replaceNode{}
                edit = true
                System.out.println("  03-03  :edit=true")
            }
        }

        if (moduleExt.applicationsName!=null&&!moduleExt.applicationsName.isEmpty()){
//            manifest.application.attributes().put('android:name', moduleExt.applicationsName)
//            manifest.@'android:name' = moduleExt.applicationId
//             NodeChild node= manifest.application[0]
//             System.out.println(" -----------   "+node.toString())
            manifest.attributes().put('android:name', moduleExt.applicationsName)
//            if (manifest.application.getProperty('@android:name')==null){
//                System.out.println("  03-0501  :$moduleExt.applicationsName :::::::::::"+manifest.application.getProperty('@android:name')+":::::::::"+manifest.toString())
//            }else {
//                manifest.application.setProperty('@android:name',moduleExt.applicationsName)
//                System.out.println("  03-0502  :$moduleExt.applicationsName :::::::::::"+manifest.application.getProperty('@android:name')+":::::::::"+manifest.toString())
//            }
//            System.out.println("  03-05  :$moduleExt.applicationsName :"+manifest.application.@'android:name'+":::::::::"+manifest.toString())
//            manifest.application.setProperty('@android:name',moduleExt.applicationsName)
//            System.out.println("  03-07  :$moduleExt.applicationsName :"+manifest.application.@'android:name'+":::::::::"+manifest.toString())
//            edit=true
        }

        //查找不到activity，就生成这个activity
        if (!isFindMain){
//            addMainActivity(manifest.application, moduleExt)
            System.out.println("  03-0703:::::::::"+manifest.toString())

        }

        if (edit){
            buildModulesManifest(manifest)
        }
    }

    void addMainActivity(def application, ModuleExt modulesExt){
        if (modulesExt.mainActivity != null && !modulesExt.mainActivity.isEmpty()){
            application.appendNode{
                activity('android:name': modulesExt.mainActivity){
                    'intent-filter'{
                        action('android:name':"android.intent.action.MAIN")
                        category('android:name':"android.intent.category.LAUNCHER")
                    }
                }
            }
            edit = true
        }

    }

    void buildModulesManifest(def manifest){

        def fileText = new File(path)
        StreamingMarkupBuilder outputBuilder = new StreamingMarkupBuilder()
        def root = outputBuilder.bind{
            mkp.xmlDeclaration()// <?xml version='1.0'?
            mkp.declareNamespace('android':'http://schemas.android.com/apk/res/android')// 命名空间
            mkp.yield manifest
        }
        String result = XmlUtil.serialize(root)
        System.out.println("  03-01  "+result)
        fileText.text = result

    }
}

class LibraryManifestStrategy extends ManifestStrategy{

    LibraryManifestStrategy(Project project) {
        super(project)
    }

    @Override
    void setMainIntentFilter(def activity, boolean isFindMain) {
        if (isFindMain){
            System.out.println("  03-03:build lib  ")
            activity.'intent-filter'.each{
                if(it.action.@'android:name' == "android.intent.action.MAIN"){
                    it.replaceNode{}
                    edit = true
                }
            }
        }
    }
}

class AppManifestStrategy extends ManifestStrategy {

    AppManifestStrategy(Project project) {
        super(project)
    }

    @Override
    void setMainIntentFilter(def activity, boolean isFindMain) {
        if (!isFindMain) {
            activity.appendNode {
                'intent-filter' {
                    action('android:name': "android.intent.action.MAIN")
                    category('android:name': "android.intent.category.LAUNCHER")
                }
            }
            edit = true
        }
    }
}
