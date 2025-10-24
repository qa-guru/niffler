#!/bin/bash

echo "🚀 Starting all Niffler services locally..."

# Kill any existing services
echo "🛑 Stopping existing services..."
pkill -9 -f "niffler.*bootRun" 2>/dev/null
pkill -9 -f "niffler-ai.*jar" 2>/dev/null
sleep 2

# Start services in order
echo "📦 Starting base services..."

# Start Auth service (port 9000)
nohup ./gradlew :niffler-auth:bootRun --args='--spring.profiles.active=local' > /tmp/niffler-auth.log 2>&1 &
echo "  ✓ Auth starting on port 9000"

# Start Currency service (port 8091, 8092)
nohup ./gradlew :niffler-currency:bootRun --args='--spring.profiles.active=local' > /tmp/niffler-currency.log 2>&1 &
echo "  ✓ Currency starting on port 8091/8092"

# Start Userdata service (port 8089)
nohup ./gradlew :niffler-userdata:bootRun --args='--spring.profiles.active=local' > /tmp/niffler-userdata.log 2>&1 &
echo "  ✓ Userdata starting on port 8089"

# Start Spend service (port 8093)
nohup ./gradlew :niffler-spend:bootRun --args='--spring.profiles.active=local' > /tmp/niffler-spend.log 2>&1 &
echo "  ✓ Spend starting on port 8093"

echo "⏳ Waiting for base services to start (40 seconds)..."
sleep 40

# Start Gateway service (port 8090)
echo "🌐 Starting Gateway..."
nohup ./gradlew :niffler-gateway:bootRun --args='--spring.profiles.active=local' > /tmp/niffler-gateway.log 2>&1 &
echo "  ✓ Gateway starting on port 8090"

# Start AI service (port 8094)
echo "🤖 Starting AI service..."
# Set Ollama API token (required)
export OLLAMA_API_TOKEN=${OLLAMA_API_TOKEN:-sk-xxx}
nohup java -jar -Dspring.profiles.active=local niffler-ai/build/libs/niffler-ai-2.0.5.jar > /tmp/niffler-ai.log 2>&1 &
echo "  ✓ AI service starting on port 8094"

echo "⏳ Waiting for Gateway and AI to start (15 seconds)..."
sleep 15

# Check running services
echo ""
echo "📊 Service Status:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

check_service() {
    local port=$1
    local name=$2
    if lsof -i :$port -sTCP:LISTEN > /dev/null 2>&1; then
        echo "✅ $name running on port $port"
    else
        echo "❌ $name NOT running on port $port"
    fi
}

check_service 9000 "Auth"
check_service 8089 "Userdata"
check_service 8091 "Currency"
check_service 8093 "Spend"
check_service 8090 "Gateway"
check_service 8094 "AI"

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📝 Logs are available in /tmp/"
echo "   - Auth: /tmp/niffler-auth.log"
echo "   - Currency: /tmp/niffler-currency.log"
echo "   - Userdata: /tmp/niffler-userdata.log"
echo "   - Spend: /tmp/niffler-spend.log"
echo "   - Gateway: /tmp/niffler-gateway.log"
echo "   - AI: /tmp/niffler-ai.log"
echo ""
echo "🌐 Application URLs:"
echo "   - Frontend: http://localhost:3000"
echo "   - Gateway API: http://localhost:8090"
echo "   - AI Service: http://localhost:8094/api/ai/health"
echo ""
echo "🛑 To stop all services: pkill -9 -f 'niffler.*bootRun'; pkill -9 -f 'niffler-ai.*jar'"

