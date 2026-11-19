# CityDirectory
Консольная программа для чтения справочников городов из CSV и XML.

В PowerShell из папки проекта:
```powershell
if (!(Test-Path out)) { New-Item -ItemType Directory out | Out-Null }
javac -encoding UTF-8 -d out src/citydirectory/*.java
java -Dfile.encoding=UTF-8 -cp out citydirectory.Main
```
Введите путь к файлу, например `data\address.csv` или `data\address.xml`.
Для завершения введите `exit`.

CSV: разделитель `;`, столбцы `city;street;house;floor`.
XML: элементы `item` с атрибутами `city`, `street`, `house`, `floor`.
