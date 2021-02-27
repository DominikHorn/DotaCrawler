#!/bin/bash

PROJECT_DIR=$(dirname "$0")

# Create data/ directory for postgresql data
mkdir ${PROJECT_DIR}/data

# Create .env file
TEMPLATE_FILE=template.env
OUTPUT_FILE=.env

if [[ -f "${OUTPUT_FILE}" ]]; then
  echo "File \"${OUTPUT_FILE}\" exists already. Please remove it manually before running this script again."
  exit -1
fi

while read -u 10 KEY || [[ -n $KEY ]]; do
  read -p "${KEY}" VALUE
  echo "${KEY}${VALUE}" >> ${OUTPUT_FILE}
done 10<"${PROJECT_DIR}/${TEMPLATE_FILE}"
