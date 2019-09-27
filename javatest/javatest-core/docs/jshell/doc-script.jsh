var results = runTest(() -> that(true, "JavaTest works in the shell!"))
var results2 = runTest(() -> that(1 + 1 == 3, "One add One is Three"))
var allResults = results.combine(results2)
allResults.succeeded
allResults.failureCount
allResults.allResults().collect(java.util.stream.Collectors.toList())
/exit
