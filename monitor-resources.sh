#!/bin/bash

# Скрипт мониторинга ресурсов Docker контейнеров Niffler
# Использование: ./monitor-resources.sh

trap 'echo "Script interrupted"; exit 1' INT

echo "=== NIFFLER RESOURCES MONITORING ==="
echo "Time: $(date)"
echo

# Проверка доступности Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker не найден"
    exit 1
fi

if ! docker info &> /dev/null; then
    echo "❌ Docker daemon недоступен"
    exit 1
fi

# Проверка превышения лимитов памяти
echo "🔍 Memory Usage Analysis:"
docker stats --no-stream --format "table {{.Container}}\t{{.MemUsage}}\t{{.MemPerc}}" 2>/dev/null | grep -E "(auth|currency|gateway|spend|userdata|frontend|kafka|zookeeper|niffler-all-db)" || echo "No Niffler containers found"

echo
echo "🚨 High Memory Usage Containers (>80%):"
docker stats --no-stream --format "{{.Container}}\t{{.MemPerc}}" 2>/dev/null | awk '$2 > 80.0 {print $1 " - " $2}' || echo "No high memory usage detected"

echo
echo "⚡ CPU Usage Analysis:"
docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}" 2>/dev/null || echo "Failed to get CPU stats"

echo
echo "🔧 Container Health Status:"
docker ps --format "table {{.Names}}\t{{.Status}}" 2>/dev/null | grep -E "(auth|currency|gateway|spend|userdata|frontend)" || echo "No application containers found"

echo
echo "📊 Resource Limits Compliance:"
for container in auth.niffler.dc currency.niffler.dc gateway.niffler.dc spend.niffler.dc userdata.niffler.dc frontend.niffler.dc niffler-all-db kafka zookeeper; do
    if docker ps -q -f name=$container > /dev/null 2>&1; then
        echo "✅ $container - Running"
        # Получаем информацию о лимитах из docker inspect
        memory_limit=$(docker inspect $container --format='{{.HostConfig.Memory}}' 2>/dev/null)
        cpu_limit=$(docker inspect $container --format='{{.HostConfig.NanoCpus}}' 2>/dev/null)
        
        if [ "$memory_limit" -gt 0 ] 2>/dev/null; then
            echo "   Memory limit: $((memory_limit / 1024 / 1024))MB"
        else
            echo "   ⚠️  No memory limit set!"
        fi
        
        if [ "$cpu_limit" -gt 0 ] 2>/dev/null; then
            cpu_cores=$(awk "BEGIN {printf \"%.2f\", $cpu_limit / 1000000000}")
            echo "   CPU limit: ${cpu_cores}vCPUs"
        else
            echo "   ⚠️  No CPU limit set!"
        fi
    else
        echo "❌ $container - Not running"
    fi
    echo
done

echo "🌐 Network Connections:"
echo "Active connections to services:"
lsof -i :80,8089,8090,8091,8092,8093,9000,5432,9092,2181 2>/dev/null | wc -l | xargs echo "Listening ports:" || echo "Network check failed"

echo
echo "📝 Container Events (last restart times):"
docker ps --format "table {{.Names}}\t{{.Status}}" | grep -E "(auth|currency|gateway|spend|userdata|frontend|kafka|zookeeper|postgres)" || echo "No containers found"

echo
echo "🔄 Container Restart History:"
docker ps -a --format "table {{.Names}}\t{{.Status}}" | grep -E "(auth|currency|gateway|spend|userdata|frontend)" || echo "No application containers found"

echo
echo "💾 Disk Usage by Containers:"
docker system df

echo
echo "🕒 Report generated at: $(date)"