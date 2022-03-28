notExistingFiles = [
    "classes/builddef.lst",
    "test-classes/builddef.lst",
    "classes/builddef-test.lst",
    "test-classes/builddef-test.lst"
]
existingFiles = [
    "aspectj-build/builddef.lst",
    "aspectj-build/builddef-test.lst"
]

for (file in notExistingFiles)
    assert !new File("$basedir/target/$file").exists()
for (file in existingFiles)
    assert new File("$basedir/target/$file").exists()
