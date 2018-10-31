package com.longshihan.myplugin.extensions

/**
 * 基础配置
 */
class ModuleExt {
    String name
    String applicationId
    String mainActivity
    String applicationsName
    String dependMethod = "implementation"
    List<String> modules = new ArrayList<>()

    ModuleExt(String name){
        this.name = name
    }

    @Override
    String toString() {
        return "name = $name, applicationId = $applicationId, " +
                " mainActivity = $mainActivity,applicationName=$applicationsName,"+
                "modules: ${modules.isEmpty()? "is empty" : "$modules"}"
    }
}