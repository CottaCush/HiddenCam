#!/bin/bash

ENCRYPTION_KEY=$1

if [ -n "$ENCRYPTION_KEY" ]; then
  # Decrypt the keyring file
  openssl aes-256-cbc -md sha256 -d -in signing/keyring.gpg.aes -out signing/keyring.gpg -k "${ENCRYPTION_KEY}"
else
  echo "ENCRYPTION_KEY should not be empty"
fi