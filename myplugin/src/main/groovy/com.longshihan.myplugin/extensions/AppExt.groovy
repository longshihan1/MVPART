package com.longshihan.myplugin.extensions

/**
 * 主工程的依赖项
 */
class AppExt extends ModuleExt{

    AppExt(String name) {
        super(name)
    }

    def name(String name){
        this.name = name
    }

    def applicationId(String applicationId){
        this.applicationId = applicationId
    }

    def dependMethod(String dependMethod){
        this.dependMethod = dependMethod
    }

    def mainActivity(String mainActivity){
        this.mainActivity = mainActivity
    }

    def modules(String... modules){
        this.modules.addAll(modules)
    }

    def applicationsName(String applicationsName){
        this.applicationsName=applicationsName
    }

    @Override
    String toString() {
        return "app = $name, applicationId = $applicationId, " +
                "dependMethod = $dependMethod," +
                "applicationName=$applicationsName," +
                "modules: ${modules.isEmpty()? "is empty" : "$modules"}"
    }
}