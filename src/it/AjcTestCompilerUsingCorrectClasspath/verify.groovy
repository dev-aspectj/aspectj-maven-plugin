file = new File(basedir, "target/test-classes/builddef.lst")
assert file.exists()
assert file.readLines().get(6) == '-classpath'
assert file.readLines().get(7).contains('junit')
