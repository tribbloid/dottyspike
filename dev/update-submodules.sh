#!/usr/bin/env bash

git config --global credential.helper store
git pull --rebase --autostash

# this is required if any git module has been initialised to another remote repo
git submodule sync && \
git submodule foreach git fetch && \
git submodule foreach git reset --hard && \
git submodule update --init --force