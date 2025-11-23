#!/bin/bash

# Setup Database TI di Server EC2
# Copy script ini ke server dan jalankan

echo "🚀 Setting up Accommodation TI Database..."

# Create docker-compose file
cat > ~/docker-compose-ti.yml <<'EOF'
services:
  accommodation-app-db:
    image: postgres:17.5-alpine
    container_name: accommodation-ti-db
    ports:
      - '35002:5432'
    restart: always
    environment:
      - POSTGRES_USER=travelapap
      - POSTGRES_PASSWORD=2306214510
      - POSTGRES_DB=accommodation-2306214510
    volumes:
      - accommodation-app-db:/var/lib/postgresql/data

volumes:
  accommodation-app-db:
    driver: local
EOF

echo "✅ Docker compose file created"

# Start database
echo "🐘 Starting PostgreSQL database..."
docker compose -f ~/docker-compose-ti.yml up -d

# Wait for container to start
sleep 3

# Set restart policy
echo "🔄 Setting restart policy..."
docker update --restart=always $(docker ps -q -f name=accommodation-ti-db)

# Create k8s folder
echo "📁 Creating k8s folder..."
mkdir -p ~/app/k8s

# Verify
echo ""
echo "✅ Setup complete!"
echo ""
echo "📊 Database Status:"
docker ps | grep accommodation-ti-db

echo ""
echo "🧪 Test database connection:"
echo "psql -h localhost -p 35002 -U travelapap -d accommodation-2306214510"
echo "Password: 2306214510"
echo ""
echo "🎉 Done! You can now proceed with GitLab CI/CD setup."
