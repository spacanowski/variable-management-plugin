package pl.spc.variable.management

import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project

class VariableManagementPlugin implements Plugin<Project> {
    def void apply(Project project) {
        configureVaraibles(project)
    }

    def configureVaraibles(Project project) {
        if (!project.hasProperty('variablesManagementUrl')) {
            throw new IllegalArgumentException('Url must be defined')
        }

        if (project.hasProperty('variablesManagementUser') && project.hasProperty('variablesManagementPsswd')) {
            Authenticator.setDefault(new PasswordAuthentication(project.getProperty('variablesManagementUser'),
                project.getProperty('variablesManagementPsswd').toCharArray()))
        }

        getJson(project.getProperty('variablesManagementUrl')).each { k, v -> project.ext.set(k, v) }
    }

    def getJson(url) {
        def cachedVersions = new File(System.getProperty("user.home") + "/.gradle/versions.json")
        try {
            def json = url.toURL().getText()
            cachedVersions.text = json
            return new JsonSlurper().parseText(json)
        } catch (Exception e) {
            return new JsonSlurper().parse(cachedVersions)
        }
    }
}
