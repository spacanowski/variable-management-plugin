package pl.spc.variable.management

import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project

class VariableManagementPlugin implements Plugin<Project> {
    def void apply(Project project) {
        configureVaraibles(project)
    }

    def configureVaraibles(Project project) {
        def url = project.ext.get('variablesManagementUrl')
        println url

        if (url == null || url.empty) {
            throw new IllegalArgumentException('Url must be defined')
        }

        getJson(url).each { k, v -> project.ext.set(k, v) }
    }

    def getJson(url) {
        def cachedVersions = new File("~/.gradle/versions.json")
        try {
            def json = url.toURL().getText()
            cachedVersions.text = json
            println json
            return new JsonSlurper().parseText(json)
        } catch (Exception e) {
            return new JsonSlurper().parse(cachedVersions)
        }
    }
}
