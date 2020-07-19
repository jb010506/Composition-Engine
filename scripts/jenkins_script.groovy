node{
    def webAppName = "patientMonitoringDB"

    def BPELDocumentGeneratorPath = "/mnt/database/selab/webapp_generator/bpel_document_generator"
    def BPELDocumentGeneratorJarName = "bpel_document_generator.jar"

    def UIComponentGeneratorPath = "/mnt/database/selab/webapp_generator/UI_component_generator"
    def UIComponentGeneratorJarName = "UI_component_generator.jar"

    def UIComponentMatchingPath = "/mnt/database/selab/webapp_generator/UI_component_matcher"
    def UIComponentMatchingJarName = "UI_component_matcher.jar"

    def UICompositeEnginePath = "/mnt/database/selab/webapp_generator/UI_composite_engine"
    def UICompositeEngineJarName = "ui_composite_engine.jar"

    // Storage
    def UICSourcePath = "/mnt/database/selab/storage/UIC_SRC/primeng"
    def UICDLPath = "/mnt/database/selab/storage/UICDL"
    def UICDLMatchedPath = "/mnt/database/selab/storage/UICDL_MATCHED"
    def WSDLPath = "/mnt/database/selab/storage/WSDL"
    def WebAppPath = "/mnt/database/selab/storage/webapp"


    dir(BPELDocumentGeneratorPath){
        stage('generate_BPEL_document'){
            sh "java -jar build/libs/${BPELDocumentGeneratorJarName} ${webAppName}"
        }
    }

    dir(UIComponentGeneratorPath){
        stage('generate_UI_components'){
            sh "java -jar build/libs/${UIComponentGeneratorJarName} ${UICSourcePath} ${UICDLPath}"
        }
    }

    dir(UIComponentMatchingPath) {
        stage('match_UI_components') {
            sh "java -jar build/libs/${UIComponentMatchingJarName} ${UICDLPath} ${WSDLPath} ${UICDLMatchedPath}"
        }
    }

    dir(UICompositeEnginePath){
        stage('generate_web_app'){
            sh "java -jar build/libs/${UICompositeEngineJarName} ${webAppName}.bpel ${webAppName}.wsdl ./output"
        }
    }

    dir("${UICompositeEnginePath}/output/${webAppName}"){
        stage('compile_web_app'){
            sh "npm install"
            sh "ng build"
        }
    }

    dir("${UICompositeEnginePath}/output/${webAppName}"){
        stage('dynamic load webapp to Response Server'){
            sh "rm -rf ${WebAppPath}/${webAppName}"
            sh "mv dist/${webAppName} ${WebAppPath}/${webAppName}"
            sh "curl -X POST http://localhost:8088/api/webresource/${webAppName}"
        }

        stage('dynamic load service components to API Server'){
            sh "curl -X POST http://140.112.90.144:7122/api/webresources"
            sh "curl http://140.112.90.144:7122/registerClassPath?path=/home/selab/service_generator_new/classes"
            sh "curl http://140.112.90.144:7122/registerDependencyPath?path=/home/selab/service_generator_new/dependency"
            sh "curl http://140.112.90.144:7122/registerExternalClassPath?path=/home/selab/service_generator_new/classes/externalLibs"
        }
    }

    stage('clean temp code'){
        dir(UICompositeEnginePath) {
            sh "rm -rf ./output/${webAppName}"
        }
    }

}