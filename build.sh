set -e

export DOCKER_BUILDKIT=1
export REGISTRY="192.168.56.200:5000"
export IMAGE_RE="rental-service"
export IMAGE_PA="payment-service"
export IMAGE_FL="fleet-service"
export VERSION_TAG=$(date +%Y%m%d-%H%M%S)
export POSTGRES_USER="postgres"
export POSTGRES_PASSWORD="password"

build_image() {
    IMAGE_NAME=$1
    FULL_IMAGE="$REGISTRY/$IMAGE_NAME"
    docker build -t ${FULL_IMAGE}:${VERSION_TAG} -t ${FULL_IMAGE}:latest backend-java/${IMAGE_NAME}
}

# Build images
build_image $IMAGE_RE
build_image $IMAGE_PA
build_image $IMAGE_FL

# Test
docker compose up -d 
sleep 40

if curl -s http://localhost:80/api/map/stations | grep -q "Hala Stulecia"; then
    echo "✅ SUCCESS"
    docker compose down
else
    echo "❌ ERROR"
    docker compose down
    exit 1
fi