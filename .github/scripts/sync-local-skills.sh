#!/usr/bin/env bash
set -euo pipefail

# Generates the tool-specific copies of this repository's own agent skills.
#
#   source       .agents/skills/<name>/SKILL.md        tracked, edit this
#   Claude Code  .claude/skills/<name>/SKILL.md        generated, gitignored
#   Copilot CLI  .github/agents/<name>.agent.md        generated, gitignored
#
# This mirrors what .ai-resources/hooks/git/post-checkout does for team resources:
# one source, tool-specific copies, copies excluded from the repository. The
# difference is where the source lives — team skills come from the submodule,
# these are local to certificate-service because this is where certificate types
# are implemented.
#
# Run directly, or let Gradle run it: `./gradlew syncLocalSkills` (wired into build).
#
# Usage:
#   .github/scripts/sync-local-skills.sh [--check]
#
#   --check   verify the copies are current without writing; non-zero if stale.

REPO_ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
SRC="${REPO_ROOT}/.agents/skills"
CLAUDE_DST="${REPO_ROOT}/.claude/skills"
COPILOT_DST="${REPO_ROOT}/.github/agents"

CHECK_ONLY=0
if [ "${1:-}" = "--check" ]; then
  CHECK_ONLY=1
fi

if [ ! -d "$SRC" ]; then
  echo "local-skills: no ${SRC}, nothing to sync"
  exit 0
fi

stale=0
count=0

for skill_dir in "$SRC"/*/; do
  [ -d "$skill_dir" ] || continue
  name="$(basename "$skill_dir")"
  source_file="${skill_dir}SKILL.md"
  [ -f "$source_file" ] || continue

  claude_file="${CLAUDE_DST}/${name}/SKILL.md"
  copilot_file="${COPILOT_DST}/${name}.agent.md"

  if [ "$CHECK_ONLY" -eq 1 ]; then
    for generated in "$claude_file" "$copilot_file"; do
      if [ ! -f "$generated" ] || ! cmp -s "$source_file" "$generated"; then
        echo "local-skills: stale or missing -> ${generated#"${REPO_ROOT}/"}"
        stale=1
      fi
    done
  else
    mkdir -p "${CLAUDE_DST}/${name}" "$COPILOT_DST"
    cp "$source_file" "$claude_file"
    cp "$source_file" "$copilot_file"
  fi

  count=$((count + 1))
done

if [ "$CHECK_ONLY" -eq 1 ]; then
  if [ "$stale" -eq 1 ]; then
    echo ""
    echo "Run .github/scripts/sync-local-skills.sh (or ./gradlew syncLocalSkills)."
    exit 1
  fi
  echo "local-skills: ${count} skills up to date"
  exit 0
fi

echo "local-skills: synced ${count} skills -> .claude/skills and .github/agents"
