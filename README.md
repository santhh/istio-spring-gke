## This is a spring cloud implementation for a microservice architecture using Istio Auth in GKE
 Deployed in GKE and use Spanner as a database. There are two services (Student & Registration) and a gateway service

# Create a GKE Cluster: 
	gcloud container clusters create istio-cluster-2     --cluster-version=1.9.6     --region=us-central1     --num-nodes=2     --machine-type=n1-standard-2     --enable-autorepair     --no-enable-cloud-logging     --no-enable-cloud-monitoring --scopes "https://www.googleapis.com/auth/cloud-platform"
# Set Cluster Credentials
	gcloud beta container clusters get-credentials istio-cluster-2 --region us-central1 --project swarm-istio-spring-demo
# 	Set Cluster Role Binding
	kubectl create clusterrolebinding cluster-admin-binding \
    --clusterrole=cluster-admin \
    --user=$(gcloud config get-value core/account)
    
# Deploy Istio Basic components (assumed Istio is already installed locally)
	kubectl apply -f install/kubernetes/istio-auth.yaml
# Deploy sidecar injector (webhook)- Need 1.9 and above GKE
	install/kubernetes/webhook-create-signed-cert.sh \
    --service istio-sidecar-injector \
    --namespace istio-system \
    --secret sidecar-injector-certs

	kubectl apply -f install/kubernetes/istio-sidecar-injector-configmap-release.yaml	

	cat install/kubernetes/istio-sidecar-injector.yaml | \
     ./install/kubernetes/webhook-patch-ca-bundle.sh > \
     install/kubernetes/istio-sidecar-injector-with-ca-bundle.yaml	
	
	kubectl apply -f install/kubernetes/istio-sidecar-injector-with-ca-bundle.yaml
# Enable sidecar injector to default namespace
	kubectl label namespace default istio-injection=enabled	
# Deploy addons
	kubectl apply -f install/kubernetes/addons/servicegraph.yaml
	kubectl apply -f install/kubernetes/addons/grafana.yaml
	kubectl apply -f install/kubernetes/addons/prometheus.yaml
	kubectl apply -f install/kubernetes/addons/zipkin.yaml
	
# 	Validate everything is running in istio namespace
	kubectl get deployments,ing -n istio-system
	
# Spanner DB Setup
	
	gcloud spanner instances create spanner-demo --config=regional-us-central1 --nodes=1 --description="Student API Demo"
	gcloud spanner databases create student-data --instance=spanner-demo
	gcloud spanner databases ddl update student-data --instance=spanner-demo --ddl="$(</Users/masudhasan/Documents/workspace-sts-3.9.3.RELEASE/istio-spring-gke/db/schema.ddl)"
# Local build 
	mvn clean package install -PbuildDocker
# Tag and Push Docker images 
		docker tag swarm/gcp-istio-student-service gcr.io/swarm-istio-spring-demo/swarm/gcp-istio-student-service:v10
		docker tag swarm/gcp-istio-api-gateway gcr.io/swarm-istio-spring-demo/swarm/gcp-istio-api-gateway:v10
		docker tag swarm/gcp-istio-registration-service gcr.io/swarm-istio-spring-demo/swarm/gcp-istio-registration-service:v10
		docker push gcr.io/swarm-istio-spring-demo/swarm/gcp-istio-student-service
		docker push gcr.io/swarm-istio-spring-demo/swarm/gcp-istio-registration-service
		docker push gcr.io/swarm-istio-spring-demo/swarm/gcp-istio-api-gateway
# Deploy microservices
	kubectl apply -f <(istioctl kube-inject -f /Users/masudhasan/Documents/workspace-sts-3.9.3.RELEASE/istio-spring-gke/kubernetes/student-service-deployment.yaml)
	kubectl apply -f <(istioctl kube-inject -f /Users/masudhasan/Documents/workspace-sts-3.9.3.RELEASE/istio-spring-gke/kubernetes/registration-service-deployment.yaml)
	kubectl apply -f <(istioctl kube-inject -f /Users/masudhasan/Documents/workspace-sts-3.9.3.RELEASE/istio-spring-gke/kubernetes/api-gateway-deployment.yaml)
	kubectl apply -f <(istioctl kube-inject -f /Users/masudhasan/Documents/workspace-sts-3.9.3.RELEASE/istio-spring-gke/kubernetes/service-ingress.yaml)
	kubectl apply -f <(istioctl kube-inject -f /Users/masudhasan/Documents/workspace-sts-3.9.3.RELEASE/istio-spring-gke/istio/api-routes.yaml)
	
# Test 
	http://35.184.171.48/api/gateway/students/1001
	{
    "studentId": "1001",
    "firstName": "John",
    "lastName": "Doe",
    "registrations": [
        {
            "registrationId": "1",
            "studentId": "1001",
            "courseId": "CSC100"
        }
    ]
	}
		
	
	
  	
  	