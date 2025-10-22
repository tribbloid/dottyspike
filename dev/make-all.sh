#!/usr/bin/env bash


CRDIR="$(cd "`dirname "$0"`"; pwd)"
FWDIR="$(cd "`dirname "$0"`"/..; pwd)"

mkdir -p ${FWDIR}/logs
mkdir -p ${FWDIR}/logs/dependencyTree
mkdir -p ${FWDIR}/logs/taskTree

source "${CRDIR}/.shared.sh"

${FWDIR}/gradlew clean

DATE=$(date +%Y_%m_%d_%H_%M_%S)

DEP_PREV_LOG=${FWDIR}/logs/dependencyTree/_prev.log
${CRDIR}/graphOf/dependency.sh "${@}" > ${DEP_PREV_LOG}
cp ${DEP_PREV_LOG} ${FWDIR}/logs/dependencyTree/"$DATE".log

TASK_PREV_LOG=${FWDIR}/logs/taskTree/_prev.log
${CRDIR}/graphOf/task.sh "${@}" > ${TASK_PREV_LOG}
cp ${TASK_PREV_LOG} ${FWDIR}/logs/taskTree/"$DATE".log

${FWDIR}/gradlew classes testClasses assemble "${@}"
