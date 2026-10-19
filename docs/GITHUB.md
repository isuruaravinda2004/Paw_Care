# Git history and publishing to your own GitHub account

The project was initialized with Git before implementation. The bundle preserves real commits from this generation session and nine merged feature branches. The author is `PawCare Project Generator`; do not present those generated commits as your personal lecture-by-lecture development record. Add your own changes and explain them during your viva.

## Restore the supplied history (recommended)

From the extracted ZIP's outer folder:

```bash
git clone PawCare-history.bundle PawCare-Git
cd PawCare-Git
git log --oneline --graph --all
git branch -a
```

The Git bundle contains source and documentation history, including screenshots, but not the prebuilt JAR. You can copy the ZIP's `PawCare/dist` folder into the cloned project or build it yourself. All nine `.form` files are tracked.

Set your identity **for your own future commits**:

```bash
git config user.name "Your Name"
git config user.email "your-verified-email@example.com"
```

Create a new empty repository in your GitHub account called:
`Pet-Grooming-Center-Management-System`. Choose the visibility required by your lecturer. Do not initialize the remote with a separate README if you want to push this existing history unchanged.

Replace YOUR_USERNAME below with your own account name:

```bash
git remote set-url origin https://github.com/YOUR_USERNAME/Pet-Grooming-Center-Management-System.git
git push -u origin main
```

GitHub may prompt you to sign in through Git Credential Manager. Do not put tokens or passwords in source files. Open the repository in your browser and copy its actual URL into `docs/SUBMISSION.md` and your coursework submission. No remote URL is claimed in this generated project.

The branch commits remain in main's merge history. To publish a named feature branch as well, restore its local branch from the cloned remote-tracking branch, then push it:

```bash
git branch feature/customer-management origin/feature/customer-management
git push origin feature/customer-management
```

Repeat for any other supplied feature branch you want to show. `git branch -a` shows the exact available names.

## Start a new personal history instead

The ordinary `PawCare` source folder excludes `.git`. If you choose not to restore the bundle:

```bash
git init -b main
git config user.name "Your Name"
git config user.email "your-verified-email@example.com"
git status
git add .
git commit -m "Import PawCare starter project"
git remote add origin https://github.com/YOUR_USERNAME/Pet-Grooming-Center-Management-System.git
git push -u origin main
```

This honestly records an imported starter project. Develop each change afterward on your own feature branch.

## Everyday feature workflow

```bash
git checkout main
git checkout -b feature/my-improvement
# Make and test your change in NetBeans.
git status
git add src/main/java/com/pawcare/view/CustomerFrame.java src/main/java/com/pawcare/view/CustomerFrame.form
git diff --staged
git commit -m "Improve customer address validation"
git checkout main
git merge --no-ff feature/my-improvement
git push origin main
```

`git branch` lists local branches. `git checkout` changes the working branch. `git merge` brings the completed feature into main. Commit a JFrame's `.java` and `.form` together. If you get a merge conflict, resolve both files coherently, run a build and open Design before committing.

## Files that should remain local

`config.properties` contains your database account settings and is ignored. `target/` and `dist/` are build outputs. Publish a runnable ZIP/JAR through a GitHub Release if your course needs downloadable binaries; keep the source repository focused on code and documentation. The SQL contains only obvious demo data.
