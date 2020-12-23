#  Content Java Archive

CJAR's are an extention of Java Archive (.jar) files, which are used to provide content for the OPEX engine to load for use within games.

CJAR's can be signed, distributed seperately, and used to add almost any content to the engine including visuals, audio, gui, and classes.
OPEX Visual provides a CJAR managing tool which can be used to guide you in creating, populating, signing and testing CJAR's.

Future plans may use JDK directly to compress and sign automatically.

## Create a CJAR
Using OPEX Visual, create a cjar template. The manager will prompt for the archive to be manually populated.
Use this time to place all content files into the archive.

When ready, click continue on OPEX Visual. It will validate the file structure, and warn of file discrepencies before generating
CJAR metadata. Once this has completed, it will guide you through using JDK to turn the file structure into a CJAR, and optionally signing it.

### CVF

To create an archive with from a valid folder structure, use your terminal to `cd` into your cjar root `/exaplecjar/`, and execute

```
jar -cvf [location] [cjar root]
jar -cvf ../example.cjar ./
```

## signing
The process of CJAR signing is the same as that used for signing typical JAR files. To continue, you must have a valid install of Java's Development Kit.



### Keystore directory
When signing, it's ideal to create a directory for key stores and the like. This directory SHOULD NOT BE IN YOUR GIT if it is public, or unprotected.
Following the Public Key Cryptogoraphy model, you may issue a public key if you wish others to be able to create and sign cjar's
that may be loaded into your game.

If you wish to keep content production private, then keep your keys securely to yourself.

That said, the JDK method below provides a password protected key store. This key store is secure and may be included in public version control,
with access to the keys stored within controlled by the distribution, or lack there of, the password to the key store.

### Create a keystore
To sign a cjar, you need to determine the following values:

```
value                     Default / Example         note

Name of game            : myGame                    Will be the name of the key inside the keystore database.
Key generation algorythm: RSA                       RSA is addequate
Location of keystore    : /mygame/keys/security.jks    Use your keystore directory
size of the key         : 2048                      2048 is typical
seconds of validity     :                           Determines the length of time which the key will be valid in seconds. Blank = 90 days, completly omit the -validity option.
                                                                                                                          Theoretically infinate (3.3 million years) = 106751991167300
Legal Name              : John Doe                  Required, your legal name.
Organizational unit     :                           Optional, unit within your organisation i.e team / department.
organisation            :                           Your legal organisation name. If none, your name.
Locality                : Leicester                 City, town, etc
Province                : Leicestershire            State, county, etc
Country code            : UK, FR, DE, US, IN, etc.. Two letter country code.

```
Once you've determined these values, you can use a terminal with a PWD inside your keystore location (`cd /mygame/security/`) to generate a keystore.
We'll be using keytool from Java's development key; thus you must have a valid JDK install. Test yours with `java -version`, you should see the version of your java installation.
The instruction options format you need is :
```
 keytool -genkey -alias [GameName] -keyalg [Algorythm] -keystore ./store.jks -keysize 2048 [-validity [Validity]]
 ```
 
 Once you've entered this, you'll be prompted for data used to generate the key, followed by a yes/no confirmation.
 If sucessful you should now see a `store.jks` file with `ls (unix)` OR `dir (windows)`

### OPEX Engine Signature
OPEX's key store is available within the Engine module, in the key's directory. This store contains master keys for signing cjar's for the engine. With access to this key, any game using OPEX could be exploited, even if it's aimed to be closed, therefore it's encryption pass is private; content for the engine directly may not be produced using a cjar, but with the open source nature additons to the engine can be made at development. 
