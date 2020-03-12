# GENERATED GCP SHARED ENVIRONMENT VARIABLES. DO NOT EDIT.
# The source file is env.sh in the clever-circlet-125504 repository.
# If it is changed, re-run 'make sync-env' in clever-circlet-125504 repository.
#
# GCLOUD Configuration
export GOOGLE_CLOUD_PROJECT=clever-circlet-125504
export GCP_PROJECT_ID=${GOOGLE_CLOUD_PROJECT}
export GCP_PROJECT_NAME=som-irt-services
export GCP_VPC_NAME=services
export GCP_ENVIRONMENT=default
export GCP_CONFIGURATION=${GCP_PROJECT_NAME}-${GCP_ENVIRONMENT}
export GCP_REGION=us-west1
export GCP_ZONE=${GCP_REGION}-a

# For StackDriver Loggin and Monitoring
export GCP_MONITORING_PROJECT_ID=${GCP_PROJECT_ID}
export GCP_SLACK_CHANNEL=med-svc-alerts

# Force gcloud auth with user credentials
export GCP_USER_AUTH=true

# PS Cloud Framework (Scripts, shared config, etc.)
export FRAMEWORK_DIR=${HOME}/bin/ps-cloud-framework
export FRAMEWORK_BUCKET=ps-cloud-framework
export SCRIPTS_DIR=${FRAMEWORK_DIR}/scripts

# Vault and secrets configuration
export VAULT_ADDR=https://vault.med.stanford.edu
export VAULT_AUTH_METHOD=ldap
export VAULT_CACHE=${HOME}/.vault-local
export SEC_PATH=secret/projects/${GCP_PROJECT_NAME}
export GCP_KEY_PATH=${SEC_PATH}/common/gcp-provision
export GCP_KEY_FILE=${VAULT_CACHE}/${GCP_KEY_PATH}

# Drone env
export DRONE_SERVER=https://drone.med.stanford.edu
export DRONE_TOKEN_FILE=${HOME}/.drone-token

# SONARQUBE env
export SONARQUBE_SERVER=https://sonarqube.med.stanford.edu
export SONARQUBE_TONKE_FILE=${HOME}/.sonar-token

# GCP artifacts bucket
export GCP_ARTIFACTS_BUCKET=${GCP_PROJECT_NAME}-artifacts

# Required by Terraform
export GCP_INFRASTRUCTURE_BUCKET=${GCP_PROJECT_ID}-infrastructure
export TF_BACKEND_PREFIX=terraform/${GCP_PROJECT_ID}/${GCP_ENVIRONMENT}/state
export TF_KEY_VAULT_PATH=${SEC_PATH}/common/tf-key
# Upgrade to terraform v0.12
export TF_CMD=/usr/local/bin/terraform

# GCP Networking
export GCP_NETWORK=${GCP_VPC_NAME}
export GCP_DNS_DOMAIN=med.stanford.edu
export GCP_NAT_TAGS=nat-stanford

# For external-dns and cert-manager/ACME
export EXTERNAL_DNS_GOOGLE_PROJECT=${GCP_PROJECT_ID}
export ACME_DNS_PROVIDER=${EXTERNAL_DNS_GOOGLE_PROJECT}-dns


# Sub-projects dir
export SUB_PROJECTS=sub-projects

# GKE Configuration
export GKE_CLUSTER_NAME=${GCP_ENVIRONMENT}-${GCP_VPC_NAME}
export GKE_NODE_TAG=${GCP_NAT_TAGS}
export KUBE_CONTEXT=${GKE_CLUSTER_NAME}

# set kube config default namespace
export KUBE_NAMESPACE=${APP_NAMESPACE}

# reserved cidrs for gke masters,  /28 CIDR blocks
export GKE_MASTER_CIDR_PROD=172.16.0.16/28
export GKE_MASTER_CIDR_STAGE=172.16.0.32/28
export GKE_MASTER_CIDR_DEV=172.16.0.48/28

# reserved cidrs for firestore,  /29 CIDR blocks
export FS_CIDR_STAGE=172.16.1.8/29
export FS_CIDR_PROD=172.16.1.16/29
export FS_CIDR_DEV=172.16.1.32/29
export FS_TIER=STANDARD
# capacity in number of TB
export FS_CAPACITY=1
export FS_NAME=filestore-${GCP_ENVIRONMENT}

#########
# Storage buckets created and used in gke-cluster-tf for each env

# gitlab backup bucket
export GITLAB_BACKUP_BUCKET=${GCP_ENVIRONMENT}-${GCP_PROJECT_NAME}-gitlab-backup
export GITLAB_BACKUP_BUCKET_LOCATION=US
export FORCE_DESTROY_GITLAB_BACKUP_BUCKET=true

# docker registry bucket
export REGISTRY_BUCKET=${GCP_ENVIRONMENT}-${GCP_PROJECT_NAME}-container-registry
export REGISTRY_BUCKET_LOCATION=US
export FORCE_DESTROY_REGISTRY_BUCKET=true

# nexus data bucket
export NEXUS_BUCKET=${GCP_ENVIRONMENT}-${GCP_PROJECT_NAME}-nexus-data
export NEXUS_BUCKET_LOCATION=US
export FORCE_DESTROY_NEXUS_BUCKET=true

# spinnaker
export SPINNAKER_BUCKET=${GCP_PROJECT_NAME}-spinnaker

############
# Cloud SQL
export MYSQL_SEC_PATH=${SEC_PATH}/cloud-sql/mysql
export POSTGRES_SEC_PATH=${SEC_PATH}/cloud-sql/postgres
export POSTGRES_MEDWIKI_SEC_PATH=${SEC_PATH}/cloud-sql/postgres-medwiki
