cd C:\Users\Sean\Ideaprojects\filetransfer

plink -agent root@192.168.17.129 -m commands\plink-commands.txt

psftp -agent -b commands\psftp-commands.txt root@192.168.17.129 