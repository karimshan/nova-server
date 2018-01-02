@echo off
title Drop Generator
java -Xmx250m -cp out;data/libraries/*; org.nova.kshan.tools.DropGenerator
pause