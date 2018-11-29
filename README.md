模块化通信框架module-service-manager
===========================

****
## 目录
* [模块化或组件化](#模块化或组件化)
* [理想的模块化架构](#理想的模块化架构)
* [模块化后的一些优化](#模块化后的一些优化)
* [模块化通信](#模块化通信)
* [模块化通信方案介绍](#模块化通信方案介绍)
* [模块间通信方案引入步骤](#模块间通信方案引入步骤)
****

## 模块化或组件化

随着客户端项目越来越大，一个项目往往会分为不同的业务线，不同的业务线由不同的开发人员维护开发，模块化/组件化势在必行，一个模块代码一条业务线，模块内职责单一，模块间界限清晰，模块自身的复用更加方便快捷，模块化的好处很多，同时也存在一些需要改进的地方：例如编译速度的瓶颈越来越大、模块间怎么进行高效通信、模块怎么独立运行调试、模块的可插拨以及随意组合等等。


## 理想的模块化架构

![image](https://github.com/heimashi/module-service-manager/blob/master/imgs/modules.png)
- 可以参考此项目代码
- 如上图所示，模块后的代码从下往上看，可以分为三层：
	- 最下层是common层，为上层业务提供基础支持，该层不含有业务代码，可以再按功能细分为多个module，提供基础统一的服务。
	- 中间层是不同的业务线模块，module-a/module-b/module-c/...等，每个模块代表不同的业务模块，自身职责尽量单一，模块间界限清晰。向下以implementation形式依赖common基础库
	- 最上层是壳工程application，该工程没有业务代码，为所有模块提供一个组装的壳，向下以runtimeOnly的形式依赖所有的业务线，采用runtimeOnly的目的是使得壳工程尽量的职责单一，各模块间在编译期没有代码上的直接交互，需要在运行期才能产生交互，模块间更加独立和复用性更好。

## 模块化后的一些优化

采用像上图这样模块化改造之后，还可以进行更进一步的优化工作，例如支持模块的单独编译运行调试，优化代码编译速度等

#### 模块的单独编译运行

- 思路: **为每个模块增加一个Application的module**，参考此案例

	- 每个module都添加一个Application的工程然后再依赖对应的模块，可以将这样的模块聚合到一个目录下，例如modules-wrapper，在settings.gradle中添加：
	```Groovy
	include ':sample2:modules-wrapper:module-a-app',':sample2:modules-wrapper:module-b-app'
	```
	- 然后新建modules-wrapper目录，在该目录下建各模块的工程module-a-app，module-b-app...
	- module-a-app这样的工程都是application工程，提供模块启动的壳
- **总结**：上面两种思路都实现了模块的独立编译运行，虽然增加了不少工程项目，但是收缩到一个目录下后也还是较好管理。


#### 项目全量包打包速度优化

经过上面的模块单独编译改造，模块本身的打包速度得到很大提高，因为模块本身可以以Application形式编译，不需要依赖其他无关模块。但是如果要进行壳工程的编译，即全量模块的打包，对于大项目时间还是会很慢。

一种优化的思路是这样的：把模块的项目project形式依赖该为aar形式依赖，因为aar里已经是编译好的class代码了，减少了java编译为class和kotlin编译为class的过程。把不经常改变的模块打成aar，或者如果你在开发A模块，你就可以选择将所有除A模块以外的模块全部以aar形式进行依赖，或者你可以选择依赖你需要关心的模块，你不关心的模块可以不依赖。aar可以发布到公司内部私服里，还有一种办法是直接发布到本地maven库，即在本地建一个目录例如local_maven，将所有aar发布到该目录下，项目中再引入该本地maven即可。下面详细介绍通过脚本改造快捷的实现方案：

- 首先在[utils.gradle](https://github.com/heimashi/module-service-manager/blob/master/utils.gradle)的脚本中添加发布aar的task，可以快捷的在所有的project中注入发布的task避免重复的发布脚本
```Groovy
//add task about publishing aar to local maven
task publishLocalMaven {
    group = 'msm'
    description = 'publish aar to local maven'
    dependsOn project.path + ':clean'
    finalizedBy 'uploadArchives'

    doLast {
        apply plugin: 'maven'
        project.group = 'com.tiger.example.modules'
        if (project.name == "module-a") {//may changer version
            project.version = '1.0.0'
        } else {
            project.version = '1.0.0'
        }
        uploadArchives {
            repositories {
                mavenDeployer {
                    repository(url: uri(project.rootProject.rootDir.path + '/local_maven'))
                }
            }
        }

        uploadArchives.doFirst {
            println "START publish aar:" + project.name + " " + project.version
        }

        uploadArchives.doLast {
            println "End publish aar:" + project.name + " " + project.version
        }
    }
}

ext {
    //...
    compileByPropertyType = this.&compileByPropertyType
}
```

- 然后就可以在各模块中执行发布aar的脚本，就可以在local_maven目录下查看到已发布的aar
```Shell
./gradlew :sample2:module-a:publishLocalMaven

```

- 在项目的build.gradle中加入本地的maven地址
```Groovy
repositories {
        //...
        maven {
            url "$rootDir/local_maven"
        }
    }
```

- 在[utils.gradle](https://github.com/heimashi/module-service-manager/blob/master/utils.gradle)的脚本中添加根据变量控制编译方式的脚本
```Groovy
//返回0，1，2三种数值，默认返回0
def getCompileType(propertyString) {
    if (hasProperty(propertyString)) {
        try {
            def t = Integer.parseInt(project[propertyString])
            if (t == 1 || t == 2) {
                return t
            }
        } catch (Exception ignored) {
            return 0
        }
    }
    return 0
}

//根据property选择依赖方式，0采用project形式编译，1采用aar形式编译，2不编译
def runtimeOnlyByPropertyType(pro, modulePath, version = '1.0.0') {
    def moduleName
    if (modulePath.lastIndexOf(':') >= 0) {
        moduleName = modulePath.substring(modulePath.indexOf(':') + 1, modulePath.length())
    } else {
        moduleName = modulePath
    }
    def type = getCompileType(moduleName+'CompileType')
    if (type == 0) {
        dependencies.runtimeOnly pro.project(":$modulePath")
    } else if (type == 1) {
        dependencies.runtimeOnly "com.tiger.example.modules:$moduleName:$version@aar"
    }
}

ext {
    //...
    runtimeOnlyByPropertyType = this.&runtimeOnlyByPropertyType
}
```
- 在gradle.properties中就可以添加控制变量来控制项目是以aar形式/project形式/不依赖三种情况来编译了
```Groovy
module-aCompileType=1
module-bCompileType=0
```
上面这样的设置代表模块a采用aar形式依赖，模块b采用project形式依赖

- 最后在壳工程中就可以调用compileByPropertyType来进行依赖了,根据gradle.property中的变量来选择依赖方式：0采用project形式编译，1采用aar形式编译，2不编译
```Groovy
dependencies {
    runtimeOnlyByPropertyType(this, 'sample2:module-a')
    runtimeOnlyByPropertyType(this, 'sample2:module-b')
    //...
}
```

#### 项目中模块的可插拔以及自由组合

经过上面模块化脚本改造，得益于runtimeOnly的模块依赖，可以通过变量来控制模块是以aar形式/project形式/不依赖。通过设置是否依赖就可以实现模块的可插拔以及自由组合，将不需要的模块设置为2就不会参与编译了
```Groovy
module-aCompileType=2
module-bCompileType=1
```
例如上面这样的设置就代表不依赖模块a，模块b采用aar形式进行依赖，可以通过设置gradle.properties中的变量来一键管理各模块的依赖关系，快速实现了模块的可插拔以及自由组合。

除了在gradle.properties直接修改变量的值，也可以不修改任何代码，在执行gradle的编译task的时候可以添加参数，通过-P来设置参数：
```Shell
./gradlew :sample2:app:asembleDebug -P module-aCompileType=2
```

## 模块化通信

通过runtimeOnly形式依赖各模块后，最上层的app层是无法与模块直接通信调用了，另外模块间也是无法直接通信，但随着业务的发展难免会有交集需要通信调用：

使用[ARouter](https://github.com/alibaba/ARouter)
