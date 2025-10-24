# Niffler AI Service - Инструкция по установке и использованию

## 📋 Обзор

Создан новый микросервис `niffler-ai`, который обрабатывает запросы от фронтенда для парсинга трат с помощью ИИ (Ollama API).

### Архитектура

```
Frontend (niffler-ng-client)
    ↓
    → http://localhost:8094/api/ai/parse-spending
    ↓
AI Service (niffler-ai)
    ↓
    → https://autotests.ai/ollama/api/generate
    ↓
Ollama API (openchat:latest)
```

## 🚀 Что было сделано

### 1. Создан микросервис `niffler-ai`

**Структура:**
```
niffler-ai/
├── src/main/java/guru/qa/niffler/
│   ├── NifflerAiApplication.java        # Main application
│   ├── controller/
│   │   └── AiController.java            # REST API endpoints
│   ├── service/
│   │   └── OllamaService.java           # Ollama API integration
│   ├── model/
│   │   ├── ParseSpendingRequest.java
│   │   ├── ParseSpendingResponse.java
│   │   ├── OllamaRequest.java
│   │   └── OllamaResponse.java
│   └── config/
│       └── CorsConfig.java              # CORS configuration
├── src/main/resources/
│   └── application.yaml                 # Configuration
├── build.gradle                         # Dependencies
├── Dockerfile                           # Docker image
└── README.md                            # Documentation
```

### 2. API Endpoints

#### POST `/api/ai/parse-spending`
Парсит описание траты на естественном языке.

**Request:**
```json
{
  "userInput": "Купил кофе за 300 рублей"
}
```

**Response:**
```json
{
  "amount": 300.0,
  "category": "Рестораны",
  "description": "Купил кофе",
  "currency": "RUB",
  "spendDate": "2025-10-21"
}
```

#### GET `/api/ai/health`
Проверка состояния сервиса.

**Response:**
```
AI service is running
```

### 3. Обновлен фронтенд

Файл `niffler-ng-client/src/api/aiService.ts` теперь обращается к микросервису вместо прямого запроса к Ollama:

```typescript
const AI_SERVICE_URL = 'http://localhost:8093/api/ai';

export const aiService = {
    parseSpending: async (userInput: string): Promise<SpendingFromAI> => {
        const response = await fetch(`${AI_SERVICE_URL}/parse-spending`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userInput: userInput,
            }),
        });
        // ... обработка ответа
    },
};
```

### 4. Добавлен в Docker Compose

```yaml
ai.niffler.dc:
  container_name: ai.niffler.dc
  image: ${PREFIX}/niffler-ai-docker:latest
  ports:
    - 8093:8093
  environment:
    - JAVA_TOOL_OPTIONS=-XX:InitialHeapSize=256m -XX:MaxHeapSize=384m
  restart: always
  networks:
    - niffler-network
```

## 📦 Запуск

### Локальный запуск

1. Собрать проект:
```bash
./gradlew :niffler-ai:build -x test
```

2. Запустить микросервис:
```bash
java -jar niffler-ai/build/libs/niffler-ai-2.0.5.jar
```

Или через Gradle:
```bash
./gradlew :niffler-ai:bootRun
```

3. Проверить работу:
```bash
curl -X GET http://localhost:8094/api/ai/health
# Ответ: AI service is running

curl -X POST http://localhost:8094/api/ai/parse-spending \
  -H "Content-Type: application/json" \
  -d '{"userInput":"Купил кофе за 300 рублей"}'
```

### Docker запуск

1. Собрать Docker образ:
```bash
./gradlew :niffler-ai:jibDockerBuild
```

2. Запустить через Docker Compose:
```bash
docker-compose up ai.niffler.dc
```

## 🔧 Конфигурация

В `application.yaml`:

```yaml
server:
  port: 8094

ollama:
  api:
    url: https://autotests.ai/ollama/api/generate
    token: sk-xxx
    model: openchat:latest

```

**Примечание:** Голосовой ввод использует Web Speech API браузера и не требует дополнительной настройки на backend.

## ✅ Преимущества новой архитектуры

1. **Безопасность** - API ключ теперь на сервере, а не в клиенте
2. **Централизация** - вся логика работы с ИИ в одном месте
3. **Масштабируемость** - микросервис можно масштабировать независимо
4. **Мониторинг** - можно отслеживать использование ИИ
5. **Кеширование** - можно добавить кеширование ответов (в будущем)

## 🧪 Примеры использования

### Пример 1: Кофе
```bash
curl -X POST http://localhost:8094/api/ai/parse-spending \
  -H "Content-Type: application/json" \
  -d '{"userInput":"Купил кофе за 300 рублей"}'
```

Response:
```json
{
  "amount": 300.0,
  "category": "Рестораны",
  "description": "Купил кофе",
  "currency": "RUB",
  "spendDate": "2025-10-21"
}
```

### Пример 2: Ужин в долларах
```bash
curl -X POST http://localhost:8094/api/ai/parse-spending \
  -H "Content-Type: application/json" \
  -d '{"userInput":"Потратил 50 долларов на ужин вчера"}'
```

