import java.nio.file.Paths
import java.io.File

/**
 This a wrapper for safe script calls

 Basically this wrapper works by settings arguments as environment variables
 and passing those environment variables via delegate script to actual script
 (which is stored as temporary file)

 Parameters:
   label - step label
   script - script
   args - arguments to pass to script
   returnStdout - return std out
   returnStatus - return status
   encoding - encoding
*/
def call(opts) {
  def label = opts.label ?: 'safe_script.sh'
  def script = opts.script
  def args = opts.args ?: []
  def returnStatus = opts.returnStatus ?: false
  def returnStdout = opts.returnStdout ?: false
  def encoding = opts.encoding ?: 'UTF-8'

  def targetScriptPath = Paths.get(pwd(tmp: true), label).toString()

  withEnv(argumentsToEnvVariables(args)) {
    writeFile(file: targetScriptPath,
      text: script,
      encoding: 'utf-8')
    sh(script: delegateShellScript(targetScriptPath, args),
      label: label,
      returnStdout: returnStdout,
      returnStatus: returnStatus,
      encoding: encoding)
  }
}

def delegateShellScript(scriptPath, args) {
  return "chmod +x '${scriptPath}' && '${scriptPath}' ${argumentsToPlaceHolders(args)}"
}

def argumentsToPlaceHolders(args) {
  return args.indexed().collect { index, item ->
    return "\"\$${argName(index)}\""
  }.join(' ')
}

def argumentsToEnvVariables(args) {
  return args.indexed().collect { index, item ->
    return "${argName(index)}=${item}"
  }
}

def argName(index) {
  return "ARG_${index}"
}
