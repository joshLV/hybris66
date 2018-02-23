# NPMANCILLARY EXTENSION - NPM 


##  How to regenerate the _npm-shrinkwrap.json_ file

The _npm-shrinkwrap.json_ should be automatically updated if you perform operations using *npm* command with the _--save_ argument, e.g *npm install bootstrap --save*. For any other case, when you want to regenerate the _npm-shrinkwrap.json_ file, follow these steps: 

1. From the root of the extension, type
```
$ ant npmpackagelock
``` 

2. The command above will update the NPM version before generating the _npm-shrinkwrap.json_ file, and therefore it has an impact on the binary folder under ./npm/node. You must revert the history to revert NPM to the previous version. 

One way of doing it is to rewrite the history. Remember to commit the _npm-shrinkwrap.json_ file first!
```
$ git reset --hard
```

If there are new files after you get the _'git status'_, then remove those files manually. 

## Windows Users

If you experience problems during a regular ```ant npminstall```, install NodeJS manually using the [node-v6.9.4-x64.msi] installation file and rewrite the contents of /node/node-v6.9.4-win-x64 with it. 

Normally NodeJS for Windows will install NodeJS under _C:\Program Files\nodejs_.    
