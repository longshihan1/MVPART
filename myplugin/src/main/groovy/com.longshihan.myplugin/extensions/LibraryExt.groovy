package com.longshihan.myplugin.extensions

/**
 * 组件的依赖项
 */
class LibraryExt extends ModuleExt {

    boolean isRunAlone = false


    LibraryExt(String name) {
        super(name)
    }

    def name(String name){
        this.name = name
    }

    def isRunAlone(boolean isRunAlone){
        this.isRunAlone = isRunAlone
    }

    def applicationId(String applicationId){
        this.applicationId = applicationId
    }
    def mainActivity(String mainActivity){
        this.mainActivity = mainActivity
    }
    def modules(String... modules){
        this.modules.addAll(modules)
    }

    def dependMethod(String dependMethod){
        this.dependMethod = dependMethod
    }

    def applicationsName(String applicationsName){
        this.applicationsName=applicationsName
    }

    @Override
    String toString() {
        return "name = $name, isRunAlone = $isRunAlone, applicationId = $applicationId, " +
                " mainActivity = $mainActivity,"+
                "applicationName=$applicationsName," +
                "modules: ${modules.isEmpty()? "is empty" : "$modules"}"
    }
}