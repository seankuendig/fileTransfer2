cd C:\Users\Sean\Ideaprojects\filetransfer2

plink -agent root@192.168.40.129 -m commands\plink-commands.txt

psftp -agent -b commands\psftp-commands.txt root@192.168.40.129 