cd C:\Users\Sean\Ideaprojects\filetransfer2

plink -i sean/privateKey.ppk root@192.168.40.129 -m commands\plink-commands.txt

psftp -i sean/privateKey.ppk -b commands\psftp-commands.txt root@192.168.40.129 

java -jar out/artifacts/fileTransfer2_jar/fileTransfer2.jar

pause