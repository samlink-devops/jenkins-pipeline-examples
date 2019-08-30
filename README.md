# jenkins-pipeline-examples
Code fragments for Jenkins pipelines

## safeSh step (vars/safeSh.groovy)
safeSh step is used to pass untrusted input to shell scripts.

### Implementation
Implementation uses a dynamically generated delegate script, that reads
variables from environment variables and passes them to script as command
line arguments

### Parameters
Step uses same parameters as sh step:

   label - step label
   script - script
   args - arguments to pass to script
   returnStdout - return std out
   returnStatus - return status
   encoding - encoding

### Usage

    safeSh(script: 'mv "$1" "$2"',
      args: [from, to])
