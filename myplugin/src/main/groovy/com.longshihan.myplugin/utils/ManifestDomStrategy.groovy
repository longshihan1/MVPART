package com.longshihan.myplugin.utils

import com.longshihan.myplugin.extensions.ModuleExt
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.gradle.api.Project
import org.w3c.dom.Attr
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NamedNodeMap

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/**
 * Dom parse
 */
abstract class ManifestDomStrategy {

    protected String path
//    def manifest
    boolean edit = false
    Document manifest

    ManifestDomStrategy(Project project){
        path = "${project.getBuildFile().getParent()}/src/main/AndroidManifest.xml"
        System.out.println("  03-06  :"+path)
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance()
        File manifestFile = new File(path)
        if (!manifestFile.getParentFile().exists() && !manifestFile.getParentFile().mkdirs()){
            println "Unable to find AndroidManifest and create fail, please manually create"
        }
//        manifest = new XmlSlurper().parse(manifestFile)
//        manifest = new XmlParser().parse(manifestFile)
         manifest = db.parse(manifest)
    }


    abstract void setMainIntentFilter(def activity, boolean isFindMain)

    void resetManifest(ModuleExt moduleExt){
        try {

            NodeList manifests = document.getElementsByTagName("manifest")
            Node manifestNode = manifests.item(0)
            Element manifestElement = manifestNode.getOwnerDocument().getDocumentElement()
            if(manifest.@package != moduleExt.applicationId && moduleExt.applicationId != null && !moduleExt.applicationId.isEmpty()){
                manifestNode.getAttributes().getNamedItem("package").setNodeValue(moduleExt.applicationId)
                edit = true
                System.out.println("  03-02  :edit=true")
            }
            System.out.println("-------------------修改application的属性--------------------------")
            //获取所有application节点的集合
            NodeList applicationList = document.getElementsByTagName("application")
            //通过 item(i)方法 获取一个application节点，nodelist的索引值从0开始
            Node application = applicationList.item(0)
            if (moduleExt.applicationsName!=null&&!moduleExt.applicationsName.isEmpty()){
                System.out.println("-------------------生成android:name属性--------------------------")
                if (application.getAttributes().getNamedItem("android:name")==null) {
                    //添加android:name
                    Element element= (Element) application
                    Attr applicationattr = document.createAttribute("android:name")
                    applicationattr.setValue(moduleExt.applicationsName)
                    element.setAttributeNode(applicationattr)
                }else {
                    //修改android:name
                    application.getAttributes().getNamedItem("android:name").setNodeValue(moduleExt.applicationsName)
                }
            }

            boolean isFindMain = false
            if (moduleExt.mainActivity != null && !moduleExt.mainActivity.isEmpty()){
                NodeList childNodes = application.getChildNodes()
                for (int i = 0; i < childNodes.size(); i++) {
                    Node childNode = childNodes.get(i)
                    boolean isMainActivity=false
                    if (moduleExt.mainActivity == childNode.attribute("android:name")) {
                        //存在mainactivity
                        isFindMain = true
                        isMainActivity=true
                    }else {
                        isMainActivity=false
                    }
                    //有问题
                    Node childFilterNode = document.getElementsByTagName("intent-filter").item(0)
                    NodeList childActionNodes = childFilterNode.get()
                    for (int j = 0; j <childActionNodes.size(); j++) {
                        if (childNodes.item(k).getNodeName()=="action"){
                            if (attr.getNodeName() == "android:name" && attr.getNodeValue() == "android.intent.action.MAIN") {
                                if (isMainActivity){
                                   break
                                }else {

                                }
                            }
                        }
                    }
                    NamedNodeMap attrs = childFilterNode.getAttributes()

                    //遍历book的属性
                    for (int j = 0; j < attrs.getLength(); j++) {
                        //通过item(index)方法获取book节点的某一个属性
                        Node attr = attrs.item(j)
                        if (attr.getNodeName() == "android:name" && attr.getNodeValue() == "android.intent.action.MAIN") {
                            isFindMain = true
                        }
                    }
                }
            }
            //查找不到activity，就生成这个activity
            if (!isFindMain){
                System.out.println("  03-0703:::::::::"+manifest.toString())
                //创建要增加的节点元素
                Element nn = document.createElement("activity")
                //设置节点属性
                Attr attr = document.createAttribute("android:name")
                attr.setValue(moduleExt.mainActivity)
                nn.setAttributeNode(attr)
                Element nn1 = document.createElement("intent-filter")
                nn.appendChild(nn1)
                Element nn2 = document.createElement("action")
                Attr actionAttr = document.createAttribute("android:name")
                actionAttr.setValue("android.intent.action.MAIN")
                nn2.setAttributeNode(actionAttr)
                nn1.appendChild(nn2)
                Element nn3 = document.createElement("category")
                Attr categoryAttr = document.createAttribute("android:name")
                categoryAttr.setValue("android.intent.category.LAUNCHER")
                nn3.setAttributeNode(categoryAttr)
                nn1.appendChild(nn3)
                application.appendChild(nn)
                edit=true
            }
            if (edit){
                //创建一个用来转换DOM对象的工厂对象
                TransformerFactory factory = TransformerFactory.newInstance()
                //获得转换器对象
                Transformer t = factory.newTransformer()
                //定义要转换的源对象
                DOMSource xml = new DOMSource(document)
                //定义要转换到的目标文件
                StreamResult s = new StreamResult(new File(path))
                //开始转换
                t.transform(xml, s)
            }
        }catch (Exception e){}
    }
}

class LibraryManifestDomStrategy extends ManifestDomStrategy{

    LibraryManifestDomStrategy(Project project) {
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

class AppManifestDomStrategy extends ManifestDomStrategy {

    AppManifestDomStrategy(Project project) {
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
