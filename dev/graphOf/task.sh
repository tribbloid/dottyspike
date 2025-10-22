#!/usr/bin/env bash

# TODO: replaced with compile + test_only?

CRDIR="$(cd "`dirname "$0"`"; pwd)"

source "${CRDIR}/../.shared.sh"

${CRDIR}/../../gradlew -q build taskTree -Dorg.gradle.parallel=false "${@}"
