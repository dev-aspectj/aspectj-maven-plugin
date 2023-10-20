indexFile = new File(basedir, "target/aspectj-report/index.html")
assert indexFile.exists()
clazzFile = new File(basedir, "target/aspectj-report/Clazz.html")
assert clazzFile.getText().contains("Advised&nbsp;by:")
