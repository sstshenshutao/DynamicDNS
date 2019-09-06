# DynamicDNS
A very very light-weight Java DynamicDNS for the NSprovider "namecheap", easy to use.



1.prepare Dynamic DNS

	1.1 What is Dynamic DNS? 
		https://www.namecheap.com/support/knowledgebase/article.aspx/33/51/what-is-dynamic-dns

	1.2 How do I start using Dynamic DNS?
		https://www.namecheap.com/support/knowledgebase/article.aspx/36/11/how-do-i-start-using-dynamic-dns

	1.3 Change ConfigFile "default.Properties" (the host is what you want to change, and the password is "Dynamic DNS Password")


2.download "DynamicDNS" from github

	2.1 install gradle

	2.2 gradle build & gradle jar
	
	2.3 java -jar ./ddns-1.0-SNAPSHOT

	or 2 directly use the already compiled jar in the dir 'ddns-run'

3.if the log in 2 is right, use nohup to run it backgrond

Finished by Shen Shutao, 4.Semester Bachelor in TUM.