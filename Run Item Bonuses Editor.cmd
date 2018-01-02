@echo off
title Item Bonuses Editor
java -Xmx250m -cp out;data/libraries/*; org.nova.kshan.tools.itembonuseditor.BonusesEditor
pause