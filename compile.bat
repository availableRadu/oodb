@echo off

javac -d ServerSide CustomClass.java
javac ServerSide/*.java

javac -d UserSide CustomClass.java
javac UserSide/*.java