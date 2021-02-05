# ModelBox
ModelBox is a desktop application that provides an intuitive way to store and preview 3D models.

# IntelliJ IDE Setup

## Assumptions:
- You have a version of the JetBrains IntelliJ Idea IDE installed (either Ultimate or CE)
- You have a OpenJDK SDK of version 15.0.2 or greater installed on your OS.
  - The latest OpenJDK version can be found here: https://jdk.java.net/15/
  
## Get started developing by executing the following commands:
Note: Be sure to first navigate to wherever you'd like to store the repo locally.

#### Via GitHub CLI
Note: If using this method, be sure to download the CLI binaries via the following link: https://cli.github.com

```
gh auth login
gh repo clone mcoryell/ModelBox
```

#### Via Git CLI

```
git clone https://github.com/mcoryell/ModelBox.git
cd ModelBox
```

### Editing in IntelliJ Idea

1. Open IntelliJ IDE
2. File -> Open... -> Navigate to wherever you stored the repo and open the entire folder

### Configure IntelliJ Idea to use the OpenJDK 15.0.2 SDK on this project (previously downloaded)

1. File -> Project Structure... -> Project -> Project SDK -> Set the JDK to be equal to the JDK previously mentioned.

### Build dependencies using Gradle

1. Navigate to the Gradle Tab in IntelliJ
2. ModelBox -> Tasks -> build -> Right click and run the build process


# You're all set to develop!

To learn more about launching IntelliJ projects from the command line, visit: https://www.jetbrains.com/help/idea/working-with-the-ide-features-from-command-line.html
