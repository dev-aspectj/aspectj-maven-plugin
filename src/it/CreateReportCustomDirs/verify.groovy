indexFile = new File(basedir, "no-target/custom-aspectj-report/index.html")
assert indexFile.exists()
clazzFile = new File(basedir, "no-target/custom-aspectj-report/Clazz.html")
assert clazzFile.getText().contains("Advised&nbsp;by:")
