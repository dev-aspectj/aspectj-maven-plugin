file = new File(basedir, "build.log")
assert file.exists()
assert file.getText().contains("compiler errors:")