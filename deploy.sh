#!/usr/bin/env bash
export ACCOUNT_ID=0060EF-863944-FAFE50	
export PROJECT_ID=cnc-app-dev
export GKE_CLUSTER_NAME=istio-cnc-app-dev-cluster
export FOLDER_ID=498985319677
export ISTIO_VERSION=1.0.0
export GIT_CLONE_LOCATION=/Users/masudhasan/Documents/workspace-sts-3.9.3.RELEASE
gcloud projects create $PROJECT_ID --folder=$FOLDER_ID --name=swarm-istio-demo
gcloud beta billing projects link $PROJECT_ID --billing-account=$ACCOUNT_ID
gcloud config set core/project $PROJECT_ID 


gcloud beta container --project $PROJECT_ID clusters create $GKE_CLUSTER_NAME --region "us-central1" --username "admin" --cluster-version "1.10.5-gke.4" --machine-type "n1-standard-8" --image-type "COS" --disk-type "pd-standard" --scopes "https://www.googleapis.com/auth/cloud-platform" --num-nodes "1" --enable-stackdriver-kubernetes --network "projects/scotia-tokenization/global/networks/default" --subnetwork "projects/scotia-tokenization/regions/us-central1/subnetworks/default" --addons HorizontalPodAutoscaling,HttpLoadBalancing,KubernetesDashboard --no-enable-autoupgrade --enable-autorepair


gcloud beta container --project $PROJECT_ID clusters create $GKE_CLUSTER_NAME  --zone "us-central1-a" --username "admin" --cluster-version "1.10.5-gke.4" --machine-type "n1-standard-1" --image-type "COS" --disk-type "pd-standard" --disk-size "100" --scopes "https://www.googleapis.com/auth/cloud-platform" --num-nodes "3" --enable-stackdriver-kubernetes --network "projects/cnc-app-dev/global/networks/default" --subnetwork "projects/cnc-app-dev/regions/us-central1/subnetworks/default" --addons HorizontalPodAutoscaling,HttpLoadBalancing --no-enable-autoupgrade --enable-autorepair

gcloud services enable compute.googleapis.com container.googleapis.com spanner.googleapis.com
gcloud beta container --project $PROJECT_ID clusters create $GKE_CLUSTER_NAME --zone "us-central1-a" --username "admin" --cluster-version "1.9.7-gke.1" --machine-type "n1-standard-1" --image-type "COS" --disk-type "pd-standard" --disk-size "100" --scopes "https://www.googleapis.com/auth/cloud-platform" --num-nodes "3" --enable-cloud-logging --enable-cloud-monitoring --network "default" --subnetwork "default" --addons HorizontalPodAutoscaling,HttpLoadBalancing,KubernetesDashboard --enable-autorepair


gcloud container clusters get-credentials $GKE_CLUSTER_NAME --region us-central1 --project $PROJECT_ID

cd $HOME/Documents/workspace-sts-3.9.3.RELEASE/istio-$ISTIO_VERSION
export PATH=$PWD/bin:$PATH
kubectl create clusterrolebinding cluster-admin-binding \
    --clusterrole=cluster-admin \
    --user=$(gcloud config get-value core/account)

kubectl apply -f install/kubernetes/istio-demo-auth.yaml --as=admin --as-group=system:masters
install/kubernetes/webhook-create-signed-cert.sh \
    --service istio-sidecar-injector \
    --namespace istio-system \
    --secret sidecar-injector-certs
kubectl apply -f install/kubernetes/istio-sidecar-injector-configmap-release.yaml
cat install/kubernetes/istio-sidecar-injector.yaml | \
     ./install/kubernetes/webhook-patch-ca-bundle.sh > \
     install/kubernetes/istio-sidecar-injector-with-ca-bundle.yaml
kubectl apply -f install/kubernetes/istio-sidecar-injector-with-ca-bundle.yaml
kubectl label namespace default istio-injection=enabled
kubectl apply -f install/kubernetes/addons/servicegraph.yaml
kubectl apply -f install/kubernetes/addons/grafana.yaml
kubectl apply -f install/kubernetes/addons/prometheus.yaml
kubectl apply -f install/kubernetes/addons/zipkin.yaml
kubectl get deployments,ing -n istio-system
#gcloud projects delete $PROJECT_ID 
gcloud spanner instances create spanner-demo --config=regional-us-central1 --nodes=1 --description="Istio API Demo"
gcloud spanner databases create student-data --instance=spanner-demo
gcloud spanner databases ddl update student-data --instance=spanner-demo --ddl="$(</Users/masudhasan/Documents/workspace-sts-3.9.3.RELEASE/istio-spring-gke/db/schema.ddl)"
kubectl apply -f <(istioctl kube-inject --debug=true -f $GIT_CLONE_LOCATION/istio-spring-gke/kubernetes/student-service-deployment.yaml)
kubectl apply -f <(istioctl kube-inject --debug=true -f $GIT_CLONE_LOCATION/istio-spring-gke/kubernetes/registration-service-deployment.yaml)
kubectl apply -f <(istioctl kube-inject --debug=true -f $GIT_CLONE_LOCATION/istio-spring-gke/kubernetes/api-gateway-deployment.yaml)
kubectl apply -f <(istioctl kube-inject --debug=true -f $GIT_CLONE_LOCATION/istio-spring-gke/kubernetes/service-ingress.yaml)
kubectl apply -f <(istioctl kube-inject --debug=true -f $GIT_CLONE_LOCATION/istio-spring-gke/istio/api-routes.yaml)
kubectl get deployments
kubectl get pods
kubectl get ing