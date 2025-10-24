# Niffler AI Service

Микросервис для работы с AI (Ollama) для парсинга трат из естественного языка.

## Описание

Этот микросервис предоставляет REST API для преобразования текстового описания трат в структурированные данные с помощью ИИ модели.

## Технологии

- Spring Boot 3.5.5
- Java 17+
- Ollama API (openchat:latest)

## API Endpoints

### POST /api/ai/parse-spending

Парсит описание траты на естественном языке и возвращает структурированные данные.

**Request:**
```json
{
  "userInput": "Купил кофе за 300 рублей в кафе"
}
```

**Response:**
```json
{
  "amount": 300.0,
  "category": "Рестораны",
  "description": "Кофе в кафе",
  "currency": "RUB",
  "spendDate": "2025-10-21T00:00:00Z"
}
```

### GET /api/ai/health

Проверка состояния сервиса.

## Конфигурация

Сервис использует переменные окружения для безопасного хранения API токена.

### Переменные окружения

```bash
OLLAMA_API_TOKEN=your-api-token-here         # Обязательно!
OLLAMA_API_URL=https://autotests.ai/ollama/api/generate  # Опционально (есть дефолт)
OLLAMA_API_MODEL=openchat:latest              # Опционально (есть дефолт)
```

В `application.yaml`:

```yaml
ollama:
  api:
    url: ${OLLAMA_API_URL:https://autotests.ai/ollama/api/generate}
    token: ${OLLAMA_API_TOKEN}
    model: ${OLLAMA_API_MODEL:openchat:latest}
```

## Запуск

### Local
```bash
export OLLAMA_API_TOKEN=sk-xxx
./gradlew :niffler-ai:bootRun -Dspring.profiles.active=local
```

Или через IntelliJ IDEA - добавьте переменные окружения в Run Configuration.

### Docker
```bash
./gradlew :niffler-ai:jibDockerBuild -Dspring.profiles.active=docker

# Установите переменную окружения для токена
export OLLAMA_API_TOKEN=sk-xxx

# Запустите через docker-compose
docker-compose up ai.niffler.dc
```

Или создайте файл `.env` в корне проекта:
```bash
OLLAMA_API_TOKEN=sk-xxx
```

## Порт

- HTTP: 8094

## Категории

Поддерживаемые категории трат:
- Обучение
- Отдых
- Рестораны
- Продукты
- Транспорт
- Спорт

## Валюты

Поддерживаемые валюты:
- RUB (Российский рубль)
- USD (Доллар США)
- EUR (Евро)
- KZT (Казахстанский тенге)

