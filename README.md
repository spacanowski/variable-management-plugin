# variable-managment-plugin
Gradle plugin for managing variables. Idea of this plugin is quite simple. When you have a lot of microservices or libraries and all have same urls or dependencies it's hard to change them all at once in every project. This plugin is for solving such problem by downloading json file with definitions of variables to use later in gradle script. So you store all your variables and versions in one place that needs to be changed instead of changing every build.gradle script. Plugin will store copy of this json in ```~/.gradle/versions.json``` for offline working.

#How to use

Configure address to your versions json file in ```gradle.settings``` or in ```build.gradle``` by assiging address to ```variablesManagementUrl``` variable. If you want to store your variables json in secure way set ```variablesManagementUser``` and ```variablesManagementPsswd``` with username and password respectively. Credentials will be send during get of json file.

build.gradle example:
```groovy
project.ext {
    variablesManagementUrl = 'http://<address>/<path>/<versions_json_file>'
}
```
If you use configuration by build.gradle it must be done before applying plugin.

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath "com.github.spacanowski:variable-managment-plugin:1.0"
    }
}
apply plugin: "pl.spc.variable.management"
```

Remember that plugin must be applied before use of first variable that will be imported from json.

## Example

versions json example
```json
{
    "groovyVersion": "2.4.7",
    "junitVersion": "4.+",
    "guavaVersion": "18.0",
    "secretNexus": "https://nexus.secretnexus.com/content/repositories/snapshots/"
}
```

gradle script variables usage
```groovy
repositories {
    maven { url "${secretNexus}" }
    mavenCentral()
}

dependencies {
    compile group: 'org.codehaus.groovy', name: 'groovy-all', version: "${groovyVersion}"
    compile group: 'com.google.guava', name: 'guava', version: "${guavaVersion}"
    testCompile group: 'junit', name: 'junit', version: "${junitVersion}"
}
```
