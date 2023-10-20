indexFile = new File(basedir, "target/custom-site/custom-aspectj-report/index.html")
assert indexFile.exists()
clazzFile = new File(basedir, "target/custom-site/custom-aspectj-report/Clazz.html")
assert clazzFile.getText().contains("Advised&nbsp;by:")
