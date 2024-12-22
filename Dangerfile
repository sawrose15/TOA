message "Thanks @#{github.pr_author}!"

if github.pr_body.length == 0
    fail "Please provide a summary in the Pull Request description."
end

if git.lines_of_code > 500
    warn "Please consider breaking up this pull request."
end

if git.deletions > git.insertions
    message  "🎉 Code Cleanup!"
end


# Notify of outdated dependencies
dependencyReportsFile = "app/build/dependencyUpdates/report.txt"
dependencyUpdatesHeader = "The following dependencies have later milestone versions:"

hasDependencyUpdatesHeader = File.readlines(dependencyReportsFile).grep(/#{dependencyUpdatesHeader}/).any?

if hasDependencyUpdatesHeader
  file = File.open(dependencyReportsFile, "rb").read
  index = file.index(dependencyUpdatesHeader)
  message file.slice(index..-1)
end