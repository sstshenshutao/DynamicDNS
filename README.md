# DynamicDNS
A very very light-weight Java DynamicDNS, easy to use.



1.prepare Dynamic DNS

	1.1 What is Dynamic DNS? 
		https://www.namecheap.com/support/knowledgebase/article.aspx/33/51/what-is-dynamic-dns

	1.2 How do I start using Dynamic DNS?
		https://www.namecheap.com/support/knowledgebase/article.aspx/36/11/how-do-i-start-using-dynamic-dns

	1.3 Change ConfigFile "default.Properties" (password is "Dynamic DNS Password")


2.download "SSTDynamicDNS" from github

	2.1 change Filedir
		cd DynamicDNS

	2.2 compile
		javac -cp ./lib/commons-logging-1.2.jar:./lib/httpcore-4.4.9.jar:./lib/commons-codec-1.10.jar:./lib/httpclient-4.5.5.jar dynamicLoop/DynamicLoop.java
	
	2.3 run
		java -cp ./lib/commons-logging-1.2.jar:./lib/httpcore-4.4.9.jar:./lib/commons-codec-1.10.jar:./lib/httpclient-4.5.5.jar: dynamicLoop.DynamicLoop

3.if the log in 2 is right, use nohup to run it backgrond

Finished by Shen Shutao, 2.Semester Bachelor.