Response:
```json
{
  "amount": 50.0,
  "category": "Рестораны",
  "description": "ужин",
  "currency": "USD",
  "spendDate": "2025-10-20"
}
```

### Пример 3: Продукты
```bash
curl -X POST http://localhost:8094/api/ai/parse-spending \
  -H "Content-Type: application/json" \
  -d '{"userInput":"Купил продукты в магазине за 1500 руб"}'
```

## 🌐 Интеграция с фронтендом

После запуска микросервиса:

1. Запустите фронтенд: `npm run dev` в `niffler-ng-client`
2. Откройте http://localhost:3000
3. Нажмите на кнопку **"AI Spending"** (фиолетовая кнопка с иконкой ✨)
4. Выберите режим ввода:
   - **Text Input** (клавиатура): Введите описание траты, например: "Купил кофе за 300 рублей", и нажмите "Parse with AI"
   - **Voice Input** (микрофон): Нажмите на кнопку микрофона, произнесите описание траты, и нажмите кнопку остановки записи. AI автоматически обработает аудио и распознает трату
5. Проверьте распознанные данные и нажмите "Save"

### 🎤 Голосовой ввод

Новая функция голосового ввода использует встроенное **Web Speech API** браузера для преобразования речи в текст:

- Нажмите на вкладку **"Voice Input"**
- Нажмите на большую синюю кнопку с микрофоном
- Разрешите доступ к микрофону при первом использовании
- Произнесите описание траты (например: "Купил продукты за тысячу рублей")
- Вы увидите транскрибируемый текст в реальном времени
- Нажмите красную кнопку остановки
- AI автоматически распарсит информацию о трате
- Транскрибированный текст отображается для проверки

**Преимущества:**
- Работает без интернета (после загрузки страницы)
- Мгновенная транскрипция в реальном времени
- Поддержка русского и английского языков
- Не требует дополнительных API ключей

## 📝 Поддерживаемые категории

- Обучение
- Отдых
- Рестораны
- Продукты
- Транспорт
- Спорт

## 💱 Поддерживаемые валюты

- RUB (Российский рубль)
- USD (Доллар США)
- EUR (Евро)
- KZT (Казахстанский тенге)

## 🐛 Troubleshooting

### Порт 8094 занят
```bash
lsof -ti :8094 | xargs kill -9
```

### Конфликт портов
AI сервис использует порт **8094**, а не 8093, чтобы не конфликтовать с Spend сервисом.

### CORS ошибки
Убедитесь, что в `CorsConfig.java` добавлен origin вашего фронтенда.

### Ошибки подключения к Ollama
Проверьте доступность API:
```bash
curl -X POST https://autotests.ai/ollama/api/generate \
  -H "Authorization: Bearer sk-xxx" \
  -H "Content-Type: application/json" \
  -d '{"model":"openchat:latest","prompt":"Hello","stream":false}'
```

## 📊 Мониторинг

Actuator endpoints доступны по адресу:
- Health: http://localhost:8094/actuator/health
- Info: http://localhost:8094/actuator/info

## 🚀 Быстрый запуск всех сервисов

Для локальной разработки используйте скрипт:
```bash
./start-all-local.sh
```

Этот скрипт запустит все необходимые сервисы:
- Auth (9000)
- Currency (8091/8092)
- Userdata (8089)
- Spend (8093)
- Gateway (8090)
- AI (8094)

## 🎤 Возможности голосового ввода

### Технологии
- **Web Speech API**: Встроенное в браузер распознавание речи
- **Реальное время**: Транскрипция отображается сразу во время записи
- **Оффлайн режим**: Работает без интернета (в Chrome/Edge)

### Поддерживаемые языки
- Русский (основной)
- Английский
- Автоматическое определение языка

### Поддерживаемые браузеры
- ✅ Chrome/Chromium (полная поддержка)
- ✅ Microsoft Edge (полная поддержка)
- ✅ Safari (полная поддержка)
- ❌ Firefox (не поддерживает Web Speech API)

### Как это работает
1. Пользователь нажимает кнопку микрофона
2. Браузер запрашивает разрешение на доступ к микрофону
3. Web Speech API начинает распознавание речи в реальном времени
4. Транскрипция отображается прямо во время записи
5. При остановке записи текст отправляется на backend
6. Backend парсит текст через Ollama AI
7. Результат возвращается пользователю

## 🔜 Возможные улучшения

1. Добавить кеширование ответов ИИ (Redis)
2. Добавить rate limiting
3. Добавить метрики использования (Prometheus)
4. Добавить fallback на другие модели ИИ
5. Добавить валидацию и санитизацию входных данных
6. Добавить поддержку других языков
7. Добавить визуализацию звуковой волны при записи
8. Добавить предпросмотр/воспроизведение записанного аудио

---

**Автор:** AI Assistant
**Дата:** 21 октября 2025
**Версия:** 2.0.5

