# GitHub Actions Workflows

This directory contains GitHub Actions workflows for the DottySpike project.

## Available Workflows

### CI (`ci.yml`)
- **Trigger**: Push to main/scala branches, Pull Requests
- **Jobs**:
  - **test**: Builds and tests the project on multiple Java versions (17, 21)
  - **format-check**: Validates code formatting with scalafmt
  - **security-scan**: Runs dependency vulnerability checks

### Code Quality (`code-quality.yml`)
- **Trigger**: Push to main/scala branches, Pull Requests
- **Jobs**:
  - **format**: Auto-formats code on PRs using scalafmt
  - **scalafix**: Auto-fixes code style issues using scalafix
  - **sonar**: Runs SonarCloud analysis (if SONAR_TOKEN is configured)

### Release (`release.yml`)
- **Trigger**: Git tags starting with 'v'
- **Jobs**:
  - **release**: Creates GitHub releases, builds distributions, publishes to Maven Central (if configured)

### Weekly Maintenance (`weekly.yml`)
- **Trigger**: Weekly cron job, manual dispatch
- **Jobs**:
  - **update-dependencies**: Checks for dependency updates, creates PRs if needed
  - **security-scan**: Comprehensive security scanning

### Documentation (`docs.yml`)
- **Trigger**: Changes to documentation files
- **Jobs**:
  - **build-docs**: Generates Scaladoc, validates documentation, deploys to GitHub Pages

## Required Secrets

To enable all features, configure these repository secrets:

- **SONAR_TOKEN**: For SonarCloud analysis
- **MAVEN_CENTRAL_TOKEN**: For publishing to Maven Central
- **MAVEN_CENTRAL_USERNAME**: Maven Central username
- **GPG_PRIVATE_KEY**: GPG key for signing releases
- **GPG_PASSWORD**: GPG key password

## Configuration

- Java versions tested: 17, 21
- Uses Gradle cache for faster builds
- Parallel builds enabled via Gradle configuration
- Caches Gradle dependencies between runs