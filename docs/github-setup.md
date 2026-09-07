# GitHub Repository Setup

Follow these steps to create the GitHub repo and push this project.

## Option A — GitHub website (no CLI)

1. Go to https://github.com/new
2. Repository name: `rasoi-royale`
3. Description: `Indian recipe app with multiplayer "What Should We Cook?" game`
4. Choose **Private** or **Public**
5. Do **not** initialize with README (this project already has one)
6. Click **Create repository**

Then in Terminal, from the project folder:

```bash
cd "/Users/nehatiwari/Documents/recipe app"
git init -b main
git add -A
git commit -m "Initial commit: Rasoi Royale monorepo foundation (Phase 1)"
git remote add origin https://github.com/YOUR_USERNAME/rasoi-royale.git
git push -u origin main
```

Replace `YOUR_USERNAME` with your GitHub username.

## Option B — GitHub CLI

Install GitHub CLI:

```bash
brew install gh
gh auth login
```

Create and push:

```bash
cd "/Users/nehatiwari/Documents/recipe app"
git init -b main
git add -A
git commit -m "Initial commit: Rasoi Royale monorepo foundation (Phase 1)"
gh repo create rasoi-royale --private --source=. --remote=origin --push
```

Use `--public` instead of `--private` if you want a public repo.

## Verify

- Open your repo on GitHub
- Confirm folders: `android/`, `backend/`, `data/`, `docs/`
- GitHub Actions should run on push (backend tests; Android needs JDK in CI)

## Branch protection (recommended)

Settings → Branches → Add rule for `main`:

- Require pull request reviews
- Require status checks (CI) to pass
