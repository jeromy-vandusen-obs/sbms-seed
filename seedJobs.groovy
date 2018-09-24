def appProjects = [
  'sbms-admin',
  'sbms-discovery',
  'sbms-web',
  'sbms-greeting',
]

def infraProjects = [
  'sbms-jenkins',
  'sbms-test'
]

def pipelineProjects = [
  'sbms-pipeline-dev',
  'sbms-pipeline-dev-down',
  'sbms-pipeline-test',
  'sbms-pipeline-test-down',
  'sbms-pipeline-uat',
  'sbms-pipeline-uat-down',
  'sbms-pipeline-uat-passed',
  'sbms-pipeline-prod',
  'sbms-pipeline-prod-down'
]

appProjects.each { projectName ->
  createPipelineJob(projectName, projectName, 'Jenkinsfile')
}

infraProjects.each { projectName ->
  createPipelineJob(projectName, projectName, 'Jenkinsfile')
}

pipelineProjects.each { projectName ->
  createPipelineJob(projectName, 'sbms-pipeline', "${projectName.substring(14)}/Jenkinsfile")
}

createListView('SBMS CI', '(sbms\\-)(?!seed)(?!pipeline\\-)(?!test).+')
createListView('SBMS Pipeline - Dev', '(sbms\\-pipeline\\-)(?!prod\\-?)(?!uat\\-?).+')
createListView('SBMS Test', '(sbms\\-test)')
createListView('SBMS Pipeline - UAT', '(sbms\\-pipeline\\-uat\\-?).*')
createListView('SBMS Pipeline - Prod', '(sbms\\-pipeline\\-prod\\-?).*')

def createPipelineJob(jobName, projectName, script) {
  pipelineJob(jobName) {
    definition {
      cpsScm {
        lightweight(false)
        scm {
          git {
            remote {
              url("https://github.com/jeromy-vandusen-obs/${projectName}.git")
            }
            branch('*/master')
            extensions{}
          }
        }
        scriptPath(script)
      }
    }
  }
}

def createListView(viewName, filter) {
  listView(viewName) {
    columns {
      status()
      weather()
      name()
      lastSuccess()
      lastFailure()
      lastDuration()
      buildButton()
    }
    jobs {
      regex(filter)
    }
  }
}
