#!/bin/sh
# Usage: ./release.sh <version>
# Example: ./release.sh 4.4.11-0
set -e

RELEASE_VERSION="$1"

if [ -z "$RELEASE_VERSION" ]; then
    echo "Usage: $0 <version> (e.g. 4.4.11-0)" >&2
    exit 1
fi

# Derive the base version (strip release suffix, e.g. 4.4.11-0 -> 4.4.11)
BASE_VERSION="${RELEASE_VERSION%-*}"
MAJOR=$(echo "$BASE_VERSION" | cut -d. -f1)
MINOR=$(echo "$BASE_VERSION" | cut -d. -f2)
PATCH=$(echo "$BASE_VERSION" | cut -d. -f3)
NEXT_PATCH=$((PATCH + 1))
NEXT_SNAPSHOT="${MAJOR}.${MINOR}.${NEXT_PATCH}-SNAPSHOT"

CURRENT_SNAPSHOT=$(grep -m1 '<version>' pom.xml | sed 's/.*<version>\(.*\)<\/version>/\1/' | tr -d ' ')
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)

echo "Current version : $CURRENT_SNAPSHOT"
echo "Release version : $RELEASE_VERSION"
echo "GN image        : geonetwork:${BASE_VERSION}"
echo "Next snapshot   : $NEXT_SNAPSHOT"
echo ""

# Step 1: bump to release version
find . -name "pom.xml" | grep -v target | sort | xargs sed -i '' "s/${CURRENT_SNAPSHOT}/${RELEASE_VERSION}/g"
sed -i '' "s/TAG=${CURRENT_SNAPSHOT}/TAG=${RELEASE_VERSION}/" .env

git add pom.xml .env $(find . -path ./target -prune -o -name "pom.xml" -print | grep -v "^\./pom.xml")
git commit -m "Release of GN ${RELEASE_VERSION}"

# Step 2: update legacy GeoNetwork image in docker-compose.yml
PREV_GN_IMAGE=$(grep 'image: geonetwork:' docker-compose.yml | sed 's/.*geonetwork://' | tr -d ' ')
sed -i '' "s/image: geonetwork:${PREV_GN_IMAGE}/image: geonetwork:${BASE_VERSION}/" docker-compose.yml
git add docker-compose.yml
git commit -m "Update GN version"

# Step 3: push commits and tag
git push origin "${CURRENT_BRANCH}"
git tag -a "${RELEASE_VERSION}" -m "Release v${RELEASE_VERSION}"
git push origin "${RELEASE_VERSION}"

# Step 4: create GitHub release
gh release create "${RELEASE_VERSION}" --title "${RELEASE_VERSION}" --notes "Release ${RELEASE_VERSION}"

# Step 5: bump to next snapshot
find . -name "pom.xml" | grep -v target | sort | xargs sed -i '' "s/${RELEASE_VERSION}/${NEXT_SNAPSHOT}/g"
sed -i '' "s/TAG=${RELEASE_VERSION}/TAG=${NEXT_SNAPSHOT}/" .env

git add pom.xml .env $(find . -path ./target -prune -o -name "pom.xml" -print | grep -v "^\./pom.xml")
git commit -m "Update version to ${NEXT_SNAPSHOT}"
git push origin "${CURRENT_BRANCH}"

echo ""
echo "Released ${RELEASE_VERSION}: https://github.com/geonetwork/geonetwork-microservices/releases/tag/${RELEASE_VERSION}"
echo "Next development version: ${NEXT_SNAPSHOT}"
